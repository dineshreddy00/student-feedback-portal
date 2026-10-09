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
                        echo "No previous WAR found."
                        rm -f "$WORKSPACE/rollback/student-feedback-portal.war"
                    fi
                '''
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sh '''
                    set -e

                    echo "Removing previous deployed application..."
                    rm -rf /var/lib/tomcat10/webapps/student-feedback-portal
                    rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war

                    echo "Deploying new WAR..."
                    cp target/student-feedback-portal.war \
                       /var/lib/tomcat10/webapps/student-feedback-portal.war

                    echo "Waiting for Tomcat deployment..."
                    sleep 5

                    echo "Deployment completed."
                '''
            }
        }

        stage('Health Check') {
            steps {
                script {
                    try {

                        echo 'Running application health check...'

                        sh '''
                            curl --fail --show-error --silent \
                            http://localhost:8081/student-feedback-portal/health
                        '''

                        echo 'HEALTH CHECK PASSED.'

                    } catch (Exception e) {

                        echo 'HEALTH CHECK FAILED!'
                        echo 'Starting rollback to previous working WAR...'

                        sh '''
                            set -e

                            if [ -f "$WORKSPACE/rollback/student-feedback-portal.war" ]; then

                                rm -rf /var/lib/tomcat10/webapps/student-feedback-portal
                                rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war

                                cp "$WORKSPACE/rollback/student-feedback-portal.war" \
                                   /var/lib/tomcat10/webapps/student-feedback-portal.war

                                echo "Previous WAR restored successfully."

                                sleep 5

                            else
                                echo "ERROR: No backup WAR available."
                                exit 1
                            fi
                        '''

                        echo 'Checking application after rollback...'

                        sh '''
                            curl --fail --show-error --silent \
                            http://localhost:8081/student-feedback-portal/health
                        '''

                        echo 'ROLLBACK SUCCESSFUL.'
                        echo 'Previous working version is running again.'

                        error('New deployment failed health check; rollback completed successfully.')
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
            echo 'PIPELINE FAILED - ROLLBACK WAS ATTEMPTED IF DEPLOYMENT FAILED.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
