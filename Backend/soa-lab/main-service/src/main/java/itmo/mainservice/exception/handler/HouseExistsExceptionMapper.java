package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.HouseExistsException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HouseExistsExceptionMapper implements ExceptionMapper<HouseExistsException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(HouseExistsException e) {
        return Response
                .status(Response.Status.METHOD_NOT_ALLOWED)
                .entity(errorBodyGenerator.generateHouseExistsException(e))
                .build();
    }
}
