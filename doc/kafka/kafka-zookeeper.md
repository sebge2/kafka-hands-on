# Kafka - Zookeeper

## Cluster Membership

Every broker has a unique identifier (set in the configuration file or automatically generated). Every time a broker starts,
it **registers** itself with its ID in Zookeeper by **creating an ephemeral node** in the path [_/brokers/ids_](http://localhost:8083/editor/data?path=%2Fbrokers%2Fids).
Brokers are notified if _/brokers/ids_ changes: new broker, broker removed.

Example:
````
{
  "features": {},
  "listener_security_protocol_map": {
    "PLAINTEXT": "PLAINTEXT"
  },
  "endpoints": [
    "PLAINTEXT://kafka-broker-1:19092"
  ],
  "rack": "r1",
  "jmx_port": -1,
  "port": 19092,
  "host": "kafka-broker-1",
  "version": 5,
  "timestamp": "1683958453939"
}
````


