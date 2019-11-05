package com.sathish.KPC.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Stream {
    /**
     * Main producer, consumer
     */
    String INPUT_1  = "in-1";
    String INPUT_2  = "in-2";
    String OUTPUT_1 = "out-1";

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

    @Input(INPUT_1)
    SubscribableChannel inboundConsumer1();

    @Input(INPUT_2)
    SubscribableChannel inboundConsumer2();

    @Output(OUTPUT_1)
    MessageChannel outboundProducer1();

    @Output(DLQ_PRODUCE_1)
    MessageChannel outboundDLQProducer1();

    @Output(DLQ_PRODUCE_2)
    MessageChannel outboundDLQProducer2();

    @Input(DLQ_CONSUME_1)
    SubscribableChannel inboundDLQConsumer1();

    @Input(DLQ_CONSUME_2)
    SubscribableChannel inboundDLQConsumer2();
}
