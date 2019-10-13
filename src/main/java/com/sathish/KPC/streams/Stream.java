package com.sathish.KPC.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Stream {
    String INPUT = "in";
    String OUTPUT = "out";

    @Input(INPUT)
    SubscribableChannel inboundConsumer();

    @Output(OUTPUT)
    MessageChannel outboundProducer();
}
