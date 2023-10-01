#!/bin/sh

# https://docs.cloudera.com/runtime/7.2.0/kafka-securing/topics/kafka-secure-sign-cert.html
# https://help.hcltechsw.com/unica/Journey/en/12.1.1/Journey/AdminGuide/Configuration_of_kafka_on_SSL.html
# https://www.baeldung.com/spring-boot-kafka-ssl
# https://dalelane.co.uk/blog/?p=4399

# shellcheck disable=SC2039
CURRENT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

remove_ca_certificate() {
  rm -f "$CURRENT_DIRECTORY/broker-ca.cert"
  rm -f "$CURRENT_DIRECTORY/broker-ca.csr"
  rm -f "$CURRENT_DIRECTORY/broker-ca.key"
  rm -f "$CURRENT_DIRECTORY/client-ca.cert"
  rm -f "$CURRENT_DIRECTORY/client-ca.csr"
  rm -f "$CURRENT_DIRECTORY/client-ca.key"
  rm -f "$CURRENT_DIRECTORY/.srl"
  rm -f "$CURRENT_DIRECTORY/client-ca.srl"
}

generate_broker_ca_certificates() {
  echo "Generate Broker CA certificate"
  openssl req -new -newkey rsa:2048 -nodes -keyout "$CURRENT_DIRECTORY/broker-ca.key" -out "$CURRENT_DIRECTORY/broker-ca.csr" -subj "/C=BE/CN=Broker CA"
  openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -subj "/C=BE/CN=Broker CA" -keyout "$CURRENT_DIRECTORY/broker-ca.key"  -out "$CURRENT_DIRECTORY/broker-ca.cert"
  echo ""
}

generate_client_ca_certificates() {
  echo "Generate Client CA certificate"
  openssl req -new -newkey rsa:2048 -nodes -keyout "$CURRENT_DIRECTORY/client-ca.key" -out "$CURRENT_DIRECTORY/client-ca.csr" -subj "/C=BE/CN=Client CA"
  openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -subj "/C=BE/CN=Client CA" -keyout "$CURRENT_DIRECTORY/client-ca.key"  -out "$CURRENT_DIRECTORY/client-ca.cert"
  echo ""
}

generate_client_certificate() {
  CLIENT_NAME=$1
  CLIENT_SSL_CONFIG_DIR=$2
  CLIENT_SSL_CONFIG_DOCKER_DIR=$3

  CLIENT_TRUSTSTORE_LOCATION="$CLIENT_SSL_CONFIG_DIR/client-$CLIENT_NAME.ts.p12"
  CLIENT_TRUSTSTORE_PASSWORD="client-$CLIENT_NAME-ts-password"

  CLIENT_KEYSTORE_LOCATION="$CLIENT_SSL_CONFIG_DIR/client-$CLIENT_NAME.ks.p12"
  CLIENT_KEYSTORE_PASSWORD="client-$CLIENT_NAME-ks-password"

  CLIENT_CERTIFICATE_UNSIGNED="$CLIENT_SSL_CONFIG_DIR/client-$CLIENT_NAME.cert"
  CLIENT_CERTIFICATE_SIGNED="$CLIENT_SSL_CONFIG_DIR/client-$CLIENT_NAME.signed.cert"

  rm -rf "$CLIENT_SSL_CONFIG_DIR"
  mkdir -p "$CLIENT_SSL_CONFIG_DIR"

  echo "Generate a truststore for client containing the client CA certificate"
  keytool -keystore "$CLIENT_TRUSTSTORE_LOCATION" -storepass "$CLIENT_TRUSTSTORE_PASSWORD" -alias "ClientCARoot" -import -file "$CURRENT_DIRECTORY/client-ca.cert" -noprompt -storetype PKCS12
  echo ""

  echo "Import broker CA certificate into client's truststore"
  keytool -keystore "$CLIENT_TRUSTSTORE_LOCATION" -storepass "$CLIENT_TRUSTSTORE_PASSWORD" -alias "BrokerCARoot" -import -file "$CURRENT_DIRECTORY/broker-ca.cert" -noprompt

  echo "Generate a private key for client"
  keytool -keystore "$CLIENT_KEYSTORE_LOCATION" -storepass "$CLIENT_KEYSTORE_PASSWORD" -keypass "$CLIENT_KEYSTORE_PASSWORD" -alias "$CLIENT_NAME" -genkey -keyalg RSA -keysize 2048 -storetype PKCS12 -dname "CN=$CLIENT_NAME,O=SGerard,C=BE" -validity 365
  echo ""

  echo "Export client certificate into a CERT file"
  keytool -keystore "$CLIENT_KEYSTORE_LOCATION" -storepass "$CLIENT_KEYSTORE_PASSWORD" -keypass "$CLIENT_KEYSTORE_PASSWORD" -alias "$CLIENT_NAME" -certreq -file "$CLIENT_CERTIFICATE_UNSIGNED"
  echo ""

  echo "Sign client certificate with CA"
  openssl x509 -req -CA "$CURRENT_DIRECTORY/client-ca.cert" -CAkey "$CURRENT_DIRECTORY/client-ca.key" -in "$CLIENT_CERTIFICATE_UNSIGNED" -out "$CLIENT_CERTIFICATE_SIGNED" -days 365 -CAcreateserial
  echo ""

  echo "Import Broker CA certificate into client's keystore"
  keytool -keystore "$CLIENT_KEYSTORE_LOCATION" -storepass "$CLIENT_KEYSTORE_PASSWORD" -keypass "$CLIENT_KEYSTORE_PASSWORD" -alias "BrokerCARoot" -import -file "$CURRENT_DIRECTORY/broker-ca.cert" -noprompt
  echo ""

  echo "Import Client CA certificate into client's keystore"
  keytool -keystore "$CLIENT_KEYSTORE_LOCATION" -storepass "$CLIENT_KEYSTORE_PASSWORD" -keypass "$CLIENT_KEYSTORE_PASSWORD" -alias "ClientCARoot" -import -file "$CURRENT_DIRECTORY/client-ca.cert" -noprompt
  echo ""

  echo "Import client certificate into client's keystore"
  keytool -keystore "$CLIENT_KEYSTORE_LOCATION" -storepass "$CLIENT_KEYSTORE_PASSWORD" -keypass "$CLIENT_KEYSTORE_PASSWORD" -alias "$CLIENT_NAME" -import -file "$CLIENT_CERTIFICATE_SIGNED" -noprompt

  echo "# kafka-console-consumer --bootstrap-server kafka-broker-1:19092 --topic test --consumer.config $CLIENT_SSL_CONFIG_DOCKER_DIR/ssl-debug-$CLIENT_NAME.properties
security.protocol = SSL
ssl.truststore.location = $CLIENT_SSL_CONFIG_DOCKER_DIR/client-$CLIENT_NAME.ts.p12
ssl.truststore.password = $CLIENT_TRUSTSTORE_PASSWORD
ssl.keystore.location = $CLIENT_SSL_CONFIG_DOCKER_DIR/client-$CLIENT_NAME.ks.p12
ssl.keystore.password = $CLIENT_KEYSTORE_PASSWORD
ssl.key.password = $CLIENT_KEYSTORE_PASSWORD" > "$CLIENT_SSL_CONFIG_DIR/ssl-debug-$CLIENT_NAME.properties"
}

