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

    public void messageProducer(ProducerData producerData) {
        log.info("Sending message event to producer, message = {}", producerData);

        MessageChannel messageChannel = stream.outboundProducer();
        messageChannel.send(MessageBuilder.withPayload(producerData)
                .build());

        log.info("Sent message event to producer");
    }
}
