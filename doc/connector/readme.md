# Kafka Connectors


## Commands

Cluster connect information:

````
curl -s localhost:18083 | jq
````

Available connectors :

````
curl -s localhost:18083/connector-plugins | jq
````

Running connectors :

````
curl -s localhost:18083/connectors | jq
````

Connector info:

````
curl -s localhost:18083/connectors/[id] | jq
````

Connectors tasks:

````
curl -s localhost:18083/connectors/[id]/tasks | jq
````

Connector status:

````
curl -s localhost:18083/connectors/[id]/status | jq
````

Connector pause/resume:

````
curl -s -X PUT localhost:18083/connectors/[id]/pause | jq
curl -s -X PUT localhost:18083/connectors/[id]/resume | jq
````

Delete a connector:

````
curl -s -X DELETE localhost:18083/connectors/[id] | jq
````
