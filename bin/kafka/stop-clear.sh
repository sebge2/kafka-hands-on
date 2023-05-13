#!/bin/sh

FILE="docker-compose-zookeeper.yml"

if [ $1 = "kraft" ]; then
  FILE="docker-compose-kraft.yml"
fi

docker-compose -f $FILE down --remove-orphans -v

rm -rf ./volume/zookeeper/zookeeper-1/*
rm -rf ./volume/zookeeper/zookeeper-2/*

rm -rf ./volume/broker/broker-1/*
rm -rf ./volume/broker/broker-2/*

