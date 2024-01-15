package itmo.secondservice.exception.handler;

import itmo.library.ErrorBody;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class ProcessingExceptionHandler implements ExceptionMapper<ProcessingException> {
    @Override
    public Response toResponse(ProcessingException e) {
        return Response
                .status(500)
                .entity(
                        ErrorBody.builder()
                                .errorCode(500)
                                .message("Internal server error")
                                .details("Main service is down")
                                .timestamp(LocalDateTime.now())
                                .build()
                )
                .build();
    }
}
