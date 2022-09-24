# Avro Schema


## Schema Evolution

The evolution of a schema from a version 1 to an updated version 2 has some characteristics.

[Confluent Documentation](https://docs.confluent.io/platform/current/schema-registry/avro.html#backward-compatibility)
[Specification](https://avro.apache.org/docs/1.11.1/specification/_print/)


### Backward Compatible Change

**Data written in V1 can be read in V2**. New schema can read old data. There is no impact on producers (less common).

Schema V1: 
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" },
    { "name": "adAllowed", "type": "boolean", "doc": "" }
  ]
}
````

Schema V2: 
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" }
  ]
}
````

The field `adAllowed` has been removed. Data written in V1 that have the `adAllowed`flag, can be read in V2, they will be ignored.

Changes allowed:
* Delete fields
* Add optional fields


### Forward Compatible Change

**Data written in V2 can be read in V1**. Old schema an read new data. There is no impact on consumers (most common).

Changes allowed:
* Add fields
* Delete optional fields

Schema V1:
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" }
  ]
}
````

Schema V2:
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" },
    { "name": "phoneNumber", "type": "string", "doc": "" }
  ]
}
````

The nullable `phoneNumber` field has been added. It's a forward compatible change. The added field will be ignored.


### Fully Compatible Change

A fully compatible change is backward and forward compatible.

Schema V1:
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" }
  ]
}
````

Schema V2:
````
{
  "type": "record",
  "namespace": "be.sgerard.kafkahandson",
  "name": "Customer",
  "fields": [
    { "name": "firstName", "type": "string", "doc": "" },
    { "name": "lastName", "type": "string", "doc": "" },
    { "name": "phoneNumber", "type": ["null", "string"], "default": null, "doc": "" }
  ]
}
````

Changes allowed:
* Add optional fields
* Delete optional fields


### Not compatible Change

- Add/remove values from enum
- Changing type of field
- Rename a required field


### Tips

Prefer fully compatible changes:
- Make the primary key required
- Use a default value for fields that can be removed
- Be careful with enums, they can’t change
- Don’t rename fields, use alias
- Don’t remove required fields
- Use default values as much as possible


## Console Producer


## Register Schema 

````
./execute_on_registry.sh 
kafka-avro-console-producer --bootstrap-server kafka-broker-1:19092 --topic test-avro --property schema.registry.url=http://schema-registry:8081 --property value.schema='{"type": "record", "name": "myRecord", "fields": [ {"name": "f1", "type": "string"}]}'
````

Once started let's try the following records:
````
# Successful
{"f1": "value1"}

# Error, wrong field name. Restart the producer
{"f2": "value1"}

# Error, wrong type. Restart the producer
{"f1": 1}
````


## Incompatible Schema

Let's break the previously created schema, ``f1`` is no more a String but an int

````
./execute_on_registry.sh 
kafka-avro-console-producer --bootstrap-server kafka-broker-1:19092 --topic test-avro --property schema.registry.url=http://schema-registry:8081 --property value.schema='{"type": "record", "name": "myRecord", "fields": [ {"name": "f1", "type": "int"}]}'
````

Now let's publish a message that should fail because we registered an incompatible schema:
````
{"f1": 1}
````


## Console Consumer

````
./execute_on_registry.sh 
kafka-avro-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test-avro --property schema.registry.url=http://schema-registry:8081 --from-beginning
````