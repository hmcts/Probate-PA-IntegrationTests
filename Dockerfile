FROM openjdk:8-alpine

RUN mkdir -p /opt/app/

WORKDIR /opt/app

COPY docker/entrypoint.sh /
COPY src /opt/app/
COPY build.gradle /opt/app/
COPY gradle /opt/app/gradle
COPY gradlew /opt/app/

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy= curl --silent --fail http://localhost:4104/health

EXPOSE 4104

ENTRYPOINT [ "/entrypoint.sh" ]
