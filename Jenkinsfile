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
                                                      string(description: 'CCD Data Store Api url', defaultValue: 'http://ccd-data-store-api-saat.service.core-compute-saat.internal/', name: 'CCD_DATA_STORE_API_URL'),
                                                      string(description: 'Service auth url', defaultValue: 'http://rpe-service-auth-provider-saat.service.core-compute-saat.internal', name: 'SERVICE_AUTH_PROVIDER_BASE_URL'),
                                                      string(description: 'Idam user auth url', defaultValue: 'http://idam-api-idam-saat.service.core-compute-idam-saat.internal', name: 'USER_AUTH_PROVIDER_OAUTH2_URL'),
                                                      string(description: 'Idam redirect url', defaultValue: 'https://www-test.probate.reform.hmcts.net/oauth2/callback', name: 'IDAM_OAUTH2_REDIRECT_URI'),
                                                       string(description: 'env', defaultValue: 'saat', name: 'ENV'),
                                                       string(description: 'Idam.secret', defaultValue: 'abc', name: 'IDAM_SECRET'),
                                                       string(description: 's2s.auth.totp.secret', defaultValue: 'abc', name: 'S2S_AUTH_TOTP_SECRET')

                                              ])


        ]
)

//@Library(['Reform', 'PROBATE'])
def ansible = new Ansible(this, 'probate')
def packager = new Packager(this, 'probate')
def versioner = new Versioner(this)

def rpmTagger
def app = "pa-ccd-integration-tests"
def artifactorySourceRepo = "probate-local"

node {
    try {
        def version
        stage('Checkout') {
            deleteDir()
            checkout scm
        }

        stage('Test') {
          env.CCD_DATA_STORE_API_URL = params.CCD_DATA_STORE_API_URL
          env.IDAM_OAUTH2_REDIRECT_URI =params.IDAM_OAUTH2_REDIRECT_URI
          env.ENV=params.ENV
          env.SECRET=params.SECRET
            sh "./gradlew clean build"
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
                reportName           : "PA CCD Integration Tests Report",
                alwaysLinkToLastBuild: true
        ]
    }
}
