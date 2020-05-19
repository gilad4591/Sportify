pipeline {
  agent {
    docker {
      image 'windsekirun/jenkins-android-docker:1.1.1'
    }
  }
  options {
    skipStagesAfterUnstable()
  }
  stages {
    stage('Checkout'){
      steps{
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/AvielCo/ParkNBark.git']]])
      }
    }
    stage ('Prepare'){
      steps {
        sh 'chmod +x ./gradlew'
      }
    }
    stage('Compile') {
      steps {
        sh './gradlew compileDebugSources'
      }
    }
    stage('Unit test') {
      steps {
        sh './gradlew testDebugUnitTest testDebugUnitTest'
        
      }
    }
    stage('Build APK') {
      steps {
        sh './gradlew assembleDebug'
      }
    }
  }
  post{
      failure{
        step([$class: 'Mailer', 
        notifyEveryUnstableBuild: true, 
        recipients: 'afikzi@ac.sce.ac.il', 
        sendToIndividuals: false])
       }
    }  
}
