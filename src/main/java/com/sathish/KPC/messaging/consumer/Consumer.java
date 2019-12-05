package com.sathish.KPC.messaging.consumer;

import com.sathish.KPC.dto.ConsumerDTO;
import com.sathish.KPC.dto.DlqProducerDTO;
import com.sathish.KPC.messaging.producer.Producer;
import com.sathish.KPC.messaging.streams.Stream;
import com.sathish.KPC.service.MessageProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.sql.Timestamp;

import static com.sathish.KPC.messaging.common.Utils.ackEvent;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;
import static com.sathish.KPC.utils.LoggingUtils.doLogWarnWithMessageAndObject;

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

    private DlqProducerDTO prepareDlqProducerData(ConsumerDTO consumerData) {
        DlqProducerDTO dlqProducerData = new DlqProducerDTO();
        dlqProducerData.setPayload(consumerData.getPayload());
        dlqProducerData.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime() + 60000);

        return dlqProducerData;
    }
}
