# Kafka

## Producer

- We create a `ProducerRecord` including a topic, an optional key and a value.
- We initialize a `KafkaProducer`. 3 properties are mandatory:
  - `bootstrap.servers`: list of `host:port`brokers (not necessary to list them all).
  - `key.serializer`: how to serialize keys into byte arrays (necessary even if there is no key, `VoidSerializer`).
  - `value.serializer`: how to serialize values into byte arrays.
- We provide the record to the `KafkaProducer#send()` method (supports multi-threading access).
- The `KafkaProducer`:
  - invokes the interceptor `ProducerInterceptor#onSend(ProducerRecord)` that can update the entire record
  - retrieves information about partitions related to the specified topic
  - serializes the key using the `key.serializer` into byte arrays
  - serializes the value using the `value.serializer` into byte arrays
  - if we didn't specify a partition, invokes the `Partitionner` that returns the partition number, otherwise it uses the specified partition 
  - adds the record to a batch of records that will be sent
  - uses a separate thread to send batches to the right broker
  - returns a `Future` object.
  
Once the `Future`is returned to the caller. There are 3 ways of interacting with it:
  - _Fire-and-forget send_: we don't care if the message arrived successfully, we don't on anything with this `Future`.
  - _Synchronous send_: we invoke `Future#get()` that will wait until we know if the message arrived or not.
  - _Asynchronous send_: we provide a callback function (reactive way) to the `Future`.


TODO
- The broker sends back a response `RecordMetadata` (containing the topic, partition and offset), or an error.