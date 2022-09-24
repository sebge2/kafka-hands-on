# JDBC Sink - Distributed Connector

This demo will fill Postgresql from the topic `demo-jdbc-sink-distributed` thanks to a distributed sink connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-jdbc-sink-distributed --partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:19092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @connector-config.json | jq`.


## Emit Messages

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-console-producer --bootstrap-server localhost:19092 --topic demo-jdbc-sink-distributed`
- Fill the message with the payload:
````
{
	"schema": {
		"type": "struct",
		"fields": [
			{
				"type": "string",
				"optional": false,
				"field": "EQUIP"
			},
			{
				"type": "int64",
				"optional": true,
				"field": "NUM_EQUIP"
			},
			{
				"type": "string",
				"optional": false,
				"field": "UNID"
			}
		],
		"optional": false,
		"name": "ACT_EQUIP"
	},
	"payload": {
		"EQUIP": "7KTUA28278",
		"NUM_EQUIP": 1,
		"UNID": "WG33609"
	}
}
````


# Links

[Documentation](https://docs.confluent.io/kafka-connectors/jdbc/current/sink-connector/overview.html)