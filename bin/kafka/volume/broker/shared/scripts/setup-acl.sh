#!/bin/sh

export KAFKA_OPTS=""
export KAFKA_JMX_OPTS=""

kafka-acls --bootstrap-server kafka-broker-1:19092 --add --cluster --operation All --allow-principal User:CN=ui-manager,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties
kafka-acls --bootstrap-server kafka-broker-1:19092 --add --cluster --operation All --allow-principal User:CN=ksql-server,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties
kafka-acls --bootstrap-server kafka-broker-1:19092 --add --cluster --operation All --allow-principal User:CN=connect-1,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties
kafka-acls --bootstrap-server kafka-broker-1:19092 --add --cluster --operation All --allow-principal User:CN=connect-2,O=SGerard,C=BE -command-config /kafka/shared-ssl/admin/ssl-debug-admin.properties