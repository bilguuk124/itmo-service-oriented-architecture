package itmo.mainservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.xml.bind.ValidationException;

import java.util.Set;

public class MyValidationException extends Exception {

    private final Set<? extends ConstraintViolation<?>> violations;

    public MyValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this.violations = violations;
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
}
