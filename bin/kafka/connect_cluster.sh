#!/bin/sh

containerId=$(docker ps -f name=kafka_kafka-1_1 -q)

docker exec -it $containerId bash
