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

    @StreamListener(value = Stream.INPUT_1)
    public void messageConsumer1(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        log.info("Message Consumer 1 Info = {}", consumer);

        log.info("Received message event in consumer1, message = {}", consumerData);

        log.info("Processing message event from consumer1, message = {}", consumerData);
        boolean messageProcessingResult = messageProcessingService.processMessage(consumerData.getPayload().getPayload());

        log.info("Finished processing message event from consumer1, message = {}", consumerData);

        if(messageProcessingResult) {
            log.info("Successfully processed message event from consumer, so committing the offset message = {}", consumerData);
        } else {
            log.warn("Failure in processing message event from consumer, so pushing the message to DLQ = {}", consumerData);

            ProducerData dlqProducerData = new ProducerData();
            dlqProducerData.setPayload(consumerData.getPayload().getPayload());

            producer.messageProducerToDLQ(dlqProducerData);
        }

        ackEvent(consumerData);
    }

    @StreamListener(value = Stream.INPUT_2)
    public void messageConsumer2(@Valid Message<ConsumerData> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        log.info("Message Consumer 2 Info = {}", consumer);

        log.info("Received message event in consumer2, message = {}", consumerData);

        log.info("Processing message event from consumer2, message = {}", consumerData);

        log.info("Finished processing message event from consumer2, message = {}", consumerData);

        ackEvent(consumerData);
    }

    private void ackEvent(@Valid Message<ConsumerData> consumerData) {
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
