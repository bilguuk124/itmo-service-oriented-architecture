package com.itmo.soap.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.soap.entity.ErrorBody;
import com.itmo.soap.exception.MainServiceException;
import com.itmo.soap.exception.ServiceFaultException;
import com.itmo.soap.service.RestResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String text = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));
        XmlMapper xmlMapper = new XmlMapper();
        ErrorBody errorBody = xmlMapper.readValue(text, ErrorBody.class);
        throw new MainServiceException("Main service returned an error", errorBody);
    }
}
