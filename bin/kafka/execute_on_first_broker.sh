#!/bin/sh

containerId=$(docker ps -f name=kafka-broker-1 -q)

docker exec -it $containerId bash
