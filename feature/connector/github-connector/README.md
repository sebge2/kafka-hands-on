Welcome to your new Kafka Connect connector!


``````
mvn archetype:generate -DarchetypeGroupId=io.confluent.maven -DarchetypeArtifactId=kafka-connect-quickstart -DarchetypeVersion=0.10.0.0 \
-Dpackage=com.opencredo.examples \
-DgroupId=com.opencredo.examples \
-DartifactId=my-source-connector \
-Dversion=1.0-SNAPSHOT
``````

# Running in development

```
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
$CONFLUENT_HOME/bin/connect-standalone $CONFLUENT_HOME/etc/schema-registry/connect-avro-standalone.properties config/MySourceConnector.properties
```
