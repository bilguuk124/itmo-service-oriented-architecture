package itmo.mainservice.exception.handler;

import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.persistence.exceptions.ConversionException;

@Provider
@Produces(MediaType.APPLICATION_XML)
public class ConversionExceptionMapper implements ExceptionMapper<ConversionException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(ConversionException e) {
        return Response
                .status(400)
                .entity(errorBodyGenerator.generateValidationError(e.getInternalException().getMessage().substring(10) + " cannot be converted to " + e.getClassToConvertTo().getSimpleName()))
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}
