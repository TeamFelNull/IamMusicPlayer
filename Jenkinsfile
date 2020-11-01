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

    stage('Notice') {
      steps {
        discordSend(webhookURL: 'https://discordapp.com/api/webhooks/772325029634899968/_x-m33bF38HrhbLoagfwZwmumy7Nqm1tqdd7i4X90ZCQKiX4714RBjPCAB1Vc_sJSxq5', successful: true, title: 'JOB_NAME', link: 'env.BUILD_URL', description: 'Jenkins Pipeline Build')
      }
    }

  }
}