# Favorite Color Application

Create input topic:
````
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic favorite-user-color --partitions 1 --replication-factor 1
````

Listen for results:
````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic favorite-user-color-stats --from-beginning --property print.key=true --property print.value=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
````
