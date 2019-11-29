package com.sathish.KPC.producer;

import com.sathish.KPC.data.ProducerData;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class CustomPartitionKeyExtractorClass {

    @Bean
    public Object CustomPartitionKeyExtractorClass1() {
        return new PartitionKeyExtractorStrategy() {
            @Override
            public Object extractKey(Message<?> message) {
                ProducerData producerData = (ProducerData) message.getPayload();

                return producerData.getPayload();
            }
        };
    }
}
