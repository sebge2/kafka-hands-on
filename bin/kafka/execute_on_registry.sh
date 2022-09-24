#!/bin/sh

containerId=$(docker ps -f name=schema-registry -q)

docker exec -it $containerId bash
