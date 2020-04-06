
pipeline{
    agent any
        stages{
            stage("Checkout") {
                steps {
                  git branch: 'project24', credentialsId: 'b71ff76d-4285-4437-bdab-c80aa49697fd', url: 'https://github.com/gilad4591/TEAM24PROJECT.git'
                }
            }
            stage("build debug APK"){
                steps{
                    sh "./gradlew assembleDebug"
                }
            }
        }
    }
