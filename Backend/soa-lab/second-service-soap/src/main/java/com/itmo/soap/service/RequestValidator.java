package com.itmo.soap.service;

import com.itmo.soap.entity.CheapestBalconyRequest;
import com.itmo.soap.entity.CheapestOfTwoRequest;
import com.itmo.soap.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

    private static final Logger logger = LoggerFactory.getLogger(RequestValidator.class);

    public static void validateCheapestBalconyRequest(CheapestBalconyRequest request) throws ValidationException {
        logger.info("Validating parameters of request");
        String cheapest = request.getCheapest();
        String balcony = request.getBalcony();

        if (cheapest == null ||
                (!cheapest.equalsIgnoreCase("cheapest")
                && !cheapest.equalsIgnoreCase("expensive"))){
            logger.error("Validation error! Parameter cheapest is wrong!");
            throw new ValidationException("Parameter cheapest is wrong!");
        }

        if (balcony == null ||
                (!balcony.equalsIgnoreCase("with-balcony") &&
        !balcony.equalsIgnoreCase("without-balcony"))){
            logger.error("Validation error! Parameter with balcony is wrong!");
            throw new ValidationException("Parameter with balcony is wrong!");
        }
    }

    public static void validateCheapestOfTwoRequest(CheapestOfTwoRequest request) throws ValidationException {
        logger.info("Validating parameters of request");
        long id1 = request.getId1();
        long id2 = request.getId2();
        logger.info("id1={}, id2={}", id1, id2);
        if (id1 <= 0){
            logger.error("Id1 must be a positive integer. Id1 = {}", id1);
            throw new ValidationException("Id1 must be a positive integer");
        }

        if (id2 <= 0){
            logger.error("Id2 must be a positive integer. Id2 = {}", id2);
            throw new ValidationException("Id2 must be a positive integer");
        }
    }
}
