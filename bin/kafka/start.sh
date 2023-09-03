#!/bin/sh

FILE="docker-compose-zookeeper.yml"

if [ $1 = "kraft" ]; then
  FILE="docker-compose-kraft.yml"
elif [ $1 = "kraft-full" ]; then
  FILE="docker-compose-kraft-full.yml"
fi

docker-compose -f $FILE up -d --remove-orphans
