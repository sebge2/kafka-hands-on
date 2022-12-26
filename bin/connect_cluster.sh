#!/bin/sh

containerId=$(docker ps -f name=bin_kafka-1_1 -q)

docker exec -it $containerId bash
