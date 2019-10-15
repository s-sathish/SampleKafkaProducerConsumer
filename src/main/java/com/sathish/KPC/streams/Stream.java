package com.sathish.KPC.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Stream {
    /**
     * Main producer, consumer
     */
    String INPUT = "in";
    String OUTPUT = "out";

    /**
     * DLQ Producer stream
     */
    String DLQ_PRODUCE_1 = "dlq-out-1";
    String DLQ_PRODUCE_2 = "dlq-out-2";

    /**
     * DLQ Consumer stream
     */
    String DLQ_CONSUME_1 = "dlq-in-1";
    String DLQ_CONSUME_2 = "dlq-in-2";

    @Input(INPUT)
    SubscribableChannel inboundConsumer();

    @Output(OUTPUT)
    MessageChannel outboundProducer();

    @Output(DLQ_PRODUCE_1)
    MessageChannel outboundDLQProducer1();

    @Output(DLQ_PRODUCE_2)
    MessageChannel outboundDLQProducer2();

    @Input(DLQ_CONSUME_1)
    SubscribableChannel inboundDLQConsumer1();

    @Input(DLQ_CONSUME_2)
    SubscribableChannel inboundDLQConsumer2();
}
