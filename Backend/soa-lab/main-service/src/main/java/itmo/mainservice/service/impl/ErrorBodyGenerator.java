package itmo.mainservice.service.impl;

import itmo.library.ErrorBody;
import itmo.mainservice.exception.NotCreatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ErrorBodyGenerator {

    private final Logger logger = LoggerFactory.getLogger(ErrorBodyGenerator.class);

    public ErrorBody generateFlatNotFoundError(int id){
        logger.info("Generating flat not found error body");
        return new ErrorBody(404, "Flat not found", "Flat with id = " + id + " was not found", LocalDateTime.now());
    }

    public ErrorBody generateValidationError(Integer id){
        logger.info("Generating validation error body");
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details("id can not be less than 1 (your input = " + id + ")")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateBadPageableError() {
        logger.info("Generating bad pageable error body");
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details("page and page size cannot be less than 1")
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

    public ErrorBody generateHouseNotFoundError(String name) {
        logger.info("Generating House not found exception error body");
        return new ErrorBody(404, "House not found", "House with name = " + name + " was not found", LocalDateTime.now());
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

    public ErrorBody generateTransactionError(NotCreatedException e) {
        logger.info("Generating Transaction exception error body");
        return ErrorBody.builder()
                .errorCode(500)
                .message("Transaction Exception")
                .details(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
