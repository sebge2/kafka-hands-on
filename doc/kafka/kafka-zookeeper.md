# Kafka - Zookeeper

Zookeeper has two important functions, it's used to:
- **elect a controller**
- **store the cluster metadata**:
  - registered brokers
  - configuration
  - topics
  - partitions
  - replicas.

## Cluster Membership

Every broker has a unique identifier (set in the configuration file or automatically generated). Every time a broker starts,
it **registers** itself with its ID in Zookeeper by **creating an ephemeral node** in the path _[/brokers/ids](http://localhost:8083/editor/data?path=%2Fbrokers%2Fids)_.
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


## Broker Controller

There is **one broker controller per cluster**. The first broker that starts becomes the controller. When a broker start
it attempts to create an ephemeral node in the path _[/controller](http://localhost:8083/editor/data?path=%2Fcontroller)_.

Example:
````
{
  "version": 1,
  "brokerid": 1,
  "timestamp": "1683958453948"
}
````

If the creation failed, they realize that there is already a controller. If a broker stops/fails/doesn't answer the heartbeats, the ephemeral nodes is removed and 
other brokers are notified. The first broker that succeeds to create a new ephemeral node becomes the controller. 

The _[/controller_epoch](http://localhost:8083/editor/data?path=%2Fcontroller_epoch)_ is increased and allows to identify who is the controller (in case an old controller still acts as the unique controller).

The controller is responsible to assign new partitions leaders when a broker leaves the cluster (by watching _[/brokers/ids](http://localhost:8083/editor/data?path=%2Fbrokers%2Fids)_) and is 
the leader of those partitions. Afterward the controller:
* save the new state in Zookeeper
* sends a _LeaderAndISR_ request to brokers that contains replicas for those partitions
* sends to every broker a _UpdateMetadata_ request in order to update the cache with all the brokers and all replicas in the cluster