#!/bin/sh

containerId=$(docker ps -f name=kafka-hands-on-range-assignor_kafka-1_1 -q)

docker exec -it $containerId bash
