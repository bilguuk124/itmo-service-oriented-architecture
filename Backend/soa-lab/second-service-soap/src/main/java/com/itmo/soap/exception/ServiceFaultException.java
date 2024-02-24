package com.itmo.soap.exception;

import com.itmo.soap.entity.ErrorBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceFaultException extends RuntimeException{
    private ErrorBody errorBody;

    public ServiceFaultException(String message, Throwable cause, ErrorBody errorBody){
        super(message, cause);
        this.errorBody = errorBody;
    }
}
