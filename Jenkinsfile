
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building Java application...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Creating WAR package...'
                sh 'mvn package'
            }
        }

        stage('Verify WAR') {
            steps {
                sh '''
                    test -f target/student-feedback-portal.war
                    echo "WAR file verified successfully."
                    ls -lh target/student-feedback-portal.war
                '''
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Backup Current Deployment') {
            steps {
                sh '''
                    set -e
                    mkdir -p "$WORKSPACE/rollback"

                    if [ -f /var/lib/tomcat10/webapps/student-feedback-portal.war ]; then
                        cp /var/lib/tomcat10/webapps/student-feedback-portal.war \
                           "$WORKSPACE/rollback/student-feedback-portal.war"
                        echo "Previous working WAR backed up successfully."
                    else
                        echo "ERROR: No previous WAR available for rollback."
                        exit 1
                    fi
                '''
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    try {
                        sh '''
                            set -e
                            echo "Removing previous deployed application..."
                            rm -rf /var/lib/tomcat10/webapps/student-feedback-portal
                            rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war

                            echo "Deploying new WAR..."
                            cp target/student-feedback-portal.war \
                               /var/lib/tomcat10/webapps/student-feedback-portal.war

                            sleep 5
                            echo "Deployment command completed."
                        '''
                    } catch (Exception e) {
                        echo 'DEPLOYMENT FAILED! Starting rollback...'

                        sh '''
                            set -e
                            rm -rf /var/lib/tomcat10/webapps/student-feedback-portal
                            rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war
                            cp "$WORKSPACE/rollback/student-feedback-portal.war" \
                               /var/lib/tomcat10/webapps/student-feedback-portal.war
                            sleep 5
                        '''

                        sh '''
                            curl --fail --show-error --silent \
                            http://localhost:8081/student-feedback-portal/health
                        '''

                        echo 'ROLLBACK SUCCESSFUL: previous application restored.'
                        error('Deployment failed; previous application restored.')
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    try {
                        sh '''
                            curl --fail --show-error --silent \
                            http://localhost:8081/student-feedback-portal/health
                        '''
                        echo 'HEALTH CHECK PASSED.'
                    } catch (Exception e) {
                        echo 'HEALTH CHECK FAILED! Starting rollback...'

                        sh '''
                            set -e
                            rm -rf /var/lib/tomcat10/webapps/student-feedback-portal
                            rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war
                            cp "$WORKSPACE/rollback/student-feedback-portal.war" \
                               /var/lib/tomcat10/webapps/student-feedback-portal.war
                            sleep 5
                        '''

                        sh '''
                            curl --fail --show-error --silent \
                            http://localhost:8081/student-feedback-portal/health
                        '''

                        echo 'ROLLBACK SUCCESSFUL: previous application restored.'
                        error('Health check failed; previous application restored.')
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'CI/CD PIPELINE COMPLETED SUCCESSFULLY!'
        }
        failure {
            echo 'PIPELINE FAILED. Check the deployment and rollback messages above.'
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
