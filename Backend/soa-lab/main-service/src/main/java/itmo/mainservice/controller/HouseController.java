package itmo.mainservice.controller;

import itmo.library.House;
import itmo.library.HousePageableResponse;
import itmo.mainservice.exception.BadPageableException;
import itmo.mainservice.exception.HouseExistsException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import itmo.mainservice.service.HouseCrudService;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Path("/houses")
public class HouseController {

    @Inject
    private HouseCrudService service;

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    private final Logger logger = LoggerFactory.getLogger(HouseController.class);

    @GET
    @Path("/aaa")
    @Produces(MediaType.APPLICATION_XML)
    public Response get(@Context final HttpServletRequest request){
        logger.info("Got test request");
        House house = new House("hello", 2, 3);
        List<House> houses = new ArrayList<>();
        houses.add(house);
        GenericEntity<List<House>> entity = new GenericEntity<>(houses){};
        return Response
                .ok(entity)
                .build();
    }

    @POST
    @Path("/aaa")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response post(House house)  {
        logger.info("Testing post method");
        return Response.ok(house).build();
    }


    @GET
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllHousesFilteredAndSorted(@QueryParam("page") Integer page,
                                                  @QueryParam("pageSize") Integer pageSize,
                                                  @QueryParam("sort") String sortParam,
                                                  @QueryParam("filter") String filterParam) throws BadPageableException {
        logger.info("Got request to get all houses");

        if (page != null && page <= 0) throw new BadPageableException();
        if (page != null && pageSize <= 0) throw new BadPageableException();

        List<String> sort = (sortParam == null)
                ? new ArrayList<>()
                : Stream.of(sortParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();
        List<String> filter = (filterParam == null)
                ? new ArrayList<>()
                : Stream.of(filterParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();

        logger.info("page = {}, pageSize = {}, sort = {} filter = {}", page, pageSize, Arrays.toString(sort.toArray()), Arrays.toString(filter.toArray()));

        HousePageableResponse response = service.getAllHousesFilteredAndSorted(sort, filter, page, pageSize);
        logger.info("Successfully processed the request");
        return Response
                .ok(response, MediaType.APPLICATION_XML)
                .build();
    }



    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response createHouse(House house) throws HouseExistsException, JpaException {
        logger.info("Got request to create a new house");
        House result = service.createHouse(house);
        return Response
                .ok(result, MediaType.APPLICATION_XML)
                .build();
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteHouseByName(@PathParam("name") String name) throws JpaException, HouseNotFoundException {
        logger.info("Got request to delete a house with name = {}", name);
        try {
            if (name == null || name.isEmpty()) throw new ValidationException();
            service.deleteByName(name);
            return Response.ok().build();
        }
        catch (ValidationException e){
            logger.warn("Validation error");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(name))
                    .build();
        }
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getHouseByName(@PathParam("name") String name) throws HouseNotFoundException {
        logger.info("Got request to get a house with name = {}", name);
        return Response
                .ok(service.getHouseByName(name))
                .build();
    }

    @PUT
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response updateHouseByName(@PathParam("name") String name, @QueryParam("year") Integer newYear, @QueryParam("numberOfFloors") Integer newNumberOfFloors) throws JpaException, HouseNotFoundException {
        logger.info("Got request to update a house with name = {}", name);
        try{
            if (name == null || name.isEmpty()) throw new ValidationException("Name of the house can not be empty!");
            if (newYear == null || newYear <= 0 || newYear > 634) throw new ValidationException("Year of the house must be > 0 and <= 634!");
            if (newNumberOfFloors == null || newNumberOfFloors <= 0) throw new ValidationException("Number of floors must be greater than 0!");
            House result = service.updateHouseByName(name, newYear, newNumberOfFloors);
            return Response
                    .ok(result, MediaType.APPLICATION_XML)
                    .build();
        } catch (ValidationException e){
            logger.warn("Validation error");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(name))
                    .build();
        }
    }
}
