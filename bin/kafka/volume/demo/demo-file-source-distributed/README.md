# File Source - Distributed Connector

This demo will fill the topic `demo-file-source-distributed` with the content of the file `demo-file.txt`` thanks to a distributed connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-file-source-distributed --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:9092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Go in `http://localhost:8080/ui/clusters/local/connectors`.
- Fill the form with the content of the file `connector-config.json` with connector name `file-stream-demo-distributed`.