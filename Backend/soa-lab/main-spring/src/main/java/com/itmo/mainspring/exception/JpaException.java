package com.itmo.mainspring.exception;

public class JpaException extends Exception {
    public JpaException(Exception message) {
        super(message);
    }
}
