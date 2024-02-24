package com.itmo.soap.service;

import com.itmo.soap.entity.CheapestBalconyRequest;
import com.itmo.soap.entity.CheapestOfTwoRequest;
import com.itmo.soap.entity.Flat;
import com.itmo.soap.exception.MainServiceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AgencyService {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(AgencyService.class);
    private final String BASE_URL = "https://localhost:16000/api";

    public Flat getCheapestOrExpensiveWithOrWithoutBalcony(CheapestBalconyRequest request) throws Exception {
        logger.info("Service is running for cheapest balcony request");
        RequestValidator.validateCheapestBalconyRequest(request);
        String requestUrl = BASE_URL + "/find-with-balcony/"+ request.getCheapest().toLowerCase() + "/" + request.getBalcony().toLowerCase();
        ResponseEntity<?> entity = sendRequest(requestUrl);
        logger.info("First service response: {}", entity);
        return RestResponseParser.parse(entity);
    }

    public Flat getCheapestOfTwo(CheapestOfTwoRequest request) throws Exception {
        logger.info("Service is running for cheapest of two request");
        RequestValidator.validateCheapestOfTwoRequest(request);
        String requestUrl = BASE_URL + "/get-cheapest/" + request.getId1() + "/" + request.getId2();
        ResponseEntity<?> entity = sendRequest(requestUrl);
        logger.info("First service response: {}", entity);
        return RestResponseParser.parse(entity);
    }

    private ResponseEntity<?> sendRequest(String requestUrl) throws HttpClientErrorException{
        return restTemplate
                .exchange(
                        requestUrl,
                        HttpMethod.GET,
                        null,
                        Object.class
                );
    }



}
