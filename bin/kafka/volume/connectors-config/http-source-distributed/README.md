# Http Source - Distributed Connector

This demo will fill the topic `demo-http-source-distributed` with the content of an HTTP call thanks to a standalone connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-http-source-distributed --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:19092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @connector-config.json | jq`.