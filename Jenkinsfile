def baseRepoUrl = 'https://github.com/MeetingTeam'
def mainBranch = 'main'

def appRepoName = 'user-service'
def appRepoUrl = "${baseRepoUrl}/${appRepoName}.git"
def appVersion = "1.0"

def k8SRepoName = 'k8s-repo'
def helmPath = "${k8SRepoName}/application/${appRepoName}"
def helmValueFile = "values.dev.yaml"

def githubAccount = 'github'
def kanikoAccount = 'kaniko'

def migrationPath = 'src/main/resources/migrations'
def migrationChanges = ''

def trivyReportFile = 'trivy_report.html'

def sonarOrg = 'meetingteam'

pipeline {
    agent {
        kubernetes {
            inheritFrom 'springboot'
        }
    }

    environment {
        DOCKER_REGISTRY = 'registry-1.docker.io'
        IMAGE_VERSION = "${appVersion}-${GIT_COMMIT.take(7)}-${BUILD_NUMBER}"

        DOCKER_IMAGE_NAME = 'hungtran679/mt_user-service'
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${IMAGE_VERSION}"

        DOCKER_FLYWAY_IMAGE_NAME = 'hungtran679/mt_flyway-user-service'
        DOCKER_FLYWAY_IMAGE = "${DOCKER_REGISTRY}/${DOCKER_FLYWAY_IMAGE_NAME}:${IMAGE_VERSION}"
    }

    stages {
        stage('Setup credentials for maven') {
            steps {
                container('maven') {
                    withCredentials([
                        usernamePassword(
                            credentialsId: githubAccount,
                            passwordVariable: 'GIT_PASS',
                            usernameVariable: 'GIT_USER'
                        )
                    ]) {
                        script {
                            def settingsXml = """
                                <settings>
                                    <servers>
                                        <server>
                                            <id>github</id>
                                            <username>${GIT_USER}</username>
                                            <password>${GIT_PASS}</password>
                                        </server>
                                    </servers>
                                </settings>
                            """
                            writeFile file: 'settings.xml', text: settingsXml.trim()
                            sh 'mv settings.xml /root/.m2/settings.xml'
                        }
                    }
                }
            }
        }

        stage('Compile code stage') {
            steps {
                container('maven') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Unit test stage') {
            steps {
                container('maven') {
                    sh 'mvn test'
                }
            }
        }

        stage('Code analysis') {
            steps {
                container('maven') {
                    withSonarQubeEnv('SonarServer') {
                        sh "mvn sonar:sonar -Dsonar.organization=${sonarOrg}"
                    }
                }
            }
        }

        stage('Quality gate check') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate(abortPipeline: true)
                }
            }
        }

        stage('Build jar file') {
            when { branch mainBranch }
            steps {
                container('maven') {
                    sh "mvn clean package -DskipTests=true"
                }
            }
        }

        stage('Build and push docker image') {
            when { branch mainBranch }
            steps {
                container('kaniko') {
                    withCredentials([
                        string(credentialsId: kanikoAccount, variable: 'KANIKO_AUTH')
                    ]) {
                        script {
                            def dockerConfig = """
                                {
                                    "auths": {
                                        "${DOCKER_REGISTRY}": {
                                            "auth": "${KANIKO_AUTH}"
                                        }
                                    }
                                }
                            """
                            writeFile file: 'config.json', text: dockerConfig.trim()

                            sh """
                                mv config.json /kaniko/.docker/config.json
                                /kaniko/executor \
                                    --context=. \
                                    --dockerfile=Dockerfile \
                                    --destination=${DOCKER_IMAGE}
                            """
                        }
                    }
                }
            }
        }

        stage('Scan built image') {
            when { branch mainBranch }
            steps {
                container('trivy') {
                    sh """
                        wget -O html.tpl https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl
                        trivy image --format template --template "@html.tpl" -o ${trivyReportFile} \
                            --timeout 15m --scanners vuln ${DOCKER_IMAGE}
                    """
                }
            }
        }

        stage('Check for changes in migration folder') {
            when { branch mainBranch }
            steps {
                script {
                    if (env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
                        migrationChanges = sh(
                            script: "git diff --name-only ${GIT_PREVIOUS_SUCCESSFUL_COMMIT} ${GIT_COMMIT} -- ${migrationPath}",
                            returnStdout: true
                        ).trim()
                    }
                    if (migrationChanges) {
                        echo "There is change in migration scripts compared to previous success build: ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                        echo 'Start build flyway image'
                        container('kaniko') {
                            sh """
                                /kaniko/executor \
                                    --context=. \
                                    --dockerfile=Flyway-dockerfile \
                                    --destination=${DOCKER_FLYWAY_IMAGE}
                            """
                        }
                    }
                }
            }
        }

        stage('Update k8s repo') {
            when { branch mainBranch }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: githubAccount,
                        passwordVariable: 'GIT_PASS',
                        usernameVariable: 'GIT_USER'
                    )
                ]) {
                    script {
                        def changeJobImageTagCmd = migrationChanges ? 
                            "sed -i '/jobImageTag:/s/:.*/: ${IMAGE_VERSION}/' ${helmValueFile}" : ''
                        sh """
                            git clone https://${GIT_USER}:${GIT_PASS}@github.com/MeetingTeam/${k8SRepoName}.git --branch ${mainBranch}
                            cd ${helmPath}
                            sed -i "/imageTag:/s/:.*/: ${IMAGE_VERSION}/" ${helmValueFile}
                            ${changeJobImageTagCmd}

                            git config --global user.email "jenkins@gmail.com"
                            git config --global user.name "Jenkins"
                            git add .
                            git commit -m "feat: update application image of helm chart '${appRepoName}' to version ${IMAGE_VERSION}"
                            git push origin ${mainBranch}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: trivyReportFile, allowEmptyArchive: true, fingerprint: true
        }
    }
}