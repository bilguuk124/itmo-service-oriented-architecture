package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.MyValidationException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import itmo.mainservice.utility.CustomValidator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(BadRequestException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorBodyGenerator.generateValidationError(e.getCause().getCause().getCause().getMessage()))
                .build();
    }
}
