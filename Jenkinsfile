pipeline {
  agent {
    docker {
      image 'maven:3.6.3-openjdk-11'
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