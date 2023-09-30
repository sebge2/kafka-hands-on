#!/bin/sh

FILE="docker-compose-kraft.yml"
PROJECT="kafka"

docker-compose -f $FILE -p $PROJECT up -d --remove-orphans
