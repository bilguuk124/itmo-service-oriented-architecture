package itmo.mainservice.controller;

import itmo.library.*;
import itmo.mainservice.exception.BadPageableException;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import itmo.mainservice.service.FlatCrudService;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import itmo.mainservice.service.impl.Validator;
import jakarta.inject.Inject;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Path("/flats")
public class FlatController {

    @Inject
    private FlatCrudService service;

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    private final Logger logger = LoggerFactory.getLogger(FlatController.class);

    @GET
    @Path("/aaa")
    @Produces(MediaType.APPLICATION_XML)
    public Response get(){
        return Response.ok(new Flat(1, "Hrus", new Coordinates(1D,2), LocalDate.now()
                ,100,3, Furnish.BAD, View.NORMAL, Transport.NORMAL, new House("hell",2,3),200L, false)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllFlats(@QueryParam("page") String pageStr,
                                @QueryParam("pageSize") String pageSizeStr,
                                @QueryParam("sort") String sortParam,
                                @QueryParam("filter") String filterParam) throws Exception {
        try{
            logger.info("Received a request to get all flats");
            int page = Validator.validatePageable(pageStr);
            int pageSize = Validator.validatePageable(pageSizeStr);

            List<String> sort = (sortParam == null)
                    ? new ArrayList<>()
                    : Stream.of(sortParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();
            List<String> filter = (filterParam == null)
                    ? new ArrayList<>()
                    : Stream.of(filterParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();

            logger.info("page = {}, pageSize = {}, sort = {} filter = {}", page, pageSize, Arrays.toString(sort.toArray()), Arrays.toString(filter.toArray()));

            FlatPageableResponse response = service.getAllFlats(sort, filter, page, pageSize);

            logger.info("Sending result, result =" + response.toString());
            return Response
                    .ok(response, MediaType.APPLICATION_XML)
                    .build();
        }
        catch (NumberFormatException e){
            throw new BadPageableException("Page and pageSize must be a number!");
        }
        catch (IllegalArgumentException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError("Opposite sorts!"))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getFlatById(@PathParam("id") Integer id) throws FlatNotFoundException {
        try {
            if (id <= 0) throw new ValidationException("Id must be a positive integer");
            return Response
                    .ok(service.getFlatByID(id))
                    .build();
        }
        catch (ValidationException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(id))
                    .build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createFlat(FlatCreateDTO flatCreateDTO) throws JpaException, HouseNotFoundException, ValidationException{
        Validator.validateNewFlatRequest(flatCreateDTO);
        Flat result = service.createFlat(flatCreateDTO);
        return Response
                .ok(result)
                .build();

    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteFlatByIdOrHouse(@PathParam("id") String param) throws JpaException, HouseNotFoundException, FlatNotFoundException {
        int id;
        try{
            if (param == null || param.isEmpty()) throw new ValidationException("Id must not be empty!");
            id = Integer.parseInt(param);
            service.deleteById(id);
            return Response
                    .ok()
                    .build();

        } catch (NumberFormatException ex){
            service.deleteFlatsOfTheHouse(param);
            return Response.ok().build();

        }  catch (ValidationException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError("Method parameter can not be null!"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateFlatById(@PathParam("id") String idStr, FlatCreateDTO dto) throws FlatNotFoundException, ValidationException {
        try{
            if (idStr == null || idStr.isEmpty()) throw new ValidationException("Id cannot be empty");
            int id = Integer.parseInt(idStr);
            if (id <= 0) throw new ValidationException("Id must be positive integer");
            Validator.validateNewFlatRequest(dto);
            Flat result = service.updateFlatById(id, dto);
            return Response
                    .ok(result)
                    .build();
        } catch (NumberFormatException ex){
            throw new ValidationException("Id must be a positive number");
        }
    }

    @GET
    @Path("/{houseName}/count")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCountOfFlatsInTheSameHouse(@PathParam("houseName") String houseName) throws HouseNotFoundException {
        try{
            if (houseName == null || houseName.isEmpty()) throw new IllegalArgumentException();
            return Response
                    .ok(service.getFlatCountOfHouse(houseName))
                    .build();
        } catch (IllegalArgumentException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError("house name can not be empty!"))
                    .build();
        }
    }

    @GET
    @Path("/numberOfRooms/{numberOfRooms}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getFlatsWithRoomsLessThan(@PathParam("numberOfRooms") Integer numberOfRooms){
        try{
            if(numberOfRooms == null) throw new IllegalArgumentException("number of rooms can't be null");
            return Response.ok(service.getFlatCountWithLessRooms(numberOfRooms)).build();
        } catch (IllegalArgumentException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(e.getMessage()))
                    .build();
        }
    }
}
