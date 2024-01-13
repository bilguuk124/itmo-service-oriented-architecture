package itmo.mainservice.exception.handler;

import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;
    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(NotFoundException e) {
        return Response
                .status(404)
                .entity(errorBodyGenerator.generateNotFoundException(e, request))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
