package itmo.mainservice.controller;

import itmo.library.*;
import itmo.mainservice.exception.BadPageableException;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
import itmo.mainservice.service.FlatCrudService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        return Response.ok(new Flat(1, "Hrus", new Coordinates(1,2), LocalDate.now()
                ,100,3, Furnish.BAD, View.NORMAL, Transport.NORMAL, new House("hell",2,3),200L, false)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllFlats(@Context final HttpServletRequest request) throws BadPageableException {
        logger.info("Received a request to get all flats");
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        Integer page = null, pageSize = null;
        logger.info("page = " + pageParam + " pageSize = " + pageSizeParam + " \nsort = " + Arrays.toString(sortParameters) + " \nfilter = " + Arrays.toString(filterParameters));

        if (pageParam != null && !pageParam.isEmpty()){
            page = Integer.parseInt(pageParam);
            if(page <= 0) throw new BadPageableException();
        }
        if (pageSizeParam != null && !pageSizeParam.isEmpty()){
            pageSize = Integer.parseInt(pageSizeParam);
            if (pageSize <= 0) throw new BadPageableException();
        }

        List<String> sort = (sortParameters == null)
                ? new ArrayList<>()
                : Stream.of(sortParameters).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> filter = (filterParameters == null)
                ? new ArrayList<>()
                : Stream.of(filterParameters).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<Flat> resultPage = service.getAllFlats(sort, filter, page, pageSize);
        logger.info("Sending result, result =" + resultPage.toString());
        GenericEntity<List<Flat>> entity = new GenericEntity<>(resultPage){};
        return Response
                .ok(entity, MediaType.APPLICATION_XML)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getFlatById(@PathParam("id") Integer id){
        try {
            if (id <= 0) throw new ValidationException();
            return Response
                    .ok()
                    .entity(service.getFlatByID(id))
                    .build();
        } catch (FlatNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateFlatNotFoundError(id))
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
    public Response createFlat(FlatCreateDTO flatCreateDTO)  {
        try{
            Flat result = service.createFlat(flatCreateDTO);
            return Response
                    .ok()
                    .entity(result)
                    .type(MediaType.APPLICATION_XML)

                    .build();
        } catch (NotCreatedException e) {
            return Response
                    .status(400)
                    .entity(errorBodyGenerator.generateTransactionError(e))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteFlatById(@PathParam("id") Integer id){
        try{
            if (id <= 0 ) throw new ValidationException();
            service.deleteById(id);
            return Response
                    .ok()
                    .build();
        } catch (FlatNotFoundException ex){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateFlatNotFoundError(id))
                    .build();
        } catch (ValidationException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(id))
                    .build();
        }

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateFlatById(@PathParam("id") Integer id, FlatCreateDTO dto){
        try{
            if (id <= 0) throw new ValidationException();
            Flat result = service.updateFlatById(id, dto);
            return Response
                    .ok()
                    .entity(result)
                    .build();
        } catch (FlatNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateFlatNotFoundError(id))
                    .build();
        } catch (ValidationException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(id))
                    .build();
        }
    }
}
