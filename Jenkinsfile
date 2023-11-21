pipeline {
    agent any
    stages {
        stage('Maven Build') {
            steps {
                script {
                    sh 'ls'
                    sh 'mvn clean'
                    sh 'npm install'
                    sh 'npm run build'
//                     sh 'docker build -t kjms-manager-portal:latest .'
//                     sh 'mvn package -DskipTests -Pprod,api-docs,no-liquibase jib:dockerBuild'
                    sh 'mvn package -DskipTests -Pdev,api-docs jib:dockerBuild' 
                                    //    sonar:sonar \
                                    //   -Dsonar.projectKey=kryonneo \
                                    //   -Dsonar.host.url=https://sonar.kkwtk.com \
                                    //   -Dsonar.login=sqp_62c1931b77353e0d3832f2722e668183b36d2337 -X'
                }
            }
        }

//         stage('Confirm Running Deploy') {
//             steps {
//                 timeout(time: 5, unit: 'MINUTES') {
//                  input(message: "Do you want to deploy the build?")
//                }
//             }
//         }

        stage('Build & Deploy') {
            steps {
                script {
                    withCredentials([azureServicePrincipal('tk-product-uat-env-spn')]) {
                        sh 'az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID'
                        sh 'az account set -s $AZURE_SUBSCRIPTION_ID'
                        sh 'az acr login --name uatkjms --resource-group UAT-KJMS'
                        sh 'docker tag kjms  uatkjms.azurecr.io/kjms-manager-portal:latest'
                        sh 'docker push  uatkjms.azurecr.io/kjms-manager-portal:latest'
                        sh 'az logout'
                    }
                }
            }
        }
        stage('Post Deployment') {
                    steps {
                        script {
                        sh 'docker rmi kjms'
                        cleanWs()
                        }
                    }
                }
    }
}
