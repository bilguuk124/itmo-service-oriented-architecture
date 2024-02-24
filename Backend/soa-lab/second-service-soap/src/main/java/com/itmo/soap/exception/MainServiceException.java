package com.itmo.soap.exception;

import com.itmo.soap.entity.ErrorBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainServiceException extends RuntimeException{
    private ErrorBody errorBody;
    public MainServiceException(String message, ErrorBody errorBody){
        super(message);
        this.errorBody = errorBody;
    }
}
