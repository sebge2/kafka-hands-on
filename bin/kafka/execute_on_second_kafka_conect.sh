#!/bin/sh

containerId=$(docker ps -f name=kafka-connect-2 -q)

docker exec -it $containerId bash
