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


## Dump Log Segment

Allows you to look at a partition segment in the filesystem and examine its content:
````
kafka-run-class kafka.tools.DumpLogSegments --deep-iteration --files /kafka/kafka-logs/kafka-hands-on-range-assignor-0/00000000000000002762.log
````