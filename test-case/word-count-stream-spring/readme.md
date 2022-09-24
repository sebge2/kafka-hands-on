# Word Count Stream

The following commands can be executed on the first broker:
````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
````

Create topics:
````
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic word-count-input --partitions 1 --replication-factor 1
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic word-count-spring-output --partitions 1 --replication-factor 1
````

Display values:
````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic word-count-spring-output --from-beginning -property print.key=true  --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
````