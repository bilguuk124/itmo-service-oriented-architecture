package com.itmo.mainspring.exception;

public class HouseNotEmptyException extends Exception {
    public HouseNotEmptyException(Exception e) {
        super(e);
    }
}
