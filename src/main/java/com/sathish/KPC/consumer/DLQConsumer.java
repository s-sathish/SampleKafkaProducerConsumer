package com.sathish.KPC.consumer;

import com.sathish.KPC.data.ConsumerData;
import com.sathish.KPC.streams.Stream;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

import static com.sathish.KPC.utils.Constants.*;

@Component
@Log4j2
public class DLQConsumer {

    @StreamListener(value = Stream.DLQ_CONSUME_1)
    public void messageConsumer1DLQ(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) {
        log.info("DLQ 1 message consumer Info = {}", consumer);

        log.info("Received(From DLQ 1) message event in consumer, message = {}", consumerData);

        consumer.pause(Collections.singleton(new TopicPartition(DLQ_TOPIC_NAME_1, DLQ_TOPIC_PARTITION)));
    }

    @StreamListener(value = Stream.DLQ_CONSUME_2)
    public void messageConsumer2DLQ(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) {
        log.info("DLQ 2 message consumer Info = {}", consumer);

        log.info("Received(From DLQ 2) message event in consumer, message = {}", consumerData);

        consumer.pause(Collections.singleton(new TopicPartition(DLQ_TOPIC_NAME_2, DLQ_TOPIC_PARTITION)));
    }

    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> wakingUpAllDLQConsumerAsPerRetryLogic() {
        return event -> {
            Collection<TopicPartition> topicPartitions = event.getTopicPartitions();
            for(TopicPartition topicPartition : topicPartitions) {
                if(topicPartition.toString().equals(DLQ_TOPIC_NAME_1_PARTITION)
                        || topicPartition.toString().equals(DLQ_TOPIC_NAME_2_PARTITION)) {
                    log.info("Resuming the DLQ Consumers as per each topic logic's, event = {}", event);

                    if(event.getConsumer().paused().size() > 0) {
                        log.info("Found new events in DLQ. Waking up and gonna process the messages...");
                        event.getConsumer().resume(event.getConsumer().paused());
                    } else {
                        log.info("No new events posted to DLQ. Gonna sleep again ZZZ...");
                    }
                }
            }
        };
    }
}