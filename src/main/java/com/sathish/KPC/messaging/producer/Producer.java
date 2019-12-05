package com.sathish.KPC.messaging.producer;

import com.sathish.KPC.dto.DlqProducerDTO;
import com.sathish.KPC.dto.ProducerDTO;
import com.sathish.KPC.messaging.streams.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.sathish.KPC.utils.LoggingUtils.*;

@Service
public class Producer {

    @Autowired
    private Stream stream;

    /**
     * Produces message to the topic
     * @param producerData Producer dto
     * @throws RuntimeException RuntimeException
     */
    public void messageProducer(ProducerDTO producerData) throws RuntimeException {
        doLogInfoWithMessageAndObject("Producing message event to Kafka broker, message = {}", producerData);

        MessageChannel messageChannel = stream.outboundProducer1();

        try {
            boolean producerSuccess = sendMessage(messageChannel, producerData);

            if(producerSuccess)
                doLogInfoWithMessage("Sent message event to Kafka broker");
            else {
                doLogErrorWithMessageAndObject("Failed sending message event to Kafka broker, message = {}", producerData);
            }
        } catch (RuntimeException ex) {
            doLogErrorWithMessageAndObject("Failed(RuntimeException) sending message event to Kafka broker, RuntimeException = {}", ex.getMessage());
        }
    }

    /**
     * Produces message to the DLQ topic
     * @param producerData Producer dto
     * @throws RuntimeException RuntimeException
     */
    public void messageProducerToDLQ(DlqProducerDTO producerData) throws RuntimeException {
        doLogInfoWithMessageAndObject("Producing(To DLQ) message event to Kafka broker, message = {}", producerData);

        MessageChannel messageChannel1 = stream.outboundDLQProducer1();
        MessageChannel messageChannel2 = stream.outboundDLQProducer2();

        try {
            boolean producerSuccess1 = sendMessageToDLQ(messageChannel1, producerData);
            boolean producerSuccess2 = sendMessageToDLQ(messageChannel2, producerData);

            if(producerSuccess1 && producerSuccess2)
                doLogInfoWithMessage("Sent(To DLQ) message event to Kafka broker");
            else {
                doLogErrorWithMessageAndObject("Failed sending(To DLQ) message event to Kafka broker, message = {}", producerData);
            }
        } catch (RuntimeException ex) {
            doLogErrorWithMessageAndObject("Failed(RuntimeException) sending(To DLQ) message event to Kafka broker, RuntimeException = {}", ex.getMessage());
        }
    }

    public void messageProducerToDLQEx(DlqProducerDTO producerData) throws RuntimeException {
        doLogInfoWithMessageAndObject("Producing(To DLQEx) message event to Kafka broker, message = {}", producerData);

        MessageChannel messageChannel = stream.outboundDLQProducer2();

        try {
            boolean producerSuccess = sendMessageToDLQ(messageChannel, producerData);

            if(producerSuccess)
                doLogInfoWithMessage("Sent(To DLQEx) message event to Kafka broker");
            else {
                doLogErrorWithMessageAndObject("Failed sending(To DLQEx) message event to Kafka broker, message = {}", producerData);
            }
        } catch (RuntimeException ex) {
            doLogErrorWithMessageAndObject("Failed(RuntimeException) sending(To DLQEx) message event to Kafka broker, RuntimeException = {}", ex.getMessage());
        }
    }

    private boolean sendMessage(MessageChannel messageChannel, ProducerDTO producerData) {
        return messageChannel.send(MessageBuilder.withPayload(producerData).build());
    }

    private boolean sendMessageToDLQ(MessageChannel messageChannel, DlqProducerDTO producerData) {
        return messageChannel.send(MessageBuilder.withPayload(producerData).build());
    }
}
