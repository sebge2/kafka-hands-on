# Kafka Hands On


## Startup

Kafka and other services will be executed on Docker. You may increase the maximum memory size allocated for Docker allowing to run them all.


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


### Start Postgresql

Will be used for a consumer/producer test.

````
cd bin/postgresql
./start.sh
````


## Commands

[Go here](./topics-command.md)


## Avro

[Go here](doc/avro/readme.md)


## REST Proxy

[Go here](doc/rest-proxy/readme.md)


## Stream

[Go here](doc/stream/readme.md)


## KSQL

[Go here](doc/ksql/readme.md)


## Links

* [Course](https://www.udemy.com/course/apache-kafka)
* [Spring Kafka on Baeldung](https://www.baeldung.com/spring-kafka)
* [Spring Kafka Streams on Baeldung](https://www.baeldung.com/spring-boot-kafka-streams)
* [Compaction](https://docs.confluent.io/kafka/design/log_compaction.html#configure-compaction)