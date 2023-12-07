package itmo.mainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlatNotFoundException extends Exception {
    private Integer id;
}
