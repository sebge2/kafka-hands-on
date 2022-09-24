# User Compliant Schema

This schema will be used by the taxi application in the user complaint stream, see [KSQL commands](../../../../../doc/ksql/readme.md).

Version 1:
````
./execute_on_registry.sh 
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/my-schema/user-complaint/schema-v1.avro" "http://localhost:8081/subjects/user_complaint-value/versions"
````

Version 2:
````
./execute_on_registry.sh 
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/my-schema/user-complaint/schema-v2.avro" "http://localhost:8081/subjects/user_complaint-value/versions"
````