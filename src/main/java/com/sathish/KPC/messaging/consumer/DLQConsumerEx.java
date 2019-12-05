package com.sathish.KPC.messaging.consumer;

import com.sathish.KPC.dto.DlqConsumerDTO;
import com.sathish.KPC.messaging.streams.Stream;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.sql.Timestamp;

import static com.sathish.KPC.messaging.common.Utils.ackEvent;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;
import static java.lang.Thread.sleep;

@Component
public class DLQConsumerEx {

    @StreamListener(value = Stream.DLQ_CONSUME_3)
    public void messageConsumer1DLQEx(@Valid Message<DlqConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) throws InterruptedException {
        doLogInfoWithMessageAndObject("DLQEx 1 message consumer Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received(From DLQEx 1) message event in consumer, message = {}", consumerData);

        boolean processingState = true;
        while (processingState) {
            if(consumerData.getPayload().getTimestamp() <= new Timestamp(System.currentTimeMillis()).getTime()) {
                doLogInfoWithMessageAndObject("DLQEx 1 message consumer - Message time has arrived...");
                ackEvent(consumerData);
                processingState = false;
            } else {
                doLogInfoWithMessageAndObject("DLQEx 1 message consumer - Message time has not been elapsed yet...");
                sleep(consumerData.getPayload().getTimestamp() - new Timestamp(System.currentTimeMillis()).getTime());
            }
        }
    }

    @StreamListener(value = Stream.DLQ_CONSUME_4)
    public void messageConsumer2DLQEx(@Valid Message<DlqConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) Consumer<?, ?> consumer) throws InterruptedException {
        doLogInfoWithMessageAndObject("DLQEx 2 message consumer Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received(From DLQEx 2) message event in consumer, message = {}", consumerData);

        boolean processingState = true;
        while (processingState) {
            if(consumerData.getPayload().getTimestamp() <= new Timestamp(System.currentTimeMillis()).getTime()) {
                doLogInfoWithMessageAndObject("DLQEx 2 message consumer - Message time has arrived...");
                ackEvent(consumerData);
                processingState = false;
            } else {
                doLogInfoWithMessageAndObject("DLQEx 2 message consumer - Message time has not been elapsed yet...");
                sleep(consumerData.getPayload().getTimestamp() - new Timestamp(System.currentTimeMillis()).getTime());
            }
        }
    }
}
