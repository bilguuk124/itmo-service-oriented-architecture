package itmo.mainservice.exception;

import lombok.Getter;

@Getter
public class HouseNotFoundException extends Exception {
    private final String houseName;

    public HouseNotFoundException(String houseName){
        this.houseName = houseName;
    }
}
