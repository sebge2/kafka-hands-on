# Administration Commands


## Dump Log Segment

Allows you to look at a partition segment in the filesystem and examine its content:
````
kafka-run-class kafka.tools.DumpLogSegments --deep-iteration --files /kafka/kafka-logs/kafka-hands-on-range-assignor-0/00000000000000002762.log
````