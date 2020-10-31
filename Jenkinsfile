pipeline {
  agent {
    docker {
      image 'gradle:4.10.3-jdk8'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/ && JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/ && export CLASSPATH=/usr/lib/jvm/java-8-openjdk-amd64/lib && CLASSPATH=/usr/lib/jvm/java-8-openjdk-amd64/lib'
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