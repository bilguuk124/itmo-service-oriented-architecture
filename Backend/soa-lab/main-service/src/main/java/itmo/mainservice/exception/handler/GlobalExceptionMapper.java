package itmo.mainservice.exception.handler;


import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(Throwable throwable) {
        return Response.ok().entity(errorBodyGenerator.generateInternalServerError(throwable)).build();
    }
}
