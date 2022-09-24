#!/bin/sh

containerId=$(docker ps -f name=avro -q)

docker exec -it $containerId sh
