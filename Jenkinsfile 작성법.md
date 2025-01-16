# Jenkinsfile 작성하기

## Jenkinsfile을 읽는 기준

Jenkinsfile은 Jenkins의 item의 설정인 pipeline에서 지정한 **Branch Specifier**에 적힌 branch에 위치한 것을 읽는 것이다.

그리고 바로 아래의 Script Path는 해당 Jenkinsfile이 어느 경로에 위치하는지를 적어주면 된다.

## Branch

만약 특정 branch에서만 빌드를 진행하고 싶다면 when을 사용하여 branch를 지정할 수 있다.
단, 이 방식은 빌드 기록은 남기 때문에 기록조차 남기고 싶지 않을 경우, triggers를 이용할 수 있다.

```
pipeline {
    agent any

    triggers {
        gitlab {
            branchFilterType: 'NameBasedFilter'
            includeBranchesSpec: 'springtest'
            serverName: 'A805'
            secretToken: 'YOUR_SECRET_TOKEN'
            triggerOnPush: true
        }
    }

    environment {
        DOCKER_IMAGE = "your-dockerhub-username/your-app-name"
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        EC2_SERVER = "your-ec2-username@your-ec2-ip"
        SSH_KEY = credentials('ec2-ssh-key-id')
    }

    stages {

        stage('Build and Push Docker Image') {
            steps {
                dir('backend') {  // backend 폴더로 이동
                    script {
                        // Docker 로그인
                        sh """
                            echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                        """

                        // Docker 이미지 빌드
                        sh """
                            docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                        """

                        // Docker 이미지 푸시
                        sh """
                            docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push ${DOCKER_IMAGE}:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    // SSH를 통한 EC2 접속 및 배포
                    sshagent([SSH_KEY]) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${EC2_SERVER} '
                                # Docker 로그인
                                echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin

                                # 기존 컨테이너 중지 및 삭제
                                docker stop your-container-name || true
                                docker rm your-container-name || true

                                # 새 이미지 풀 및 실행
                                docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}
                                docker run -d \
                                    --name your-container-name \
                                    -p 8080:8080 \
                                    ${DOCKER_IMAGE}:${DOCKER_TAG}
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Docker 로그아웃
            sh 'docker logout'
        }
    }
}
```