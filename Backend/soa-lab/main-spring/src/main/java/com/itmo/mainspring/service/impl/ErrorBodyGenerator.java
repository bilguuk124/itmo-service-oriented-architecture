package com.itmo.mainspring.service.impl;

import com.itmo.feignclient.entity.ErrorBody;
import com.itmo.mainspring.exception.BadPageableException;
import com.itmo.mainspring.exception.HouseExistsException;
import com.itmo.mainspring.exception.HouseNotEmptyException;
import com.itmo.mainspring.exception.JpaException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@Component
public class ErrorBodyGenerator {

    private final Logger logger = LoggerFactory.getLogger(ErrorBodyGenerator.class);

    public ErrorBody generateFlatNotFoundError(String message) {
        logger.info("Generating flat not found error body");
        return new ErrorBody(
                404,
                "Flat not found",
                message,
                LocalDateTime.now());
    }

    public ErrorBody generateBadPageableError(BadPageableException exception) {
        logger.info("Generating bad pageable error body");
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details(exception.getMessage() != null ? exception.getMessage() : "Bad pageable")
                .timestamp(LocalDateTime.now())
                .build();
    }


    public ErrorBody generateValidationError(String message) {
        logger.info("Generating Validation error error body");
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateHouseNotFoundError(String message) {
        logger.info("Generating House not found exception error body");
        return new ErrorBody(404, "Not found", message, LocalDateTime.now());
    }

    public ErrorBody generateServletError(String servletName, Integer statusCode, String message) {
        logger.info("Generating default servlet exception error body");
        return ErrorBody.builder()
                .errorCode(statusCode)
                .message("Servlet error: " + servletName)
                .details(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateTransactionError(JpaException e) {
        logger.warn("Transaction error has occurred" + e.getMessage());
        logger.info("Generating Transaction exception error body");
        return ErrorBody.builder()
                .errorCode(500)
                .message("Transaction Exception")
                .details(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateInternalServerError(Throwable e) {
        logger.warn("Unknown Error has occurred" + e.getMessage());
        logger.info("Generating Unknown exception error body");
        return ErrorBody.builder()
                .errorCode(500)
                .message(e.getClass().getSimpleName())
                .details(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateHouseExistsException(HouseExistsException e) {
        logger.warn("Already exists" + e.getMessage());
        logger.info("Generating HouseExists exception error body");
        return ErrorBody.builder()
                .errorCode(405)
                .message("Bad Request")
                .details("House with specified name already exists")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateNoResultException(String cheapness, String balcony) {
        logger.warn("No result for {} and {}", cheapness, balcony);
        logger.info("Generating No result exception error body");
        return ErrorBody.builder()
                .errorCode(400)
                .message("No content")
                .details("No result for " + cheapness + " and " + balcony)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public ErrorBody generateHouseNotEmptyException(HouseNotEmptyException e) {
        return ErrorBody.builder()
                .errorCode(400)
                .message("House is not empty")
                .details("House was not removed because it is not empty")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateNotFoundException(NoResourceFoundException e, HttpServletRequest request) {
        return ErrorBody.builder()
                .errorCode(404)
                .message("Resource not found")
                .details("Resource on this path does not exist: " + request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateMissingParameter(MissingServletRequestParameterException e) {
        return ErrorBody.builder()
                .errorCode(400)
                .message("Bad request")
                .details(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateIllegalArgumentException(IllegalArgumentException e) {
        return ErrorBody.builder()
                .errorCode(400)
                .message("Bad request")
                .details(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateNoFlatExistsException(String message) {
        return ErrorBody.builder()
                .errorCode(400)
                .message("No flats exists")
                .details(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
