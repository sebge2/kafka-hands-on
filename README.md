# Kafka Hands On


## Startup

### Start Kafka
````
cd bin/kafka
docker-compose up -d
./start.sh
````


### Start OpenSearch

Will be used for a consumer test. You may increase the maximum memory size allocated for Docker.

````
cd bin/opensearch
./start.sh
````


### Start OpenSearch

Will be used for a consumer/producer test. You may increase the maximum memory size allocated for Docker.

````
cd bin/opensearch
./start.sh
````


### Start Postgresql

Will be used for a consumer/producer test. You may increase the maximum memory size allocated for Docker.

````
cd bin/postgresql
./start.sh
````


## Commands

The following commands can be executed on the broker (first, or second broker):

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
````


### Kafka Topics Command

````
kafka-topics --bootstrap-server localhost:9092 --list
kafka-topics --bootstrap-server localhost:9092 --create --topic test --partitions 3 --replication-factor 1
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


### KSQL - Print Command

In the first terminal:

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
kafka-topics --bootstrap-server localhost:9092 --create --topic users --partitions 3 --replication-factor 1

# After start printing users in the second terminal:
kafka-console-producer --bootstrap-server localhost:9092 --topic users
````

In the second terminal:

````
./execute_on_ksql.sh
ksql

# In KSQL command line:
print 'users';

# Start the producer in the first terminal
````


## Links

* [Course](https://www.udemy.com/course/apache-kafka)
* [Spring Kafka on Baeldung](https://www.baeldung.com/spring-kafka)
* [Spring Kafka Streams on Baeldung](https://www.baeldung.com/spring-boot-kafka-streams)