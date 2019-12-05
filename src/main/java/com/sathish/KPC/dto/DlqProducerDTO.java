package com.sathish.KPC.dto;

import lombok.Data;

@Data
public class DlqProducerDTO {
    private String payload;
    private long timestamp;
}
