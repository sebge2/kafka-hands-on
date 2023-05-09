# Kafka

## Producer


### Prepare Sending

- We create a `ProducerRecord` including a topic, an optional key and a value.
- We initialize a `KafkaProducer`. 
  - 3 properties are mandatory:
    - `bootstrap.servers`: list of `host:port`brokers (not necessary to list them all).
    - `key.serializer`: how to serialize keys into byte arrays (necessary even if there is no key, `VoidSerializer`).
    - `value.serializer`: how to serialize values into byte arrays.
  - Optional properties: 
    - `client.id` a logical identifier for the client used for logging, metrics and quotas
- We provide the record to the `KafkaProducer#send()` method (supports multi-threading access).
- The `KafkaProducer`:
  - checks that the buffer is not full, otherwise it waits
  - invokes the interceptor `ProducerInterceptor#onSend(ProducerRecord)` that can update the entire record
  - retrieves information about partitions related to the specified topic
  - serializes the key using the `key.serializer` into byte arrays
  - serializes the value using the `value.serializer` into byte arrays
  - if we didn't specify a partition, invokes the `Partitionner` that returns the partition number, otherwise it uses the specified partition 
  - adds the record to a batch of records that will be sent
  - uses a separate thread to send batches to the right broker
  - returns a `Future` object if it's performed before _max.block.ms_ otherwise a timeout exception is thrown

The `acks` parameter specified by producers, controls how many partition replicas must receive the record and answer to the producer to consider the write successful.
This parameter influences the _producer latency_, not the _end-to-end latency_.
- `acks=0`: the producer won’t even wait for a response from the broker. It immediately considers the write successful the moment the record is sent out. This achieves very high throughput.
- `acks=1`: the producer will consider the write successful when the leader receives the record (default).
- `acks=all=-1`: the producer will consider the write successful when all of the in-sync replicas receive the record. This is achieved by the leader broker that will send back a response once all the in-sync replicas receive the record themselves. This increases latency. This is used in combination with `min.insync.replicas` that specifies the minimum number of in-sync brokers (including the leader). If the number of in-sync broker is no reached, an error is triggered.


### Sending

TODO

Once the `Future`is returned to the caller. There are 3 ways of interacting with it:
  - _Fire-and-forget send_: we don't care if the message arrived successfully, we don't on anything with this `Future`.
  - _Synchronous send_: we invoke `Future#get()` that will wait until we know if the message arrived or not.
  - _Asynchronous send_: we provide a callback function (reactive way) to the `Future`.






TODO
- The broker sends back a response `RecordMetadata` (containing the topic, partition and offset), or an error.

  [Sequence Diagram of Delivery time breakdown](https://cwiki.apache.org/confluence/plugins/gliffy/viewer.action?inline=false&pageId=66851583&name=newtimeout&version=6&lastPage=%2Fpages%2Fviewpage.action%3FpageId%3D66851583&imageUrl=%2Fconfluence%2Fdownload%2Fattachments%2F66851583%2Fnewtimeout.png%3Fversion%3D6%26modificationDate%3D1589222868000%26api%3Dv2&gonUrl=%2Fconfluence%2Fdownload%2Fattachments%2F66851583%2Fnewtimeout%3Fapi%3Dv2%26version%3D6)





Kafka will not allow consumers to read records until they are written to all in sync replicas.


## Dictionary

- `End-to-end latency`: measured from the time a record was produced until it's available for consumers
- 

## Links

- [Acks explanation](https://betterprogramming.pub/kafka-acks-explained-c0515b3b707e)