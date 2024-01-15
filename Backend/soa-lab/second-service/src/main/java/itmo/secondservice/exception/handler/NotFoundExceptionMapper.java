package itmo.secondservice.exception.handler;

import itmo.library.ErrorBody;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Inject
    private HttpServletRequest request;

    @Override
    public Response toResponse(NotFoundException e) {
        return Response
                .status(404)
                .entity(ErrorBody.builder()
                        .errorCode(404)
                        .message("Resource not found")
                        .details("Following resources was not found: " + request.getRequestURL())
                        .timestamp(LocalDateTime.now())
                        .build())
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
