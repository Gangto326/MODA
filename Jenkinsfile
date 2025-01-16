pipeline {
    agent any

    triggers {
        gitlab(
            triggerOnPush: true,
            triggerOnMergeRequest: false,
            branchFilterType: 'NameBasedFilter',
            includeBranchesSpec: 'test',
            secretToken: 'f8963042e5ea4955c9cf51f34002518d'
        )
    }

    stages {

        stage('Create Test Directory') {
            steps {
                script {
                    // 현재 시간을 포함한 디렉토리 이름 생성
                    def timestamp = new Date().format('yyyyMMdd_HHmmss')
                    def dirName = "test_${timestamp}"

                    // EC2에 SSH로 접속하여 디렉토리 생성
                    sshagent(['ec2-server']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ec2-user@52.79.239.182 '
                                sudo mkdir -p /home/ec2-user/${dirName}
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Directory creation successful!'
        }
        failure {
            echo 'Directory creation failed!'
        }
    }
}
