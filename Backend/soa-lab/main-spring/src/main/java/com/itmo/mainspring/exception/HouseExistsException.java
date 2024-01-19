package com.itmo.mainspring.exception;

import javax.validation.constraints.NotEmpty;


public class HouseExistsException extends Exception {

    public HouseExistsException(@NotEmpty String name) {
        super("House with this name exists: " + name);
    }
}
