package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_XML)

public class HouseNotFoundExceptionMapper implements ExceptionMapper<HouseNotFoundException> {
    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(HouseNotFoundException e) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorBodyGenerator.generateHouseNotFoundError(e.getMessage()))
                .build();
    }
}
