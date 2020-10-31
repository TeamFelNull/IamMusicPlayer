pipeline {
  agent {
    docker {
      image 'gradle:4.10.3-jdk11'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'source .bash_profile'
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