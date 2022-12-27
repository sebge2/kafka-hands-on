# Kafka Hands On


## Startup

### Start Kafka
````
cd bin/kafka
docker-compose up -d
./start.sh
````


### Start OpenSearch

Will be used for a consumer test.

````
cd bin/opensearch
./start.sh
````


## Setup

The examples require to perform the following setup:

````
cd bin/
./start.sh
./connect_cluster.sh
kafka-topics --bootstrap-server localhost:9092 --create --topic kafka-hands-on-range-assignor --partitions 2
kafka-topics --bootstrap-server localhost:9092 --create --topic kafka-hands-on-cooperative-sticky-assigner --partitions 2
kafka-topics --bootstrap-server localhost:9092 --create --topic wikimedia.recentchange --partitions 3
````


## Commands

### Kafka Topics Command

````
kafka-topics --bootstrap-server localhost:9092 --list
kafka-topics --bootstrap-server localhost:9092 --create --topic test --partitions 3 --replication-factor 2
kafka-topics --bootstrap-server localhost:9092 --create --topic --describe test
kafka-topics --bootstrap-server localhost:9092 --delete --topic test
````

### Kafka Console Producer

````
kafka-console-producer --bootstrap-server localhost:9092 --topic test
kafka-console-producer --bootstrap-server localhost:9092 --topic test --property parse.key=true --property key.separator=:
````


### Kafka Console Consumer

````
kafka-console-consumer --bootstrap-server localhost:9092 --topic test
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --prty print.value=true
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --group group1
````


### Kafka Console Consumer Group

````
kafka-consumer-groups --bootstrap-server localhost:9092 --list
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group group1
kafka-consumer-groups --bootstrap-server localhost:9092 --group  group1 --reset-offsets --to-earliest --execute --topic test
kafka-consumer-groups --bootstrap-server localhost:9092 --group  group1 --reset-offsets --shift-by 2 --execute --topic test
kafka-consumer-groups --bootstrap-server localhost:9092 --group  group1 --reset-offsets --shift-by -2 --execute --topic test
kafka-consumer-groups --bootstrap-server localhost:9092 --group  group1 --reset-offsets --shift-by -2 --execute --all-topics
````


## Links

* [Course](https://www.udemy.com/course/apache-kafka)
* [Spring Kafka on Baeldung](https://www.baeldung.com/spring-kafka)
* [Spring Kafka Streams on Baeldung](https://www.baeldung.com/spring-boot-kafka-streams)