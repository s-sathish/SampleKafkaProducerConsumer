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

import static com.sathish.KPC.utils.Constants.DLQ_TOPIC_NAME;
import static com.sathish.KPC.utils.Constants.DLQ_TOPIC_PARTITION;

@Component
@Log4j2
public class DLQConsumer {

    @StreamListener(value = Stream.DLQ_CONSUME)
    public void messageConsumerDLQ(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) {
        log.info("DLQ message consumer Info = {}", consumer);

        log.info("Received(From DLQ) message event in consumer, message = {}", consumerData);

        consumer.pause(Collections.singleton(new TopicPartition(DLQ_TOPIC_NAME, DLQ_TOPIC_PARTITION)));
    }

    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> wakingUpAfterOneMinute() {
        return event -> {
            Collection<TopicPartition> topicPartitions = event.getTopicPartitions();
            for(TopicPartition topicPartition : topicPartitions) {
                if(topicPartition.toString().equals(DLQ_TOPIC_NAME + "-" + DLQ_TOPIC_PARTITION)) {
                    log.info("Resuming the DLQ Consumer after one minute, event = {}", event);

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