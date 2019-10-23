package com.sathish.KPC.utils;

import java.util.HashMap;

public class Constants {
    /**
     * API properties
     */
    public final static String API_SUCCESS_RESPONSE_MESSAGE = "Success";

    /**
     * DLQ properties
     */
    public final static Integer DLQ_TOPIC_TOTAL_PARTITION  = 2;
    public final static String  DLQ_TOPIC_NAME_1           = "producer-consumer-topic-dlq-1";
    public final static String  DLQ_TOPIC_NAME_2           = "producer-consumer-topic-dlq-2";

    public final static HashMap<String, Integer> DLQ_TOPIC_PARTITION_MAP = new HashMap<>();

    public static HashMap<String, Integer> DLQ_TOPIC_NAMES_AND_PARTITIONS() {
        for(int i = 0; i < DLQ_TOPIC_TOTAL_PARTITION; i++) {
            DLQ_TOPIC_PARTITION_MAP.put(DLQ_TOPIC_NAME_1 + "-" + i, 1);
            DLQ_TOPIC_PARTITION_MAP.put(DLQ_TOPIC_NAME_2 + "-" + i, 1);
        }

        return DLQ_TOPIC_PARTITION_MAP;
    }
}
