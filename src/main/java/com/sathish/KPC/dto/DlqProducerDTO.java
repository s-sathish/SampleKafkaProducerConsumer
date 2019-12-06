package com.sathish.KPC.dto;

import lombok.Data;

@Data
public class DlqProducerDTO {
    private String payload;
    private long processAfter;
    private int previousTimeDelay;
    private int attemptCount;
}
