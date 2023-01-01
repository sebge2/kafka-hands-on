# Http Source - Distributed Connector

This demo will fill the topic `demo-http-source-distributed` with the content of an HTTP call thanks to a standalone connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-http-source-distributed --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:9092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Go in `http://localhost:8080/ui/clusters/local/connectors`.
- Fill the form with the content of the file `connector-config.json` with connector name `http-stream-demo-distributed`.