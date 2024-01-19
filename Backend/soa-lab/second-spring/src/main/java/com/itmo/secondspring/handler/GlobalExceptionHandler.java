package com.itmo.secondspring.handler;

import com.itmo.feignclient.entity.ErrorBody;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<?> handleNotFoundException(NoResourceFoundException e){
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .contentType(MediaType.APPLICATION_XML)
//                .body(
//                        ErrorBody.builder()
//                                .errorCode(404)
//                                .message("Not Found")
//                                .details("Resource was not found: " + e.getResourcePath())
//                                .timestamp(LocalDateTime.now())
//                                .build()
//                );
//    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleDefaultException(Throwable e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_XML)
                .body(
                        ErrorBody.builder()
                                .errorCode(500)
                                .message(e.getClass().getCanonicalName())
                                .details(e.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }
}
