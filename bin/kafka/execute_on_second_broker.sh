#!/bin/sh

containerId=$(docker ps -f name=kafka-broker-2 -q)

docker exec -it $containerId bash
