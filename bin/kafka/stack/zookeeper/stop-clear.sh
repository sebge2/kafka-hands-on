#!/bin/sh

PROJECT="kafka"

docker-compose -p $PROJECT down --remove-orphans

rm -rf ../../volume/zookeeper/zookeeper-1/log
rm -rf ../../volume/zookeeper/zookeeper-1/data
rm -rf ../../volume/zookeeper/zookeeper-2/log
rm -rf ../../volume/zookeeper/zookeeper-2/data

rm -rf ../../volume/broker/broker-1/logs
rm -rf ../../volume/broker/broker-2/logs

