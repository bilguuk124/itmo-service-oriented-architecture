package itmo.mainservice.exception;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter

public class HouseExistsException extends Exception {

    public HouseExistsException(@NotEmpty String name) {
        super("House with this name exists: " + name);
    }
}
