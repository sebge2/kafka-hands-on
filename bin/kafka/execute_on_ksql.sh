#!/bin/sh

containerId=$(docker ps -f name=ksql-server -q)

docker exec -it $containerId bash