generate_broker_certificates() {
  BROKER_NAME=$1
  BROKE_HOSTNAME="kafka-$BROKER_NAME"
  BROKER_SSL_CONFIG_DIR="$CURRENT_DIRECTORY/../../volume/broker/$BROKER_NAME/ssl"

  BROKER_TRUSTSTORE_LOCATION="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.ts.p12"
  BROKER_TRUSTSTORE_PASSWORD="server-$BROKER_NAME-ts-password"

  BROKER_KEYSTORE_LOCATION="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.ks.p12"
  BROKER_KEYSTORE_PASSWORD="server-$BROKER_NAME-ks-password"
  
  BROKER_CERTIFICATE_UNSIGNED="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.cert"
  BROKER_CERTIFICATE_SIGNED="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.signed.cert"
  
  rm -rf "$BROKER_SSL_CONFIG_DIR"
  mkdir -p "$BROKER_SSL_CONFIG_DIR"

  echo "Generate a truststore for brokers containing the CA certificate (inter-broker authentication)"
  keytool -keystore "$BROKER_TRUSTSTORE_LOCATION" -storepass "$BROKER_TRUSTSTORE_PASSWORD" -alias "BrokerCARoot" -import -file "./broker-ca.cert" -noprompt -storetype PKCS12
  echo ""

  echo "Import Client CA certificate into broker's truststore"
  keytool -keystore "$BROKER_TRUSTSTORE_LOCATION" -storepass "$BROKER_TRUSTSTORE_PASSWORD" -alias "ClientCARoot" -import -file "./client-ca.cert" -noprompt

  echo "Generate a private key for broker"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -genkey -keyalg RSA -keysize 2048 -storetype PKCS12 -dname "CN=$BROKE_HOSTNAME,O=SGerard,C=BE" -validity 365
  echo ""

  echo "Export broker certificate into a CERT file"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -certreq -file "$BROKER_CERTIFICATE_UNSIGNED"
  echo ""

  echo "Sign broker certificate with CA"
  openssl x509 -req -CA "./broker-ca.cert" -CAkey "./broker-ca.key" -in "$BROKER_CERTIFICATE_UNSIGNED" -out "$BROKER_CERTIFICATE_SIGNED" -days 365 -CAcreateserial
  echo ""

  echo "Import Broker CA certificate into broker's keystore"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "BrokerCARoot" -import -file "./broker-ca.cert" -noprompt

  echo "Import Client CA certificate into broker's keystore"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "ClientCARoot" -import -file "./client-ca.cert" -noprompt

  echo "Import Broker certificate into broker's keystore"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -import -file "$BROKER_CERTIFICATE_SIGNED" -noprompt
}

remove_ca_certificate

generate_broker_ca_certificates
generate_client_ca_certificates

generate_client_certificate "admin" "$CURRENT_DIRECTORY/../../volume/broker/shared/ssl/admin" "/kafka/shared-ssl/admin"
generate_client_certificate "sample-client-1" "$CURRENT_DIRECTORY/../../volume/broker/shared/ssl/sample-client-1" "/kafka/shared-ssl/sample-client-1"
generate_client_certificate "sample-client-2" "$CURRENT_DIRECTORY/../../volume/broker/shared/ssl/sample-client-2" "/kafka/shared-ssl/sample-client-2"

generate_client_certificate "ui-manager" "$CURRENT_DIRECTORY/../../volume/ui-manager/ssl" "/ui-manager/ssl"
generate_client_certificate "ksql-server" "$CURRENT_DIRECTORY/../../volume/ksql-server/ssl" "/ksql/ssl"
generate_client_certificate "schema-registry" "$CURRENT_DIRECTORY/../../volume/schema-registry/ssl" "/schema-registry/ssl"
generate_client_certificate "connect-1" "$CURRENT_DIRECTORY/../../volume/connect/connect-1/ssl" "/connect/ssl"
generate_client_certificate "connect-2" "$CURRENT_DIRECTORY/../../volume/connect/connect-2/ssl" "/connect/ssl"

generate_broker_certificates "broker-1"
generate_broker_certificates "broker-2"

remove_ca_certificate