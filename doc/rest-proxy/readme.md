# REST Proxy

The following commands must be executed in this folder.


## List Topics

````
curl http://localhost:8082/topics | jq
````


## Get Topic Info

````
curl http://localhost:8082/topics/kafka-connect-status | jq
````


## Produce Record

Binary: 
````
curl -X POST -d @./binary/send-records-binary.json -H "Content-Type: application/vnd.kafka.binary.v2+json" -H "Accept: application/vnd.kafka.v2+json, application/vnd.kafka+json, application/json" http://localhost:8082/topics/test-rest-proxy-binary | jq
````

JSON:
````
curl -X POST -d @./json/send-records-json.json -H "Content-Type: application/vnd.kafka.json.v2+json" -H "Accept: application/vnd.kafka.v2+json, application/vnd.kafka+json, application/json" http://localhost:8082/topics/test-rest-proxy-json | jq
````

Avro:
````
curl -X POST -d @./avro/send-records-avro.json -H "Content-Type: application/vnd.kafka.avro.v2+json" -H "Accept: application/vnd.kafka.v2+json, application/vnd.kafka+json, application/json" http://localhost:8082/topics/test-rest-proxy-avro | jq
````


## Consume Record

Binary:
`````
# Create consumer
curl -X POST -d @./binary/create-consumer-binary.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-binary-consumer | jq

# Create a subscription
curl -X POST -d @./binary/create-subscription-binary.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-binary-consumer/instances/my-binary-consumer/subscription | jq

# Fetch records
curl -X GET -H "Accept: application/vnd.kafka.binary.v2+json" 'http://localhost:8082/consumers/my-binary-consumer/instances/my-binary-consumer/records?timeout=10000' | jq
`````

JSON:
`````
# Create consumer
curl -X POST -d @./json/create-consumer-json.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-json-consumer | jq

# Create a subscription
curl -X POST -d @./json/create-subscription-json.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-json-consumer/instances/my-json-consumer/subscription | jq

# Fetch records
curl -X GET -H "Accept: application/vnd.kafka.json.v2+json" 'http://localhost:8082/consumers/my-json-consumer/instances/my-json-consumer/records?timeout=10000' | jq
`````

Avro:
`````
# Create consumer
curl -X POST -d @./avro/create-consumer-avro.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-avro-consumer | jq

# Create a subscription
curl -X POST -d @./avro/create-subscription-avro.json -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-avro-consumer/instances/my-avro-consumer/subscription | jq

# Fetch records
curl -X GET -H "Accept: application/vnd.kafka.avro.v2+json" 'http://localhost:8082/consumers/my-avro-consumer/instances/my-avro-consumer/records?timeout=10000' | jq
`````


## Delete Consumer

Binary consumer:
````
curl -X DELETE  -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-json-consumer/instances/my-json-consumer | jq
````

JSON consumer:
````
curl -X DELETE  -H "Content-Type: application/vnd.kafka.v2+json" http://localhost:8082/consumers/my-binary-consumer/instances/my-binary-consumer | jq
````


## Links

- [API](https://docs.confluent.io/platform/current/kafka-rest/api.html).