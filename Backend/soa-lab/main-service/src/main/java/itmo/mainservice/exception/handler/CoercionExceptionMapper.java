package itmo.mainservice.exception.handler;

import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.type.descriptor.java.CoercionException;

@Provider
public class CoercionExceptionMapper implements ExceptionMapper<CoercionException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(CoercionException e) {
        return Response
                .status(400)
                .entity(errorBodyGenerator.generateValidationError(e.getMessage() + " " + e.getCause().getMessage().toLowerCase()))
                .build();
    }
}
