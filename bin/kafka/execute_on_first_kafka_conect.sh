#!/bin/sh

containerId=$(docker ps -f name=kafka-connect-1 -q)

docker exec -it $containerId bash
