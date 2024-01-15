package itmo.secondservice.exception.handler;

import itmo.library.ErrorBody;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class ZGlobalExceptionHandler implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(500)
                .entity(ErrorBody.builder()
                        .errorCode(500)
                        .message(throwable.getClass().getSimpleName())
                        .details(throwable.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
