package itmo.soa.mainservice.service.impl;

import itmo.soa.library.ErrorBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;

public class ErrorBodyGenerator {

    public ErrorBody getFlatNotFoundError(int id){
        return new ErrorBody(404, "Flat not found", "Flat with id = " + id + " was not found", LocalDateTime.now());
    }

    public ErrorBody generateValidationError(ConstraintViolationException ex) {
        StringBuilder details = new StringBuilder("<errors>");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            details.append("<error>")
                    .append("<propertyPath>").append(violation.getPropertyPath()).append("</propertyPath>")
                    .append("<invalidValue>").append(violation.getInvalidValue()).append("</invalidValue>")
                    .append("<message>").append(violation.getMessage()).append("</message>")
                    .append("</error>");
        }

        details.append("</errors>");

        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details(details.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateValidationError(Integer id){
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details("id can not be less than 1 (your input = " + id + ")")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ErrorBody generateBadPageableError() {
        return ErrorBody.builder()
                .errorCode(400)
                .message("Validation Error")
                .details("page and page size cannot be less than 1")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
