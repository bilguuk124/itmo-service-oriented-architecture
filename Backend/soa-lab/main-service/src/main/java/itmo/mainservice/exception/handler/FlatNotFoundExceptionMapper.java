package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FlatNotFoundExceptionMapper implements ExceptionMapper<FlatNotFoundException> {
    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(FlatNotFoundException e) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorBodyGenerator.generateFlatNotFoundError(e.getId()))
                .build();
    }
}
