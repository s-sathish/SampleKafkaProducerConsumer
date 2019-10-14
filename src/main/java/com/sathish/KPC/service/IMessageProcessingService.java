package com.sathish.KPC.service;

public interface IMessageProcessingService {
    /**
     * Process the received message
     * @param message Received message - String
     * @return Return value of the processing logic - True/False
     */
    boolean processMessage(String message);
}
