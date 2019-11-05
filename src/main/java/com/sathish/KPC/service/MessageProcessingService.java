package com.sathish.KPC.service;

import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService implements IMessageProcessingService {

    @Override
    public boolean processMessage(String message) {
        return true; // Set this to false to test the DLQ mechanism, Also make sure to reduce the idleEventInterval
    }
}
