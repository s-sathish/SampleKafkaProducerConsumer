package com.sathish.KPC.messaging.consumer;

import com.sathish.KPC.dto.ConsumerDTO;
import com.sathish.KPC.dto.ProducerDTO;
import com.sathish.KPC.messaging.producer.Producer;
import com.sathish.KPC.service.MessageProcessingService;
import com.sathish.KPC.messaging.streams.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

import static com.sathish.KPC.utils.LoggingUtils.*;

@Component
public class Consumer {

    @Autowired
    private MessageProcessingService messageProcessingService;

    @Autowired
    private Producer producer;

    @StreamListener(value = Stream.INPUT_1)
    public void messageConsumer1(@Valid Message<ConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        doLogInfoWithMessageAndObject("Message Consumer 1 Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received message event in consumer1, message = {}", consumerData);

        doLogInfoWithMessageAndObject("Processing message event from consumer1, message = {}", consumerData);
        boolean messageProcessingResult = processMessage(consumerData.getPayload().getPayload());

        doLogInfoWithMessageAndObject("Finished processing message event from consumer1, message = {}", consumerData);

        if(messageProcessingResult) {
            doLogInfoWithMessageAndObject("Successfully processed message event from consumer, so committing the offset message = {}", consumerData);
        } else {
            doLogWarnWithMessageAndObject("Failure in processing message event from consumer, so pushing the message to DLQ = {}", consumerData);
            produceMessageToDLQ(consumerData.getPayload());
        }

        ackEvent(consumerData);
    }

    @StreamListener(value = Stream.INPUT_2)
    public void messageConsumer2(@Valid Message<ConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        doLogInfoWithMessageAndObject("Message Consumer 2 Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received message event in consumer2, message = {}", consumerData);
        doLogInfoWithMessageAndObject("Processing message event from consumer2, message = {}", consumerData);
        doLogInfoWithMessageAndObject("Finished processing message event from consumer2, message = {}", consumerData);

        ackEvent(consumerData);
    }

    private boolean processMessage(String payload) {
        return messageProcessingService.processMessage(payload);
    }

    private void produceMessageToDLQ(ConsumerDTO consumerData) {
        producer.messageProducerToDLQ(prepareDlqProducerData(consumerData));
    }

    private ProducerDTO prepareDlqProducerData(ConsumerDTO consumerData) {
        ProducerDTO dlqProducerData = new ProducerDTO();
        dlqProducerData.setPayload(consumerData.getPayload());

        return dlqProducerData;
    }

    private void ackEvent(@Valid Message<ConsumerDTO> consumerData) {
        Acknowledgment acknowledgment= consumerData.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if(acknowledgment != null) {
            doLogInfoWithMessageAndObject("Acknowledgement is {} for message = {}", acknowledgment, consumerData);
            acknowledgment.acknowledge();
        }
        else {
            doLogWarnWithMessageAndObject("Acknowledgement is null for message = {}", consumerData);
        }
    }
}
