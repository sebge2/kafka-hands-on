# KSQL

This page is based on this [course](https://www.udemy.com/course/kafka-ksql/) and show different features of KSQL
through the taxi application.


## Setup Avro Schemas

Setup schemas that will be used by topics using Avro:
- Create the user-complaint schema, go in this [readme](/bin/kafka/volume/schema/user-complaint/README.md).
- Create the weather-row schema, go in this [readme](/bin/kafka/volume/schema/weather-row/README.md).


## Setup User Defined Functions

Setup functions that will be used by KSQL queries:
- Create the taxi-time function, go in this [readme](/readme.md).


## Create Tables/Streams

The following commands will be executed on the KSQL container:

````
cd bin/kafka
./start.sh
./execute_on_ksql.sh
ksql
````

Let's create all the structure of our application.

````
run script '/ksql/sample-queries/01-create-stream.ksql';
run script '/ksql/sample-queries/02-create-stream-from-stream.ksql';
run script '/ksql/sample-queries/03-create-table.ksql';
run script '/ksql/sample-queries/04-create-join-from-table-stream.ksql';
run script '/ksql/sample-queries/05-create-stream-for-pull-queries.ksql';
run script '/ksql/sample-queries/06-create-aggregate-query.ksql';
run script '/ksql/sample-queries/07-create-source-connector.ksql';
run script '/ksql/sample-queries/08-create-stream-avro.ksql';
run script '/ksql/sample-queries/09-create-stream-struct.ksql'; 
run script '/ksql/sample-queries/10-create-stream-with-key.ksql';
run script '/ksql/sample-queries/11-create-table-with-key.ksql';
run script '/ksql/sample-queries/12-repartition-stream.ksql';
run script '/ksql/sample-queries/13-mergin-streams.ksql';
run script '/ksql/sample-queries/14-windowing.ksql';
run script '/ksql/sample-queries/15-geospatial.ksql';
run script '/ksql/sample-queries/16-use-user-defined-function.ksql';
````


## Produce Data

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh

cat /my-data/taxi-app/weather-data.json | kafka-console-producer --bootstrap-server localhost:19092 --topic weather
cat /my-data/taxi-app/driver-profile-data.json | kafka-console-producer --bootstrap-server localhost:19092 --topic driver_profile
````

Then [start connectors](../../bin/kafka/volume/connectors-config/taxi-app-datagen-source-distributed/README.md) that will produce data thanks to Kafka Source Connectors.


## Management

````
list topics;

list tables;

describe users_stream;

describe extended user_profile_pretty;

describe function taxi_wait;

drop stream if exists users_stream;

drop stream if exists users_stream delete topic;

terminate query CSAS_USER_PROFILE_PRETTY_5;

list functions;

show queries;

explain [QUERY ID];

list properties;
````


### Print Command

In the first terminal:

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
kafka-topics --bootstrap-server localhost:19092 --create --topic users --partitions 3 --replication-factor 1

# After start printing users in the second terminal:
kafka-console-producer --bootstrap-server localhost:19092 --topic users
````

In the second terminal:

````
./execute_on_ksql.sh
ksql

# In KSQL command line:
print 'users';

# Start the producer in the first terminal and send messages
# Press CTRL+C

print 'users' from beginning;
# Press CTRL+C

print 'users' from beginning limit 2;
# Press CTRL+C

print 'users' from beginning interval 2 limit 2;  # print 1st message and 3rd message
# Press CTRL+C
````



### KSQL - Create Command

In the first terminal:

````
cd bin/kafka
./start.sh
./execute_on_first_broker.sh
kafka-topics --bootstrap-server localhost:19092 --create --topic users --partitions 3 --replication-factor 1

# After start selecting users in the second terminal:
kafka-console-producer --bootstrap-server localhost:19092 --topic users
````

On second terminal:

````
./execute_on_ksql.sh
ksql

# In KSQL command line:
create stream users_stream (name VARCHAR, countrycode VARCHAR) with (kafka_topic='users', value_format='DELIMITED');
select * from users_stream emit changes; # Emit values in the first terminal
````

## Links

https://www.confluent.io/blog/kafka-streams-vs-ksqldb-compared/


set 'auto.offset.reset'='earliest';

select country_code, count(*) from user_profile group by country_code emit changes;

select numbers from country_driver where rowkey = 'BE';






TODO








kafka-console-producer --bootstrap-server localhost:19092 --topic user_complaint --property value.schema='user_complaint-value'



select * from country_driver where rowkey='BE';
print 'db-car_user' from beginning;



select city->name, description from weather emit changes;



kafka-console-producer --bootstrap-server localhost:19092 --topic weather  <<< '{"city": {"name": "Arlon", "country": "Belgium", "latitude": -33.8, "longitude": 150}, "description": "light rain", "clouds": 92, "deg": -3, "humidity": 80, "pressure": 1019, "rain": 0}'
kafka-console-producer --bootstrap-server localhost:19092 --topic weather  <<< '{"city": {"name": "Arlon", "country": "Belgium", "latitude": -33.8, "longitude": 150}, "description": "strong rain", "clouds": 92, "deg": -3, "humidity": 80, "pressure": 1019, "rain": 0}'



https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-reference/select-pull-query/
select * from country_driver where rowkey='BE';


select * from rider_request_world emit changes;
should by generated with datagen:
    kafka-console-producer --bootstrap-server localhost:19092 --topic rider_request_europe  <<< '{"name": "John Smith", "city": "Arlon", "latitude": -33.8, "longitude": 150}'
    kafka-console-producer --bootstrap-server localhost:19092 --topic rider_request_usa  <<< '{"name": "John Smith", "city": "San Fransisco", "latitude": -43.9, "longitude": 89}'


kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'BE:BE,Belgium'
kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'FR:FR,France'
kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'AU:AU,Australia'
kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'IN:IN,India'
kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'GB:GB,Great Britain'
kafka-console-producer --bootstrap-server localhost:19092 --topic country4 --property "parse.key=true" --property "key.separator=:" <<< 'US:US,United States'