# File Source - Standalone Connector

This demo will fill the topic `demo-http-source-standalone` with the content of an HTTP call thanks to a standalone connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-http-source-standalone --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:19092`
- Exit container `CTRl + D`


## Setup Connector

- Execute `cd ../../..`
- Execute `./execute_on_first_kafka_conect.sh`
- Execute `confluent-hub install --no-prompt cjmatta/kafka-connect-sse:1.0`
- Exit container `CTRl + D`


## Launch Standalone Connector

- Execute `cd ../../..`
- Execute `./execute_on_first_kafka_conect.sh`
- Execute `connect-standalone /demo/http-source-standalone/worker.properties /demo/http-source-standalone/http-stream-demo-standalone.properties`
- Exit container `CTRl + D`
