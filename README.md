# Student Feedback Portal

A simple web-based Student Feedback Portal developed as an iStudio DevOps CI/CD project.

## Project Overview

The Student Feedback Portal allows students to submit feedback through a web interface. The application is built using Java and Maven and deployed on Apache Tomcat through a Jenkins CI/CD pipeline.

## Technologies Used

- Java
- Maven
- JUnit
- Git & GitHub
- Jenkins
- Apache Tomcat
- Linux
- HTML/CSS

## Features

- Student feedback form
- Name and email validation
- Feedback submission
- Feedback success confirmation
- Application health-check endpoint
- Automated unit testing
- Automated WAR packaging
- Jenkins CI/CD pipeline
- Deployment to Tomcat

## Git Branching Strategy

- `main` – Production-ready code
- `dev` – Development branch
- `feature/feedback-form` – Feedback form development
- `feature/unit-tests` – Unit testing development

## CI/CD Pipeline

The Jenkins pipeline performs the following stages:

1. Checkout SCM
2. Checkout
3. Build
4. Test
5. Package
6. Archive Artifact
7. Deploy to Tomcat
8. Health Check

## Application Health Check

The application provides a health-check endpoint:

`/student-feedback-portal/health`

Expected response:

```text
STATUS: UP
APPLICATION: Student Feedback Portal
SERVICE: Tomcat
