#!/bin/sh

register_schema() {
  SCHEMA_FILE=$1
  SCHEMA_NAME=$2

  curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "@/schema-registry/sample-schema/$SCHEMA_FILE" "http://localhost:8081/subjects/$SCHEMA_NAME/versions"
}

register_schema "user-complaint/schema-v1.avro" "user_complaint-value"
register_schema "user-complaint/schema-v2.avro" "user_complaint-value"
register_schema "weather-row/schema-v1.avsc" "WEATHER_ROW-value"

