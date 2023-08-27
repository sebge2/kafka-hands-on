# Commands for Topics


## Kafka Console Producer

````
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --property schema.registry.url=http://localhost:8081 --property value.schema='{"type": "record", "name": "myRecord", "fields": [ {"name": "f1", "type": "string" ]]}'
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --property parse.key=true --property key.separator=:
````


## Kafka Console Consumer

````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --from-beginning
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --from-beginning --property schema.registry.url=http://localhost:8081
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --group group1
````