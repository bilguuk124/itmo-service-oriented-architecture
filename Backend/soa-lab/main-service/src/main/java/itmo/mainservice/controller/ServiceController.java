package itmo.mainservice.controller;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NoFlatsExistsException;
import itmo.mainservice.service.BonusService;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import itmo.mainservice.service.impl.NoMatchFoundException;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("/")
public class ServiceController {

    @Inject
    private BonusService bonusService;
    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @GET
    @Path("/find-with-balcony/{cheapest}/{with-balcony}")
    public Response getCheapestOrExpensiveWithOrWithoutBalconyFlat(@PathParam("cheapest") String cheapness, @PathParam("with-balcony") String balcony) throws NoFlatsExistsException {
        try{
            if (
                    (cheapness == null || cheapness.isEmpty()) ||
                    (!Objects.equals(cheapness, "expensive") && !Objects.equals(cheapness, "cheapest"))
            ) throw new IllegalArgumentException("Must be expensive or cheapest!");

            if (
                    (balcony == null || balcony.isEmpty()) ||
                    (!Objects.equals(balcony,"with-balcony") && !Objects.equals(balcony, "without-balcony"))
            ) throw new IllegalArgumentException("Must be with-balcony or without-balcony");

            Flat flat = bonusService.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
            return Response
                    .ok(flat)
                    .build();

        } catch (IllegalArgumentException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(ex.getMessage()))
                    .build();
        } catch (NoMatchFoundException e) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(errorBodyGenerator.generateNoResultException(cheapness, balcony))
                    .build();
        }
    }

    @GET
    @Path("/get-cheapest/{id1}/{id2}")
    public Response getCheaperOfTwo(@PathParam("id1") Integer id1, @PathParam("id2") Integer id2) throws FlatNotFoundException {
        if (id1 == null || id1 < 1) throw new ValidationException();
        if (id2 == null || id2 < 1) throw new ValidationException();

        Flat flat = bonusService.getCheaperOfTwoFlats(id1, id2);
        return Response
                .ok(flat)
                .build();
    }
}
