#!/bin/sh

remove_ca_certificate() {
  rm -f "./server.cert"
  rm -f "./server.csr"
  rm -f "./server.key"
  rm -f "./.srl"
}

generate_ca_certificates() {
  echo "Generate CA certificate"
  openssl req -new -newkey rsa:2048 -nodes -keyout ./server.key -out ./server.csr -subj "/C=BE/CN=Broker CA"
  openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -subj "/C=BE/CN=Broker CA" -keyout ./server.key  -out ./server.cert
  echo ""
}


generate_broker_certificates() {
  BROKER_NAME=$1
  BROKE_HOSTNAME="kafka-$BROKER_NAME"
  BROKER_SSL_CONFIG_DIR="./volume/broker/$BROKER_NAME/ssl"

  BROKER_TRUSTSTORE_LOCATION="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.ts.p12"
  BROKER_TRUSTSTORE_PASSWORD="server-$BROKER_NAME-ts-password"

  BROKER_KEYSTORE_LOCATION="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.ks.p12"
  BROKER_KEYSTORE_PASSWORD="server-$BROKER_NAME-ks-password"
  
  BROKER_CERTIFICATE_UNSIGNED="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.cert"
  BROKER_CERTIFICATE_SIGNED="$BROKER_SSL_CONFIG_DIR/server-$BROKER_NAME.signed.cert"
  
  rm -rf "$BROKER_SSL_CONFIG_DIR"
  mkdir -p "$BROKER_SSL_CONFIG_DIR"

  echo "Generate a trust store for brokers containing the CA certificate (inter-broker authentication)"
  keytool -import -file "./server.cert" -keystore "$BROKER_TRUSTSTORE_LOCATION" -storetype PKCS12 -storepass "$BROKER_TRUSTSTORE_PASSWORD" -alias "BrokerCARoot" -noprompt
  echo ""

  echo "Generate a private key for broker"
  keytool -genkey -keyalg RSA -keysize 2048 -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -storetype PKCS12 -dname "CN=$BROKE_HOSTNAME,O=SGerard,C=BE" -validity 365
  echo ""

  echo "Export broker certificate into a CERT file"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -certreq -file "$BROKER_CERTIFICATE_UNSIGNED"
  echo ""

  openssl x509 -req -CA "./server.cert" -CAkey "./server.key" -in "$BROKER_CERTIFICATE_UNSIGNED" -out "$BROKER_CERTIFICATE_SIGNED" -days 365 -CAcreateserial

  echo "Import CA certificate into broker's keystore"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "BrokerCARoot" -import -file "./server.cert" -noprompt

  echo "Import Broker certificate into broker's keystore"
  keytool -keystore "$BROKER_KEYSTORE_LOCATION" -storepass "$BROKER_KEYSTORE_PASSWORD" -keypass "$BROKER_KEYSTORE_PASSWORD" -alias "$BROKE_HOSTNAME" -import -file "$BROKER_CERTIFICATE_SIGNED" -noprompt
}

remove_ca_certificate
generate_ca_certificates

generate_broker_certificates "broker-1"
generate_broker_certificates "broker-2"

remove_ca_certificate