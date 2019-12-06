package com.sathish.KPC.messaging.consumer;

import com.sathish.KPC.dto.DlqConsumerDTO;
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

import static com.sathish.KPC.messaging.common.Utils.ackEvent;
import static com.sathish.KPC.utils.CommonUtils.getCurrentTimeStamp;
import static com.sathish.KPC.utils.Constants.MAX_TOTAL_ATTEMPTS_FOR_DELAYED_PROCESSING;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;
import static java.lang.Thread.sleep;

@Component
public class DLQConsumerEx {

    @Autowired
    private MessageProcessingService messageProcessingService;

    @Autowired
    private Producer producer;

    @StreamListener(value = Stream.DLQ_CONSUME_2)
    public void messageConsumer1DLQEx(@Valid Message<DlqConsumerDTO> consumerData, @Header(KafkaHeaders.CONSUMER) org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) throws Exception {
        doLogInfoWithMessageAndObject("DLQEx 1 message consumer Info = {}", consumer);
        doLogInfoWithMessageAndObject("Received(From DLQEx 1) message event in consumer, message = {}", consumerData);

        try {
            boolean processingState = true;
            while (processingState) {
                if(consumerData.getPayload().getProcessAfter() <= getCurrentTimeStamp()) {
                    doLogInfoWithMessageAndObject("DLQEx 1 message consumer - Message time has arrived...");
                    processingState = false;

                    boolean messageProcessingResult = processMessage(consumerData.getPayload().getPayload());
                    if(messageProcessingResult)
                        ackEvent(consumerData);
                    else
                        throw new Exception("Intentional exception being thrown in messageConsumer1DLQEx");
                } else {
                    doLogInfoWithMessageAndObject("DLQEx 1 message consumer - Message time has not been elapsed yet...");
                    sleep(consumerData.getPayload().getProcessAfter() - getCurrentTimeStamp());
                }
            }
        } catch (Exception ex) {
            doLogInfoWithMessageAndObject("Caught the thrown intentional exception...");
            ackEvent(consumerData);

            int attemptCount = consumerData.getPayload().getAttemptCount();
            if(++attemptCount <= MAX_TOTAL_ATTEMPTS_FOR_DELAYED_PROCESSING) {
                produceMessageToDLQ(consumerData.getPayload(), attemptCount);
            } else {
                throw new Exception("Exhausted max attempts for delayed processing. Will put the message to Pre configured DLQ...");
            }
        }
    }

    private boolean processMessage(String payload) {
        return messageProcessingService.processMessage(payload);
    }

    private void produceMessageToDLQ(DlqConsumerDTO consumerData, int attemptCount) {
        producer.messageProducerToDLQEx(prepareDlqProducerData(consumerData, attemptCount));
    }

    private DlqProducerDTO prepareDlqProducerData(DlqConsumerDTO consumerData, int attemptCount) {
        DlqProducerDTO dlqProducerData = new DlqProducerDTO();

        dlqProducerData.setPayload(consumerData.getPayload());
        dlqProducerData.setProcessAfter(getCurrentTimeStamp() + (consumerData.getPreviousTimeDelay() * attemptCount));
        dlqProducerData.setPreviousTimeDelay(consumerData.getPreviousTimeDelay() * attemptCount);
        dlqProducerData.setAttemptCount(attemptCount);

        return dlqProducerData;
    }
}
