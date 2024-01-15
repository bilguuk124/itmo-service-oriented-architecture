package itmo.mainservice.exception;

import lombok.Getter;

@Getter
public class HouseNotFoundException extends Exception {

    public HouseNotFoundException(String message) {
        super(message);
    }

    public static class NoMatchFoundException extends Exception {
        public NoMatchFoundException(String message) {
            super(message);
        }
    }
}
