package com.sathish.KPC.consumer;

import com.sathish.KPC.data.ConsumerData;
import com.sathish.KPC.data.ProducerData;
import com.sathish.KPC.producer.Producer;
import com.sathish.KPC.service.MessageProcessingService;
import com.sathish.KPC.streams.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@Log4j2
public class Consumer {

    @Autowired
    private MessageProcessingService messageProcessingService;

    @Autowired
    private Producer producer;

    @StreamListener(value = Stream.INPUT)
    public void messageConsumer(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        log.info("Message Consumer Info = {}", consumer);

        log.info("Received message event in consumer, message = {}", consumerData);

        log.info("Processing message event from consumer, message = {}", consumerData);
        boolean messageProcessingResult = messageProcessingService.processMessage(consumerData.getPayload().getPayload());

        log.info("Finished processing message event from consumer, message = {}", consumerData);

        if(messageProcessingResult) {
            log.info("Successfully processed message event from consumer, so committing the offset message = {}", consumerData);
        } else {
            log.warn("Failure in processing message event from consumer, so pushing the message to DLQ = {}", consumerData);

            ProducerData dlqProducerData = new ProducerData();
            dlqProducerData.setPayload(consumerData.getPayload().getPayload());

            producer.messageProducerToDLQ(dlqProducerData);
        }

        Acknowledgment acknowledgment= consumerData.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if(acknowledgment != null) {
            log.info("Acknowledgement is {} for message = {}", acknowledgment, consumerData);
            acknowledgment.acknowledge();
        }
        else {
            log.warn("Acknowledgement is null for message = {}", consumerData);
        }
    }
}
