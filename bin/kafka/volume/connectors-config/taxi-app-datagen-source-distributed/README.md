# Taxi Application - Mocked Data

The purpose of this connector is to generate fake data for the taxi application used
to manipulate [KSQL commands](../../../../../doc/ksql/readme.md). Please start generation only when
streams and tables are set up.


## User Profile

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @user-profile-connector-config.json | jq`.


## Rider Request - USA

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @ride-request-usa-connector-config.json | jq`.


## Rider Request - Europe

- Execute in current directory `curl -X POST -H "Content-type:application/json" -s localhost:18083/connectors -d @ride-request-europe-connector-config.json | jq`.