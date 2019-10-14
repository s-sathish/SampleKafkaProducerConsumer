package com.sathish.KPC.service;

import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService implements IMessageProcessingService {

    @Override
    public boolean processMessage(String message) {
        return false; // Set this to false to rest the DLQ mechanism
    }
}
