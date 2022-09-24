# GitHub Source - Standalone Connector

This demo will fill the topic `github-kubernetes` with GitHub issues thanks to a standalone connector. This connector must be
installed before running the Kafka cluster. The connector is part of this [project](../../../../../test-case/github-connector/README.md).


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic github-kubernetes --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:19092`
- Exit container `CTRl + D`


## Launch Standalone Connector

- Execute `cd ../../..`
- Execute `./execute_on_first_kafka_conect.sh`
- Execute `connect-standalone /demo/github-source-standalone/worker.properties /demo/github-source-standalone/github-standalone.properties`
- Exit container `CTRl + D`