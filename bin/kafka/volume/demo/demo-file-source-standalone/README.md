# File Source Demo

This demo will fill the topic `demo-file-source-standalone` with the content of the file `demo-file.txt``


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-file-source-standalone --partitions 3 --replication-factor 1 --bootstrap-server 127.0.0.1:9092`
- Exit container `CTRl + D`


## Launch Standalone Connector

- Execute `cd ../../..`
- Execute `./execute_on_first_kafka_conect.sh`
- Execute `connect-standalone /demo/demo-file-source-standalone/worker.properties /demo/demo-file-source-standalone/file-stream-demo-standalone.properties`
- Exit container `CTRl + D`