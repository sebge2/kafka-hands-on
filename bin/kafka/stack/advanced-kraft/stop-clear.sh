#!/bin/sh

PROJECT="kafka"

docker-compose -p $PROJECT down --remove-orphans

rm -rf ../../volume/broker/broker-1/logs
rm -rf ../../volume/broker/broker-1/ssl
rm -rf ../../volume/broker/broker-2/logs
rm -rf ../../volume/broker/broker-2/ssl
rm -rf ../../volume/broker/shared/ssl

rm -rf ../../volume/connect/connect-1/ssl
rm -rf ../../volume/connect/connect-2/ssl

rm -rf ../../volume/ksql-server/ssl

rm -rf ../../volume/schema-registry/ssl

rm -rf ../../volume/ui-manager/ssl

