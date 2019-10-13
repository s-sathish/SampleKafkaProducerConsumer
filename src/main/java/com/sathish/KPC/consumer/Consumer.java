package com.sathish.KPC.consumer;

import com.sathish.KPC.data.ConsumerData;
import com.sathish.KPC.streams.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@Log4j2
public class Consumer {

    @StreamListener(value = Stream.INPUT)
    public void messageConsumer(@Valid Message<ConsumerData> consumerData) throws Exception {
        log.info("Received message event in consumer, message = {}", consumerData.getPayload().getPayload());
    }
}
