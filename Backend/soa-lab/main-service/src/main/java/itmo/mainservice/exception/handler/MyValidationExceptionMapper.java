package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.MyValidationException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.ValidationException;

@Provider
public class MyValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(ValidationException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorBodyGenerator.generateValidationError(2))
                .build();
    }
}
