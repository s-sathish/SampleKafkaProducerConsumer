package com.sathish.KPC.api;

import com.sathish.KPC.data.ApiRequest;
import com.sathish.KPC.data.ProducerData;
import com.sathish.KPC.data.ResponseData;
import com.sathish.KPC.producer.Producer;
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
public class Api {

    @Autowired
    private Producer producer;

    @PostMapping(value = "/add/data", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData> handleApiRequest(final @RequestBody ApiRequest apiRequest) {
        doLogInfoWithMessageAndObject("Received API request = {}", apiRequest);

        produceMessage(prepareProducerData(apiRequest));

        return new ResponseEntity<>(ResponseData.builder().status(API_SUCCESS_RESPONSE_MESSAGE).build(), HttpStatus.OK);
    }

    private void produceMessage(ProducerData producerData) {
        producer.messageProducer(producerData);
    }

    private ProducerData prepareProducerData(ApiRequest apiRequest) {
        ProducerData producerData = new ProducerData();
        producerData.setPayload(apiRequest.getData());

        return producerData;
    }
}
