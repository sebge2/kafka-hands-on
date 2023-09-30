# Kafka Hands On

I created this project for saving code written in the scope of the following [course](https://www.udemy.com/course/apache-kafka).
Afterward, I added documentation helping me to pass the Kafka certification.


## Startup

Kafka and other services will be executed on Docker. You may increase the maximum memory size allocated for Docker allowing to run them all.


### Start Kafka

Different stacks are available:
* Kafka with Zookeeper
* Kafka with Kraft
* Kafka with Kraft with an advanced configuration (SSL, monitoring, ...)

````
cd bin/kafka
cd zookeeper / cd simple-kraft
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


## Documentation

You can find documentation in the following sections.

- [Kafka API](doc/kafka/kafka.md)
- [Administration Commands](doc/kafka/admin-command.md)
- [Avro](doc/avro/readme.md)
- [REST Proxy](doc/rest-proxy/readme.md)
- [Stream](doc/stream/readme.md)
- [KSQL](doc/ksql/readme.md)


## Links

* [Course](https://www.udemy.com/course/apache-kafka)
* [Spring Kafka on Baeldung](https://www.baeldung.com/spring-kafka)
* [Spring Kafka Streams on Baeldung](https://www.baeldung.com/spring-boot-kafka-streams)
* [Compaction](https://docs.confluent.io/kafka/design/log_compaction.html#configure-compaction)