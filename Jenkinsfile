pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = "backend"
        DOCKER_USER = ""
    }

    stages {
        stage('Build and Push with Docker Compose') {
            steps {
                dir('.') {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credential',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )]) {
                        script {
                           try {
                                sh """
                                    echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin
                                    pwd && ls -la  # 현재 디렉토리와 파일 목록 확인
                                    docker-compose -f docker-compose.yml build
                                    docker-compose -f docker-compose.yml push
                                """
                            } catch (Exception e) {
                                echo "Build failed: ${e.message}"
                                throw e
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'dockerhub-credential',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASSWORD'
                        ),
                        string(
                            credentialsId: 'ec2-server',
                            variable: 'EC2_SERVER'
                        ),
                        file(
                            credentialsId: 'ec2-ssh-key-file',
                            variable: 'SSH_KEY_FILE'
                        )
                    ]) {
                        sh """
                            chmod 600 \${SSH_KEY_FILE}  # PEM 파일 권한 설정
                            scp -i \${SSH_KEY_FILE} docker-compose.yml \${EC2_SERVER}:~/
                            ssh -i \${SSH_KEY_FILE} \${EC2_SERVER} '
                                echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin
                                
                                # 특정 프로젝트의 컨테이너만 중지하고 제거
                                docker-compose down
                                
                                # 해당 프로젝트의 컨테이너만 재시작
                                docker-compose pull
                                docker-compose up -d
                                
                                # 사용하지 않는 이미지만 정리 (강제성 제거)
                                docker image prune -f
                                
                                docker logout
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
}
