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
                echo 'Creating WAR file...'
                sh 'mvn package'
            }
        }

        stage('Archive Artifact') {
            steps {
                echo 'Archiving WAR artifact...'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying application to Tomcat...'

                sh '''
                    rm -f /var/lib/tomcat10/webapps/student-feedback-portal.war

                    cp target/student-feedback-portal.war \
                    /var/lib/tomcat10/webapps/student-feedback-portal.war

                    sleep 5
                '''
            }
        }

        stage('Health Check') {
            steps {
                echo 'Checking application health...'

                sh '''
                    curl -f http://localhost:8081/student-feedback-portal/health
                '''
            }
        }
    }

    post {

        success {
            echo 'CI/CD PIPELINE COMPLETED SUCCESSFULLY!'
        }

        failure {
            echo 'PIPELINE FAILED - DEPLOYMENT WAS NOT SUCCESSFUL.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
