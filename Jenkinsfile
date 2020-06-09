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
        checkout([$class: 'GitSCM', branches: [[name: '*/project24']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/gilad4591/TEAM24PROJECT.git']]])
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
    stage('Unit & Integration Tests') {
                steps {
                    script
                        //run a gradle test
                        sh './gradlew clean test --no-daemon'
                        sh './gradlew assemble connectedAndroidTest'
                        junit '*/build/test-results/testDebugUnitTest/*.xml' {
                    }

                }
            }

//     stage ('Cloud Test Lab'){
//       steps {
//        script{
//            sh "gcloud auth activate-service-account --key-file /opt/service_account_key.json"
//            sh "gcloud firebase test android run --project ${env.gcloud_project_id} --app app/build/outputs/apk/app-debug.apk --test app/build/outputs/apk/app-debug-androidTest.apk --device model=Nexus6,version=22,locale=en,orientation=portrait"
//            }
//        }
//    }
    stage('Static Code Analysis') {
            steps {
                script {
                    sh './gradlew checkstyle'
                    recordIssues enabledForFailure: true, aggregatingResults: true, tool: checkStyle(pattern: 'app/build/reports/checkstyle/checkstyle.xml')
                }
            }
        }
  }
 post {
        success {
            echo 'BUILD SUCCESSFUL - NO EMAIL WILL BE SENT'
        }
        always{
              sh 'cp -r app/build/test-results $WORKSPACE/test-results'
              junit(keepLongStdio: true, testResults: '**/test-results/**/*.xml')
        }
        failure { //Send an email to to all teammates about broken build
            emailext(subject: '$JOB_NAME - Build# $BUILD_NUMBER - $BUILD_STATUS',
                    body: '$DEFAULT_CONTENT',
                    replyTo: 'afikziv@gmail.com',
                    to: 'afikziv@gmail.com'
            )

            emailext(subject: '$JOB_NAME - Build# $BUILD_NUMBER - $BUILD_STATUS',
                    body: '$DEFAULT_CONTENT',
                    replyTo: 'tamiryakov@gmail.com',
                    to: 'tamiryakov@gmail.com')
       }
    }  
}
