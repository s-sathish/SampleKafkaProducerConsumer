package com.sathish.KPC.utils;

public class Constants {
    /**
     * API properties
     */
    public final static String API_SUCCESS_RESPONSE_MESSAGE = "Success";

    /**
     * DLQ properties
     */
    public final static Integer DLQ_TOPIC_PARTITION        = 0; // TODO: Support 'N' number of partitions
    public final static String  DLQ_TOPIC_NAME_1           = "producer-consumer-topic-dlq-1";
    public final static String  DLQ_TOPIC_NAME_2           = "producer-consumer-topic-dlq-2";
    public final static String  DLQ_TOPIC_NAME_1_PARTITION = DLQ_TOPIC_NAME_1 + "-" + DLQ_TOPIC_PARTITION;
    public final static String  DLQ_TOPIC_NAME_2_PARTITION = DLQ_TOPIC_NAME_2 + "-" + DLQ_TOPIC_PARTITION;
}
