package com.sathish.KPC.messaging.producer.partitioner;

import com.sathish.KPC.dto.DlqProducerDTO;
import com.sathish.KPC.dto.ProducerDTO;
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
                ProducerDTO producerData = (ProducerDTO) message.getPayload();

                return producerData.getPayload();
            }
        };
    }

    @Bean
    public Object CustomPartitionKeyExtractorClass2() {
        return new PartitionKeyExtractorStrategy() {
            @Override
            public Object extractKey(Message<?> message) {
                DlqProducerDTO producerData = (DlqProducerDTO) message.getPayload();

                return producerData.getPayload();
            }
        };
    }
}
