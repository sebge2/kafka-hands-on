#!/bin/sh

FILE="docker-compose-zookeeper.yml"

if [ $1 = "kraft" ]; then
  FILE="docker-compose-kraft.yml"
fi

docker-compose -f $FILE down --remove-orphans