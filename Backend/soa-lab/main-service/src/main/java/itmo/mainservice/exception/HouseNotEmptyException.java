package itmo.mainservice.exception;

public class HouseNotEmptyException extends Exception {
    public HouseNotEmptyException(Exception e) {
        super(e);
    }
}
