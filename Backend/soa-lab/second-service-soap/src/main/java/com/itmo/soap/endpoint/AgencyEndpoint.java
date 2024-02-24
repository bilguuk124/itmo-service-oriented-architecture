package com.itmo.soap.endpoint;

import com.itmo.soap.entity.*;
import com.itmo.soap.exception.MainServiceException;
import com.itmo.soap.exception.ServiceFaultException;
import com.itmo.soap.exception.ValidationException;
import com.itmo.soap.service.AgencyService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
@AllArgsConstructor
public class AgencyEndpoint {

    private final AgencyService service;
    private final Logger logger = LoggerFactory.getLogger(AgencyEndpoint.class);
    private final String NAMESPACE =  "http://www.itmo.com/soa";

    @ResponsePayload
    @PayloadRoot(localPart = "CheapestBalconyRequest", namespace = NAMESPACE)
    public CheapestBalconyResponse getCheapestOrExpensiveWithOrWithoutBalcony(@RequestPayload CheapestBalconyRequest request) throws ServiceFaultException {
        try{
            logger.info("New request to get cheapest or expensive, with or without balcony flat");
            Flat flat = service.getCheapestOrExpensiveWithOrWithoutBalcony(request);
            logger.info("Response: Flat: [{},{},{},{},{}]", flat.getId(), flat.getName(), flat.getArea(), flat.getCreationDate(), flat.getNumberOfRooms());
            CheapestBalconyResponse response = new CheapestBalconyResponse();
            response.setFlat(flat);
            return response;
        } catch (ValidationException e){
            logger.error(e.getMessage());
            throw new ServiceFaultException("Validation error", e ,getBadRequestBody(e));
        } catch (MainServiceException e){
            logger.error(e.getMessage());
            throw new ServiceFaultException("No flat was found", e, e.getErrorBody());
        } catch (Exception e){
            logger.error(e.getClass().getSimpleName());
            logger.error(e.getMessage());
            throw new ServiceFaultException("Main service is down", e, getInternalServerErrorBody());
        }

    }

    @ResponsePayload
    @PayloadRoot(localPart = "CheapestOfTwoRequest", namespace = NAMESPACE)
    public CheapestOfTwoResponse getCheapestOfTwoFlats(@RequestPayload CheapestOfTwoRequest request) throws ServiceFaultException {
        try{
            logger.info("New request to get cheaper of two flats");
            Flat flat = service.getCheapestOfTwo(request);
            logger.info("Response: Flat: [{},{},{},{},{}]", flat.getId(), flat.getName(), flat.getArea(), flat.getCreationDate(), flat.getNumberOfRooms());
            CheapestOfTwoResponse response = new CheapestOfTwoResponse();
            response.setFlat(flat);
            return response;
        } catch (ValidationException e){
            logger.error(e.getMessage());
            throw new ServiceFaultException("Validation error", e, getBadRequestBody(e));
        } catch (MainServiceException e){
            logger.error(e.getMessage());
            throw new ServiceFaultException("No flat was found", e, e.getErrorBody());
        }
        catch (Exception e){
            logger.error(e.getClass().getSimpleName());
            logger.error(e.getMessage());
            throw new ServiceFaultException("Main service is down", e, getInternalServerErrorBody());
        }
    }

    private ErrorBody getBadRequestBody(ValidationException e){
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrorCode(400);
        errorBody.setMessage("Bad Request");
        errorBody.setDetails(e.getMessage());
        return errorBody;
    }

    private ErrorBody getInternalServerErrorBody(){
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrorCode(500);
        errorBody.setMessage("Internal Server Error");
        errorBody.setDetails("Main service is down");
        return errorBody;
    }

}
