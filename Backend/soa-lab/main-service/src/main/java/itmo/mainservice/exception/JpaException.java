package itmo.mainservice.exception;

public class JpaException extends Exception {
    public JpaException(Exception message){
        super(message);
    }
}
