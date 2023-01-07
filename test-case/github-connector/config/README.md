# GitHub

This demo will fill the topic `demo-file-source-distributed` with the content of the file `demo-file.txt`` thanks to a distributed connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic github-kubernetes --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:9092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Execute in current directory (host machine) `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @connector-config.json | jq`.