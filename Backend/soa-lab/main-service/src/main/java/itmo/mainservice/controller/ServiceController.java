package itmo.mainservice.controller;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NoFlatsExistsException;
import itmo.mainservice.service.BonusService;
import jakarta.inject.Inject;
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

    @GET
    @Path("/find-with-balcony/{cheapest}/{with-balcony}")
    public Response getCheapestOrExpensiveWithOrWithoutBalconyFlat(@PathParam("cheapest") String cheapness, @PathParam("with-balcony") String balcony) throws NoFlatsExistsException {
        if (
                (cheapness == null || cheapness.isEmpty()) ||
                (!Objects.equals(cheapness, "expensive") && !Objects.equals(cheapness, "cheapest"))
        ) throw new IllegalArgumentException();

        if (
                (balcony == null || balcony.isEmpty()) ||
                (!Objects.equals(balcony,"with-balcony") && !Objects.equals(balcony, "without-balcony"))
        ) throw new IllegalArgumentException();
        Flat flat = bonusService.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
        return Response
                .ok(flat)
                .build();
    }

    @GET
    @Path("/get-cheapest/{id1}/{id2}")
    public Response getCheaperOfTwo(@PathParam("id1") Integer id1, @PathParam("id2") Integer id2) throws FlatNotFoundException {
        if (id1 == null || id1 < 1) throw new ValidationException();
        if (id2 == null || id2 < 1) throw new ValidationException();

        Flat flat = null;
        flat = bonusService.getCheaperOfTwoFlats(id1, id2);
        return Response
                .ok(flat)
                .build();
    }
}
