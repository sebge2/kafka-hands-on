# see https://github.com/coderfi/docker-avro-tools/blob/master/Dockerfile
FROM openjdk:8-jdk-alpine

ENV AVRO_VERSION 1.11.1
ENV AVRO_TOOLS_JAR /usr/share/java/avro-tools-${AVRO_VERSION}.jar

RUN mkdir -p /usr/share/java \
 && cd /usr/share/java \
 && wget https://repo1.maven.org/maven2/org/apache/avro/avro-tools/${AVRO_VERSION}/avro-tools-${AVRO_VERSION}.jar


ADD avro-tools /usr/local/bin/avro-tools
RUN chmod 0700 /usr/local/bin/avro-tools


CMD tail -f /dev/null