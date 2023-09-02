# Administration Commands

The following commands can be executed on the first broker:

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
````

or second broker:
````
cd bin/kafka
./start.sh
./execute_on_second_broker.sh
````


## Topics


### Create a Topic

There are 3 mandatory arguments: `--topic`, `--replication-factor`and `--partitions`.

````
kafka-topics --bootstrap-server kafka-broker-1:19092 --create --topic test --partitions 3 --replication-factor 1
````

Useful arguments: 
* `--if-exists`
* `--if-not-exists`


### List topics

````
kafka-topics --bootstrap-server kafka-broker-1:19092 --list
````

Useful argument: `--exclude-internal`.


### Describe a Topic

````
 kafka-topics --bootstrap-server kafka-broker-1:19092 --describe --topic test
````


### Describe Topics

````
 kafka-topics --bootstrap-server kafka-broker-1:19092 --describe
````

Useful arguments:
* `--topics-with-overrides`
* `--exclude-internal`
* `--under-replicated-partitions`
* `--at-min-isr-partitions`
* `--under-min-isr-partitions`
* `--unavailable-partitions`


### Adding Partitions

````
kafka-topics --bootstrap-server kafka-broker-1:19092 --alter --topic test --partitions 5
````


### Deleting a Topic

Be careful, the option `delete.topic.enable` must be `true`.

````
 kafka-topics --bootstrap-server kafka-broker-1:19092 --delete --topic test
````


## Consumer Groups


### List Groups

````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --list
````


### Describe a Group

````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --describe --group connect-postgresql-stream-demo-distributed
````


### Delete a Group

Be careful, the group must be empty (no active members).
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --delete --group kafka-hands-on-range-assignor-group-2
````


### Export Offsets

Offsets will be exported to a CSV file:
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --export --group my-group  --topic test --reset-offsets --to-current --dry-run > offsets.csv
````


### Reset Offsets

Based on a CSV file (see how to export offsets):
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --reset-offsets --group my-group --from-file offsets.csv --execute
````

Reset to the earliest offset for partition 0:
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --to-earliest --execute --topic test:0
````

Reset to the latest offset for partition 0:
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --to-latest --execute --topic test:0
````

Reset relative to the current offset (the shift can be a positive or negative number) for partition 0:
````
kafka-consumer-groups --bootstrap-server kafka-broker-1:19092 --group  group1 --reset-offsets --shift-by -2 --execute --topic test:0
````

It can be tested with:

````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --group my-group
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test
````


## Dynamic Configuration

General pattern for adding a configuration:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --alter --entity-type [TYPE] --entity-name [NAME] --add-config [KEY]=[VALUE]
``````

General pattern for deleting a configuration:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --alter --entity-type [TYPE] --entity-name [NAME] --delete-config [KEY]
``````

General pattern for checking a configuration:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --alter --entity-type [TYPE] --entity-name [NAME] --describe
``````


### Topic Dynamic Configuration

Example:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name test --alter --add-config min.insync.replicas=2
``````

You can check with:
````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name test --describe
````

or:
````
kafka-topics --bootstrap-server kafka-broker-1:19092 --describe --topics-with-overrides
````

You can delete a specific configuration:
````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type topics --entity-name test --alter --delete-config min.insync.replicas
````


### Users and Clients Dynamic Configuration

Example:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --alter --add-config "controller_mutation_rate=10" --entity-type clients --entity-name my-client --entity-type users --entity-name my-user
``````


## Broker Dynamic Configuration

Example:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type brokers --entity-name 1 --alter --add-config unclean.leader.election.enable=true
``````

You can check with:
``````
kafka-configs --bootstrap-server kafka-broker-1:19092 --entity-type brokers --entity-name 1 --describe
``````


## Console Producer

````
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test
````

It can be customized either by: `--producer.config=[FILE]`, or by `--producer-property [KEY]=[VALUE]`.

Example of producer properties:
* `acks=[all, -1, 0, 1]`

Useful options:
* `batch-size=[SIZE]`
* `timeout=[TIMEOUT]`
* `compression-codec=[CODEC]`
* `sync`
* ...

