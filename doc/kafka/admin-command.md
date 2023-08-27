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


## Dump Log Segment

Allows you to look at a partition segment in the filesystem and examine its content:
````
kafka-run-class kafka.tools.DumpLogSegments --deep-iteration --files /kafka/kafka-logs/kafka-hands-on-range-assignor-0/00000000000000002762.log
````