package com.itmo.soap.exception;

import com.itmo.soap.entity.ErrorBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class CustomSoapExceptionResolver extends SoapFaultMappingExceptionResolver {
    private static final QName ERROR_CODE = new QName("errorCode");
    private static final QName MESSAGE = new QName("message");
    private static final QName DETAILS = new QName("details");
    private final Logger logger = LoggerFactory.getLogger(CustomSoapExceptionResolver.class);

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault){
        logger.info("Handling error: exception {} message:{}", ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage());
        if (ex instanceof ServiceFaultException){
            ErrorBody errorBody = ((ServiceFaultException) ex).getErrorBody();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(ERROR_CODE).addText(String.valueOf(errorBody.getErrorCode()));
            detail.addFaultDetailElement(MESSAGE).addText(errorBody.getMessage());
            detail.addFaultDetailElement(DETAILS).addText(errorBody.getDetails());
        }
    }
}
