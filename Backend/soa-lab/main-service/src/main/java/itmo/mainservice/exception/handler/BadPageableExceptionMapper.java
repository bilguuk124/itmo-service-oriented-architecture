package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.BadPageableException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadPageableExceptionMapper implements ExceptionMapper<BadPageableException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(BadPageableException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorBodyGenerator.generateBadPageableError())
                .build();
    }
}
