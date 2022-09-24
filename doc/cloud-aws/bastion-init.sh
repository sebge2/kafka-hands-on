#!/bin/bash

sudo apt update
sudo apt install default-jre -y

wget https://archive.apache.org/dist/kafka/3.3.2/kafka_2.13-3.3.2.tgz
sudo tar -xzf kafka_2.13-3.3.2.tgz -C /var/lib/

wget https://github.com/aws/aws-msk-iam-auth/releases/download/v1.1.6/aws-msk-iam-auth-1.1.6-all.jar
sudo mv aws-msk-iam-auth-1.1.6-all.jar /var/lib/kafka_2.13-3.3.2/libs/

sudo touch /etc/profile.d/init-msk.sh
sudo chmod ugo+x /etc/profile.d/init-msk.sh

echo "export PATH=\$PATH:/var/lib/kafka_2.13-3.3.2/bin" | sudo tee -a /etc/profile.d/init-msk.sh
echo "export KAFKA_HEAP_OPTS=\"-Xms512m -Xmx1g\"" | sudo tee -a /etc/profile.d/init-msk.sh


sudo touch /var/lib/kafka_2.13-3.3.2/config/client.properties
echo "security.protocol=SASL_SSL" | sudo tee -a /var/lib/kafka_2.13-3.3.2/config/client.properties
echo "sasl.mechanism=AWS_MSK_IAM" | sudo tee -a /var/lib/kafka_2.13-3.3.2/config/client.properties
echo "sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;" | sudo tee -a /var/lib/kafka_2.13-3.3.2/config/client.properties
echo "sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler" | sudo tee -a /var/lib/kafka_2.13-3.3.2/config/client.properties