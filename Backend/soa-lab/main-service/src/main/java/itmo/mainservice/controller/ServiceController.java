package itmo.mainservice.controller;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.NoFlatsExistsException;
import itmo.mainservice.service.BonusService;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Path("/")
public class ServiceController {

    @Inject
    private BonusService bonusService;
    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @GET
    @Path("/find-with-balcony/{cheapest}/{with-balcony}")
    public Response getCheapestOrExpensiveWithOrWithoutBalconyFlat(@PathParam("cheapest") String cheapness, @PathParam("with-balcony") String balcony) throws NoFlatsExistsException {
        log.info("Got request to get cheapest or most expensive flat with or without balcony");
        try {
            if (
                    (cheapness == null || cheapness.isEmpty()) ||
                            (!Objects.equals(cheapness, "expensive") && !Objects.equals(cheapness, "cheapest"))
            ) {
                log.error("Bad Request parameter must be either 'cheapest' or 'expensive' (without '')");
                throw new IllegalArgumentException("Must be expensive or cheapest!");
            }

            if (
                    (balcony == null || balcony.isEmpty()) ||
                            (!Objects.equals(balcony, "with-balcony") && !Objects.equals(balcony, "without-balcony"))
            ) {
                log.error("Bad Request parameter must be either 'with-balcony' or 'without-balcony' (without '')");
                throw new IllegalArgumentException("Must be with-balcony or without-balcony");
            }

            Flat flat = bonusService.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
            log.info("Sending successful result");
            return Response
                    .ok(flat)
                    .build();

        } catch (IllegalArgumentException ex) {
            log.info("Returning error body for bad request");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(ex.getMessage()))
                    .build();
        } catch (HouseNotFoundException.NoMatchFoundException e) {
            log.info("Returning error body for no matching flat");
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateNoResultException(cheapness, balcony))
                    .build();
        }
    }

    @GET
    @Path("/get-cheapest/{id1}/{id2}")
    public Response getCheaperOfTwo(@PathParam("id1") Integer id1, @PathParam("id2") Integer id2) throws FlatNotFoundException {
        log.info("Got request to get cheaper of flats with id {} and {}", id1, id2);
        if (id1 == null || id1 < 1) throw new ValidationException("Id must be a positive integer");
        if (id2 == null || id2 < 1) throw new ValidationException("Id must be a positive integer");

        Flat flat = bonusService.getCheaperOfTwoFlats(id1, id2);
        return Response
                .ok(flat)
                .build();
    }
}
