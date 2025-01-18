pipeline {
    agent any
    
    triggers {
        gitlab(
            branchFilterType: 'NameBasedFilter',
            includeBranchesSpec: 'springtest',
            triggerOnPush: true
        )
    }
    
    environment {
        COMPOSE_PROJECT_NAME = "springtest"
        DOCKER_USER = "dreamingj98"
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
                                sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin"
                                sh """
                                    docker-compose build
                                    docker-compose push
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
                        sshUserPrivateKey(
                            credentialsId: 'ec2-ssh-key',
                            keyFileVariable: 'SSH_KEY'
                        )
                    ]) {
                        sh """
                            scp -i \${SSH_KEY} docker-compose.yml \${EC2_SERVER}:~/
                            ssh -i \${SSH_KEY} \${EC2_SERVER} '
                                echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin
                                
                                # 이전 컨테이너 정리
                                docker-compose down --remove-orphans
                                
                                # 새 이미지 가져오기 및 실행
                                docker-compose pull
                                docker-compose up -d
                                
                                # Docker 이미지 정리 (선택사항)
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