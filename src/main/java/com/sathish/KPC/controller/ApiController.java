package com.sathish.KPC.controller;

import com.sathish.KPC.dto.ApiRequestDTO;
import com.sathish.KPC.dto.ProducerDTO;
import com.sathish.KPC.dto.ApiResponseDTO;
import com.sathish.KPC.messaging.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.sathish.KPC.utils.Constants.API_SUCCESS_RESPONSE_MESSAGE;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ApiController {

    @Autowired
    private Producer producer;

    @PostMapping(value = "/add/data", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDTO> handleApiRequest(final @RequestBody ApiRequestDTO apiRequest) {
        doLogInfoWithMessageAndObject("Received API request = {}", apiRequest);

        produceMessage(prepareProducerData(apiRequest));

        return new ResponseEntity<>(ApiResponseDTO.builder().status(API_SUCCESS_RESPONSE_MESSAGE).build(), HttpStatus.OK);
    }

    private void produceMessage(ProducerDTO producerData) {
        producer.messageProducer(producerData);
    }

    private ProducerDTO prepareProducerData(ApiRequestDTO apiRequest) {
        ProducerDTO producerData = new ProducerDTO();
        producerData.setPayload(apiRequest.getData());

        return producerData;
    }
}
