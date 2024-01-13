package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.JpaException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_XML)

public class JpaExceptionMapper implements ExceptionMapper<JpaException> {
    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(JpaException e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorBodyGenerator.generateTransactionError(e))
                .build();
    }
}
