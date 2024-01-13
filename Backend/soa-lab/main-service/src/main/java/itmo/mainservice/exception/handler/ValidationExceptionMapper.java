package itmo.mainservice.exception.handler;

import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_XML)

public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;


    @Override
    public Response toResponse(ValidationException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorBodyGenerator.generateValidationError(e.getMessage()))
                .build();
    }
}
