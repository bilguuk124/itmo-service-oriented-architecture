package itmo.mainservice.controller;

import itmo.library.House;
import itmo.mainservice.exception.BadPageableException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
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
import java.util.List;
import java.util.stream.Collectors;
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
    public Response getAllHousesFilteredAndSorted(@Context final HttpServletRequest request) throws BadPageableException {
        logger.info("Got request to get all houses");
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");

        Integer page = null, pageSize = null;

        if (pageParam != null && !pageParam.isEmpty()){
            page = Integer.parseInt(pageParam);
            if(page <= 0) throw new BadPageableException();
        }
        if (pageSizeParam != null && !pageSizeParam.isEmpty()){
            pageSize = Integer.parseInt(pageSizeParam);
            if(pageSize <= 0) throw new BadPageableException();
        }
        logger.info("Page = {}, Page Size = {}", page, pageSize );


        List<String> sorts = (sortParameters == null)
                ? new ArrayList<>()
                : Stream.of(sortParameters)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> filters = (filterParameters == null)
                ? new ArrayList<>()
                : Stream.of(filterParameters)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<House> resultPage = service.getAllHousesFilteredAndSorted(sorts, filters, page, pageSize);
        GenericEntity<List<House>> entity = new GenericEntity<>(resultPage){};
        logger.info("Successfully processed the request");
        return Response
                .ok(entity, MediaType.APPLICATION_XML)
                .build();
    }



    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response createHouse(House house) {
        logger.info("Got request to create a new house");
        try{
            House result = service.createHouse(house);
            return Response
                    .ok(result, MediaType.APPLICATION_XML)
                    .build();
        } catch (NotCreatedException e) {
            logger.warn("House was not created due to transaction error");
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorBodyGenerator.generateTransactionError(e))
                    .build();
        }
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteHouseByName(@PathParam("name") String name){
        logger.info("Got request to delete a house with name = {}", name);
        try {
            if (name == null || name.isEmpty()) throw new ValidationException();
            service.deleteByName(name);
            return Response.ok().build();
        } catch (HouseNotFoundException e) {
            logger.warn("House with specified name ({}) was not found", name);
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateHouseNotFoundError(name))
                    .build();
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
    public Response getHouseByName(@PathParam("name") String name){
        logger.info("Got request to get a house with name = {}", name);
        try{
            return Response
                    .ok(service.getHouseByName(name))
                    .build();
        } catch (HouseNotFoundException e) {
            logger.warn("House with specified name ({}) was not found", name);
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateHouseNotFoundError(name))
                    .build();
        }
    }

    @PUT
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response updateHouseByName(@PathParam("name") String name, @Valid House house){
        logger.info("Got request to update a house with name = {}", name);
        try{
            if (name == null || name.isEmpty()) throw new ValidationException();
            House result = service.updateHouseByName(name, house);
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
        catch (HouseNotFoundException e){
            logger.warn("House with specified name ({}) was not found", name);
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateHouseNotFoundError(name))
                    .build();
        } catch (NotCreatedException e) {
            logger.warn("House was not updated due to transaction error");
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorBodyGenerator.generateTransactionError(e))
                    .build();
        }
    }
}
