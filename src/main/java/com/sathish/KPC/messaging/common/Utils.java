package com.sathish.KPC.messaging.common;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import javax.validation.Valid;

import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;
import static com.sathish.KPC.utils.LoggingUtils.doLogWarnWithMessageAndObject;

public class Utils {

    public static void ackEvent(@Valid Message<?> consumerData) {
        Acknowledgment acknowledgment= consumerData.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if(acknowledgment != null) {
            doLogInfoWithMessageAndObject("Acknowledgement is {} for message = {}", acknowledgment, consumerData);
            acknowledgment.acknowledge();
        }
        else {
            doLogWarnWithMessageAndObject("Acknowledgement is null for message = {}", consumerData);
        }
    }
}
