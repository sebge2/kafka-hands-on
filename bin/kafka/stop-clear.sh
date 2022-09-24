#!/bin/sh

docker-compose down --remove-orphans -v

rm -rf ./volume/zookeeper/zookeeper-1/*
rm -rf ./volume/zookeeper/zookeeper-2/*

rm -rf ./volume/broker/broker-1/*
rm -rf ./volume/broker/broker-2/*

