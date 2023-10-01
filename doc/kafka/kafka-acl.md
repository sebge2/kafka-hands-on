# Kafka ACL

## Restrict Read/Write on Topic

User `sample-client-1` is authorized to consume, `sample-client-2` is authorized to produce:
````
kafka-acls --bootstrap-server kafka-broker-1:19092 --add --topic test --operation Read --allow-principal User:CN=sample-client-1,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties
kafka-acls --bootstrap-server kafka-broker-1:19092 --add --topic test --operation Write --allow-principal User:CN=sample-client-2,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties
````

Now, you can test, the client is authorized:
````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --consumer.config /kafka/shared-ssl/sample-client-1/ssl-debug-sample-client-1.properties
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --producer.config /kafka/shared-ssl/sample-client-2/ssl-debug-sample-client-2.properties
````

The other client is not authorized:
````
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --producer.config /kafka/shared-ssl/sample-client-1/ssl-debug-sample-client-1.properties
````