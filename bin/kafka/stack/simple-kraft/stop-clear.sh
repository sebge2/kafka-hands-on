#!/bin/sh

PROJECT="kafka"

docker-compose -p $PROJECT down --remove-orphans

rm -rf ../../volume/broker/broker-1/logs
rm -rf ../../volume/broker/broker-2/logs

