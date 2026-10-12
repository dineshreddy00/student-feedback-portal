pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
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
                sh """
                    set -e
                    mkdir -p "$WORKSPACE/rollback"
                    cp /var/lib/jenkins/rollback-evidence/student-feedback-portal-good.war "$WORKSPACE/rollback/student-feedback-portal.war"
                    echo "KNOWN-GOOD WAR backed up successfully."
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sh '''
                    set -e
                    DEPLOY=/var/lib/tomcat10/webapps
                    APP="$DEPLOY/student-feedback-portal"
                    WAR="$DEPLOY/student-feedback-portal.war"
                    FAILED="$DEPLOY/student-feedback-portal.failed-$(date +%s)"

                    if [ -d "$APP" ]; then
                        mv "$APP" "$FAILED"
                    fi

                    rm -f "$WAR"
                    cp target/student-feedback-portal.war "$WAR"
                    sleep 5
                    echo "Deployment command completed."
                '''
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
                            DEPLOY=/var/lib/tomcat10/webapps
                            APP="$DEPLOY/student-feedback-portal"
                            WAR="$DEPLOY/student-feedback-portal.war"
                            FAILED="$DEPLOY/student-feedback-portal.failed-rollback-$(date +%s)"

                            if [ -d "$APP" ]; then
                                mv "$APP" "$FAILED"
                            fi

                            rm -f "$WAR"
                            cp "$WORKSPACE/rollback/student-feedback-portal.war" "$WAR"
                            sleep 8
                        '''

                        sh '''
                            curl --fail --show-error --silent \
                              http://localhost:8081/student-feedback-portal/health
                        '''

                        echo 'ROLLBACK SUCCESSFUL: previous WAR restored.'
                        error('New deployment failed; previous WAR restored.')
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
            echo 'PIPELINE FAILED. Check deployment and rollback messages.'
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
