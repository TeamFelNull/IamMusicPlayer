pipeline {
  agent {
    docker {
      image 'gradle:4.10.3-jdk8'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'gradle build'
      }
    }

    stage('Upload') {
      steps {
        archiveArtifacts 'build/libs/*'
      }
    }

    stage('libs Clean') {
      steps {
        sh 'rm -fr build/libs/*'
      }
    }

  }
}