package com.sathish.KPC.messaging.consumer;

import com.sathish.KPC.dto.ConsumerDTO;
import com.sathish.KPC.messaging.streams.Stream;
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
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessage;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;

@Component
public class DLQConsumer {

    @StreamListener(value = Stream.DLQ_CONSUME_1)
    public void messageConsumer1DLQ(@Valid Message<ConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) throws Exception {
        doLogInfoWithMessageAndObject("DLQ 1 message consumer Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received(From DLQ 1) message event in consumer, message = {}", consumerData);

        // Comment the for loop and uncomment the below line to intentionally fail the message and push the pre configured DLQ
        // throw new Exception("Intentional exception from messageConsumer1DLQ");
        for(int i = 0; i < DLQ_TOPIC_TOTAL_PARTITION; i++)
            consumer.pause(Collections.singleton(new TopicPartition(DLQ_TOPIC_NAME_1, i)));
    }

    @Bean
    public ApplicationListener<ListenerContainerIdleEvent> wakingUpAllDLQConsumerAsPerRetryLogic() {
        return event -> {
            Collection<TopicPartition> topicPartitions = event.getTopicPartitions();
            for(TopicPartition topicPartition : topicPartitions) {
                if(DLQ_TOPIC_PARTITION_MAP.containsKey(topicPartition.toString())) {
                    doLogInfoWithMessageAndObject("Resuming the DLQ Consumers as per each topic logic's, event = {}", event);

                    if(event.getConsumer().paused().size() > 0) {
                        doLogInfoWithMessage("Found new events in DLQ. Waking up and gonna process the messages...");
                        event.getConsumer().resume(event.getConsumer().paused());
                    } else {
                        doLogInfoWithMessage("No new events posted to DLQ. Gonna sleep again ZZZ...");
                    }
                }
            }
        };
    }
}