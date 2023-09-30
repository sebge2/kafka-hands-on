#!/bin/sh

FILE="docker-compose-zookeeper.yml"
PROJECT="kafka"

docker-compose -f $FILE -p $PROJECT down --remove-orphans