package com.itmo.mainspring.exception;


public class FlatNotFoundException extends Exception {
    public FlatNotFoundException(int id){
        super("Flat with id = " + id + " was not found!");
    }
}
