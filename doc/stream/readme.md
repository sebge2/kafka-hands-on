# Streams


````
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic word-count-input --partitions 1 --replication-factor 1
````

kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic word-count-input

kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic stream-example-flatmap --from-beginning --property print.key=true --property print.value=true


## Links

- [Stream course resources](https://github.com/simplesteph/kafka-streams-course/)
- [Kafka Streams VS KSQL](https://www.confluent.io/blog/kafka-streams-vs-ksqldb-compared/)
- [Stream join](https://www.confluent.io/blog/crossing-streams-joins-apache-kafka/)
- [Stream testing](https://kafka.apache.org/documentation/streams/developer-guide/testing.html)