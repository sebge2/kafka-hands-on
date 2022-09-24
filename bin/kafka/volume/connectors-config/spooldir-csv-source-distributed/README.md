# CSV Source - Distributed Connector

This demo will fill Postgresql from the topic `spooldir-csv-source-distributed` thanks to a distributed sink connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic spooldir-csv-source-distributed --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:19092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @connector-config.json | jq`.