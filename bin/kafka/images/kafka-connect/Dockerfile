FROM confluentinc/cp-kafka-connect-base:6.1.9

RUN confluent-hub install --no-prompt cjmatta/kafka-connect-sse:1.0
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-elasticsearch:14.0.4
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.6.0
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-datagen:0.6.0
RUN confluent-hub install --no-prompt jcustenborder/kafka-connect-spooldir:2.0.65