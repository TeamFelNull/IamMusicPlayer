pipeline {
  agent {
    docker {
      image 'gradle:4.10.3-jdk11'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'export JAVA_HOME=/usr/lib/jvm/openjdk-11/ && JAVA_HOME=/usr/lib/jvm/openjdk-11/ && gradle build'
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