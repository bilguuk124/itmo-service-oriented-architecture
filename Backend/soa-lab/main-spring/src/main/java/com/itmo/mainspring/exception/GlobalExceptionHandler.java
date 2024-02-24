package com.itmo.mainspring.exception;

import com.itmo.mainspring.service.impl.ErrorBodyGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorBodyGenerator generator;

    @ExceptionHandler(FlatNotFoundException.class)
    public ResponseEntity<?> handleFlatNotFoundException(FlatNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(generator.generateFlatNotFoundError(e.getMessage()));
    }

    @ExceptionHandler(BadPageableException.class)
    public ResponseEntity<?> handleBadPageableException(BadPageableException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateBadPageableError(e));
    }

    @ExceptionHandler(HouseExistsException.class)
    public ResponseEntity<?> handleHouseExistsException(HouseExistsException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateHouseExistsException(e));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParameterException(MissingServletRequestParameterException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateMissingParameter(e));
    }

    @ExceptionHandler(HouseNotEmptyException.class)
    public ResponseEntity<?> handleHouseNotEmptyException(HouseNotEmptyException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateHouseNotEmptyException(e));
    }

    @ExceptionHandler(HouseNotFoundException.class)
    public ResponseEntity<?> handleHouseNotFoundException(HouseNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(generator.generateHouseNotFoundError(e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateValidationError(e.getMessage()));
    }

//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<?> handleNotFoundException(NoResourceFoundException e, HttpServletRequest request){
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(generator.generateNotFoundException(e,request));
//    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateValidationError( "Parameters must be a number: " + e.getMessage().substring(17)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generator.generateIllegalArgumentException(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleDefaultException(Throwable e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(generator.generateInternalServerError(e));
    }


}
