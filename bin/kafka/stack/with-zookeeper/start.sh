#!/bin/sh

FILE="docker-compose-zookeeper.yml"
PROJECT="kafka"

docker-compose -f $FILE -p $PROJECT up -d --remove-orphans
