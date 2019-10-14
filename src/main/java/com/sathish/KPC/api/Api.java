package com.sathish.KPC.api;

import com.sathish.KPC.data.ApiRequest;
import com.sathish.KPC.data.ProducerData;
import com.sathish.KPC.data.ResponseData;
import com.sathish.KPC.producer.Producer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.sathish.KPC.utils.Constants.API_SUCCESS_RESPONSE_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Log4j2
public class Api {

    @Autowired
    private Producer producer;

    @PostMapping(value = "/add/data", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData> handleApiRequest(final @RequestBody ApiRequest apiRequest) {
        log.info("Received API request = {}", apiRequest);

        ProducerData producerData = new ProducerData();
        producerData.setPayload(apiRequest.getData());

        producer.messageProducer(producerData);

        return new ResponseEntity<>(ResponseData.builder().status(API_SUCCESS_RESPONSE_MESSAGE).build(), HttpStatus.OK);
    }
}
