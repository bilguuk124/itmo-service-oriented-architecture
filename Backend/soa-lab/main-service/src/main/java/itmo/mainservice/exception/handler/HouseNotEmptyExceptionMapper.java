package itmo.mainservice.exception.handler;

import itmo.mainservice.exception.HouseNotEmptyException;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Produces(MediaType.APPLICATION_XML)

public class HouseNotEmptyExceptionMapper implements ExceptionMapper<HouseNotEmptyException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;
    private final Logger logger = LoggerFactory.getLogger(HouseNotEmptyExceptionMapper.class);

    @Override
    public Response toResponse(HouseNotEmptyException e) {
        logger.warn("Generating house was not empty exception response");
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorBodyGenerator.generateHouseNotEmptyException(e))
                .build();
    }
}
