# GitHub Source Connector


## Description

This project is based on the following [training](https://github.com/simplesteph/kafka-connect-github-source/tree/v1.1). 

The skeleton has been generated thanks to the Maven archetype:

``````
mvn archetype:generate -DarchetypeGroupId=io.confluent.maven -DarchetypeArtifactId=kafka-connect-quickstart -DarchetypeVersion=0.10.0.0 \
-Dpackage=com.opencredo.examples \
-DgroupId=com.opencredo.examples \
-DartifactId=my-source-connector \
-Dversion=1.0-SNAPSHOT
``````


## Installation

Please run `mvn clean install` before starting the Kafka cluster. If the cluster is already running, please restart
Kafka connect nodes.
