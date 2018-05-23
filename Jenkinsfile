#!groovy
@Library('Reform')
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner

def triggers = []
if (env.BRANCH_NAME == "master") {
    triggers << cron('H H(0-4) * * *') //build to trigger sometime between midnight and 2am every day
}

properties(
        [
                [$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/hmcts/Probate-PA-IntegrationTests.git'],
                pipelineTriggers(triggers),
                parameters([
                        string(description: 'CCD Data Store Api url', defaultValue: 'https://ccd-data-store-api-saat.service.core-compute-saat.internal/', name: 'CCD_DATA_STORE_API_URL'),
                        string(description: 'Service auth url', defaultValue: 'http://betadevbccidams2slb.reform.hmcts.net', name: 'SERVICE_AUTH_PROVIDER_BASE_URL'),
                        string(description: 'Idam user auth url', defaultValue: 'http://betaDevbccidamAppLB.reform.hmcts.net', name: 'USER_AUTH_PROVIDER_OAUTH2_URL'),
                        string(description: 'Evidence Management url', defaultValue: 'http://dm-store-saat.service.core-compute-saat.internal', name: 'EVIDENCE_MANAGEMENT_URL'),
                        string(description: 'Service auth service name', defaultValue: 'PROBATE_BACKEND', name: 'AUTHORISED_SERVICES'),
                        string(description: 'Idam user id', defaultValue: '22603', name: 'IDAM_USER_ID')
                ])
        ]
)

//@Library(['Reform', 'PROBATE'])
def ansible = new Ansible(this, 'probate')
def packager = new Packager(this, 'probate')
def versioner = new Versioner(this)

def rpmTagger
def app = "sol-ccd-services-integration-tests"
def artifactorySourceRepo = "probate-local"

node {
    try {
        def version
        stage('Checkout') {
            deleteDir()
            checkout scm
        }

        stage('Test') {
            sh "./gradlew clean build"
        }

        stage('Package (Docker)') {
            if ("master" == "${env.BRANCH_NAME}") {
                dockerImage imageName: 'probate/sol-ccd-services-integration-tests', tags: ['master']
            } else if ("master" == "${env.BRANCH_NAME}") {
                dockerImage imageName: 'probate/sol-ccd-services-integration-tests'
            }
        }

    } catch (err) {
        currentBuild.result = 'UNSTABLE'
        echo "RESULT: ${currentBuild.result}"

        slackSend(
                channel: '#probate-jenkins',
                color: 'danger',
                message: "${env.JOB_NAME}:  <${env.BUILD_URL}console|Build ${env.BUILD_DISPLAY_NAME}> has FAILED probate Sol CCD service")

        throw err
    } finally {
        publishHTML target: [
                reportDir            : "${env.WORKSPACE}/target/site/serenity/",
                reportFiles          : "index.html",
                reportName           : "Sol CCD Integration Tests Report For Dev",
                alwaysLinkToLastBuild: true
        ]
    }
}
