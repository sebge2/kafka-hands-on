# File Source - Distributed Connector

This demo will fill Elastic Search from the topic `demo-elasticsearch-source-distributed` thanks to a distributed sink connector.


## Setup Topic

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-topics --create --topic demo-elasticsearch-source-distributed--partitions 3 --replication-factor 1 --bootstrap-server kafka-broker-1:9092`
- Exit container `CTRl + D`


## Launch Distributed Connector

- Go in `http://localhost:8080/ui/clusters/local/connectors`.
- Fill the form with the content of the file `connector-config.json` with connector name `elasticsearch-stream-demo-distributed`.


## Emit Messages

- Execute `cd ../../..`
- Execute `./execute_on_first_broker.sh`
- Execute `kafka-console-producer --bootstrap-server localhost:9092 --topic demo-elasticsearch-source-distributed`
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