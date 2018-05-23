#!/bin/sh

sh /opt/app/gradlew -Dhttp.proxyHost=http://proxyout.reform.hmcts.net-Dhttp.proxyPort=8080 clean build $@
