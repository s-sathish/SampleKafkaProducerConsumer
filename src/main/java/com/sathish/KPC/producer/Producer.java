package com.sathish.KPC.producer;

import com.sathish.KPC.data.ProducerData;
import com.sathish.KPC.streams.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class Producer {

    @Autowired
    private Stream stream;

    /**
     * Produces message to the topic
     * @param producerData Producer data
     * @throws RuntimeException RuntimeException
     */
    public void messageProducer(ProducerData producerData) throws RuntimeException {
        log.info("Producing message event to Kafka broker, message = {}", producerData);

        MessageChannel messageChannel = stream.outboundProducer1();

        try {
            boolean producerSuccess = messageChannel.send(MessageBuilder.withPayload(producerData).build());

            if(producerSuccess)
                log.info("Sent message event to Kafka broker");
            else {
                log.error("Failed sending message event to Kafka broker, message = {}", producerData);
            }
        } catch (RuntimeException ex) {
            log.error("Failed(RuntimeException) sending message event to Kafka broker, RuntimeException = {}", ex.getMessage());
        }
    }

    /**
     * Produces message to the DLQ topic
     * @param producerData Producer data
     * @throws RuntimeException RuntimeException
     */
    public void messageProducerToDLQ(ProducerData producerData) throws RuntimeException {
        log.info("Producing(To DLQ) message event to Kafka broker, message = {}", producerData);

        MessageChannel messageChannel1 = stream.outboundDLQProducer1();
        MessageChannel messageChannel2 = stream.outboundDLQProducer2();

        try {
            boolean producerSuccess1 = messageChannel1.send(MessageBuilder.withPayload(producerData)
                    .build());
            boolean producerSuccess2 = messageChannel2.send(MessageBuilder.withPayload(producerData)
                    .build());

            if(producerSuccess1 && producerSuccess2)
                log.info("Sent(To DLQ) message event to Kafka broker");
            else {
                log.error("Failed sending(To DLQ) message event to Kafka broker, message = {}", producerData);
            }
        } catch (RuntimeException ex) {
            log.error("Failed(RuntimeException) sending(To DLQ) message event to Kafka broker, RuntimeException = {}", ex.getMessage());
        }
    }
}
