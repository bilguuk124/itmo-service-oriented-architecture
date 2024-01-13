package itmo.mainservice.exception.handler;


import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
@Produces(MediaType.APPLICATION_XML)
public class ZGlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(Throwable throwable) {
        return Response
                .status(500)
                .entity(errorBodyGenerator.generateInternalServerError(throwable))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
