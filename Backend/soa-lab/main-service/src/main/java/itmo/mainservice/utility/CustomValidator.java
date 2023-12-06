package itmo.mainservice.utility;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class CustomValidator {
    @Inject
    private Validator validator;

    public <T> boolean isValid(T element){
        Set<ConstraintViolation<T>> violations = validator.validate(element);
        return violations.isEmpty();
    }
}
