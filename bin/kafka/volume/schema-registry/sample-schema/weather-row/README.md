# Weather Row Schema

This schema will be used by the taxi application in the weather-row stream, see [KSQL commands](../../../../../../doc/ksql/readme.md).
They are automatically registered on the container startup.

Version 1:
````
./execute_on_registry.sh 
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/schema-registry/sample-schema/weather-row/schema-v1.avsc" "http://localhost:8081/subjects/WEATHER_ROW-value/versions"
````