# User Compliant Schema

This schema will be used by the taxi application in the user complaint stream, see [KSQL commands](../../../../../../doc/ksql/readme.md).

Version 1:
````
./execute_on_registry.sh 
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/schema-registry/sample-schema/user-complaint/schema-v1.avro" "http://localhost:8081/subjects/user_complaint-value/versions"
````

Version 2:
````
./execute_on_registry.sh 
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/schema-registry/sample-schemauser-complaint/schema-v2.avro" "http://localhost:8081/subjects/user_complaint-value/versions"
````