Line reader properties can be customized by `--property`:
* `ignore.error=[BOOLEAN]`
* `parse-key=[BOOLEAN]`
* `key-separator=[SEPARATOR]`

Example:
``````
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --property parse.key=true --property key.separator=:
``````

With a AVRO schema:
``````
kafka-console-producer --bootstrap-server kafka-broker-1:19092 --topic test --property schema.registry.url=http://localhost:8081 --property value.schema='{"type": "record", "name": "myRecord", "fields": [ {"name": "f1", "type": "string" ]]}'
``````


## Console Consumer

By default, the consumer group has the syntax: `console-consumer-[ID]`. It can be customized with the option `--group`.

Single topic to listen to with `--topic`:
````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test
````

Multiple topics to listen to with `--whitelist`:
````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --whitelist '.*'
````

It can be customized either by: `--consumer.config=[FILE]` or by `--consumer-property [KEY]=[VALUE]`.

Example of consumer properties:
* `exclude.internal.topics`

Useful options:
* `--formatter=[CLASS NAME]`
* `--from-beginning`
* `--max-messages [NUMBER]`
* `--partition [NUMBER]`
* `--offset [earliest|latest|NUMBER]`
* `--skip-message-on-error`
* `--group [GROUP ID]`

The default formatter can be customized by `--property`:
* `print.timestamp=[BOOLEAN]`
* `print.key=[BOOLEAN]`
* `print.offset=[BOOLEAN]`
* `print.partition=[BOOLEAN]`

* `key.separator=[SEPARATOR]`
* `line.separator=[SEPARATOR]`

* `key.deserializer=[CLASS NAME]`
* `value.deserializer=[CLASS NAME]`

Example to get committed offsets:
`````
kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic __consumer_offsets --from-beginning --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" --consumer-property exclude.internal.topics=false
`````


## Partition Management

### Start Automatic Replica Election

If you want to start manually an election on all the topics where the election type is `PREFERRED`, or `UNCLEAN`:
````
kafka-leader-election --bootstrap-server kafka-broker-1:19092 --election-type PREFERRED --all-topic-partitions
````

With a specific partition and topic:
````
kafka-leader-election --bootstrap-server kafka-broker-1:19092 --election-type PREFERRED --topic test --partition 0 
````

You can also pass a list of several partitions:
````
kafka-leader-election --bootstrap-server kafka-broker-1:19092 --election-type PREFERRED --path-to-json-file partitions.json
````

with body:
````
{ "partitions":
[
  { "topic": "test", "partition": 0 }
]
}
````


### Start Manual Replica Assignment

First of all, generate 2 files containing the current partitions assignment (ex: `current_assignment.json`) for the specified broker and the proposed assignment (ex: `new_assignment.json`):
````
kafka-reassign-partitions --bootstrap-server kafka-broker-1:19092 --topics-to-move-json-file ./topics.json --broker-list 1,2 --generate
````

With `topics.json` file:
````
{
    "topics": [
        {
            "topic": "test"
        }
    ]
}
````

To execute the new assignment:
````
kafka-reassign-partitions --bootstrap-server kafka-broker-1:19092 --reassignment-json-file ./new_assignment.json --execute
````

To verify the status of the reassignment:
````
kafka-reassign-partitions --bootstrap-server kafka-broker-1:19092 --reassignment-json-file ./new_assignment.json --verify
````


## Dump Log Segment

Allows you to look at a partition segment in the filesystem and examine its content:
````
kafka-dump-log --files /kafka/kafka-logs/test-0/00000000000000000000.log 
````

* Information about the payload: `--print-data-log`
* Check that the index is in a usable state: `--index-sanity-check` 
* Only check indexes without printing out all the indexes: `--verify-index-only`



## Replication Verification

In order to check that every message is properly replicated on all the replicas of the specified topics:
````
kafka-replica-verification --broker-list kafka-broker-1:19092,kafka-broker-2:29092 --topic-white-list '.*' 
````


## Check API versions

The following tool helps you to perform upgrades of features:
````
kafka-broker-api-versions --bootstrap-server kafka-broker-1:19092
````


## Client ACLs:

Management of client Access Control Lists:
````
kafka-acls --bootstrap-server kafka-broker-1:19092
````