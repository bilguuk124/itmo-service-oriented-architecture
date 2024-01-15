package itmo.mainservice.exception;

import jakarta.validation.constraints.NotEmpty;


public class HouseExistsException extends Exception {

    public HouseExistsException(@NotEmpty String name) {
        super("House with this name exists: " + name);
    }
}
