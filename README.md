# SampleKafkaProducerConsumer
Sample Kafka Producer Consumer Application in Spring Boot.


# Setup Kafka locally for testing

Download the latest release from here: http://apachemirror.wuchna.com/kafka/

For example - http://apachemirror.wuchna.com/kafka/2.3.0/kafka_2.12-2.3.0.tgz

```bash
// Unzip and start zookeeper, kafka

$ tar -xzf kafka_2.12-2.3.0.tgz
$ cd kafka_2.12-2.3.0

$ bin/zookeeper-server-start.sh config/zookeeper.properties
$ bin/kafka-server-start.sh config/server.properties
```

In case of multiple binders, replicate the zookeeper, kafka config file under "kafka_2.12-2.3.0" folder and change the following params in each of the replicated file
```bash
Zookeeper:         Kafka:
dataDir=           broker.id=
clientPort=        listeners=
                   log.dirs=
                   zookeeper.connect=
                   
$ bin/zookeeper-server-start.sh config/zookeeper1.properties
$ bin/kafka-server-start.sh config/server1.properties
$ bin/zookeeper-server-start.sh config/zookeeper2.properties
$ bin/kafka-server-start.sh config/server2.properties
```

# Run the application

Once zookeeper and kafka server are started, now run the application.
For simple testing, use the following to initiate producer message.
```http request
curl -X POST \
  http://localhost:8090/kpc/add/data \
  -H 'Content-Type: application/json' \
  -d '{
	"data": "kafka"
}'
```

The above API request will produce a message to the broker.


* `Multiple binders`:

  In our application, we have used multiple kafka binders. Usual producer/consumer will be binding to kafka1 and DLQ producer/consumer will be binding to kafka2
    ```bash
    One binder will be on localhost:2181 - localhost:9092
    One binder will be on localhost:2182 - localhost:9093
    ```


* `Consumer groups`:

  For demonstration purpose of different consumer groups, the message will be delivered to two consumers - both on different consumer groups.


* `Manual acknowledgement`:

  The `ackEvent` function in Consumer class is for manual acknowledgement of the message (autoCommitOffset: false)
    ```java
    Acknowledgment acknowledgment= consumerData.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
    if(acknowledgment != null) {
        acknowledgment.acknowledge();
    }
    ```


* `DLQ handler and Consumer pause/resume`:

  In `MessageProcessingService` class, make the return statement to false, so that the message processing logic is considered as a failure and so the message is sent to DLQ.

  Again two DLQ consumers will be functioning to explain the consumer pause and resume functionality for different `idleEventInterval` for each of the DLQ consumer
    ```java
    // Pause the consumer
    consumer.pause(Collections.singleton(new TopicPartition(TOPIC, PARTITION)));
    
    // Resume the consumer
    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> wakingUpAllDLQConsumerAsPerRetryLogic() {
        return event -> {           
            if(event.getConsumer().paused().size() > 0) {
                event.getConsumer().resume(event.getConsumer().paused());
            }
        };
    }
    ```


* `Instance count and Instance index`:

  These two params are very important when the application is deployed on multiple instances.

    ```bash
    Instance count = Number of instances the application is deployed
    Instance index = Will vary per instance. Starting from 0 to n -1 where n = InstanceCount
    ```


* `Producer partitioning`;

  For data partitioning, the following properties needs to be set correctly.

  On Producer side:
  1. partitionKeyExpression - On what basis to decide the partition (Uses murmur hash internally for partitioning logic)

     Use partitionSelectorName, partitionKeyExtractorName for custom key selector and partition selector. See Implementation in `CustomPartitioner` & `CustomPartitionKeyExtractorClass` class
  2. partitionCount - Number of partitions on the topic

  On Consumer side:
  1. Just mark the consumer as ```partitioned = true```