# Commands for Topics

The following commands can be executed on the first broker:

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
````


## Topic Management

````
kafka-topics --bootstrap-server kafka-broker-1:19092 --list
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic test --partitions 3 --replication-factor 1
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic --describe test
kafka-topics --bootstrap-server kafka-broker-1:19092 --delete --topic test
````


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


## Kafka Console Consumer Group

````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --list
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --describe --group group1
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --to-earliest --execute --topic test
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --shift-by 2 --execute --topic test
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --shift-by -2 --execute --topic test
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --shift-by -2 --execute --all-topics
````

## Topic Configuration

````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name my-topic  --describe
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name my-topic  --alter --add-config min.insync.replicas=2
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name my-topic  --alter --delete-config min.insync.replicas
````