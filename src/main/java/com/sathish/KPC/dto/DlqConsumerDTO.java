package com.sathish.KPC.dto;

import lombok.Data;

@Data
public class DlqConsumerDTO {
    private String payload;
    private long timestamp;
}