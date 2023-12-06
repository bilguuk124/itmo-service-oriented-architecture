package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.MyValidationException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.ok().entity(errorBodyGenerator.generateValidationError(new MyValidationException(e.getConstraintViolations()))).build();
    }
}
