package itmo.soa.mainservice.resources;

import itmo.soa.library.ErrorBody;
import itmo.soa.library.Flat;
import itmo.soa.library.FlatCreateDTO;
import itmo.soa.mainservice.exception.BadPageableException;
import itmo.soa.mainservice.exception.FlatNotFoundException;
import itmo.soa.mainservice.service.FlatCrudService;
import itmo.soa.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/flats")
public class FlatCrudResource {
    @Inject
    private FlatCrudService service;

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @GET
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response getAllFlats(@Context final HttpServletRequest request){
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        Integer page = null, pageSize = null;

        try{
            if (!pageParam.isEmpty()){
                page = Integer.parseInt(pageParam);
                if(page <= 0) throw new BadPageableException();
            }
            if (!pageSizeParam.isEmpty()){
                pageSize = Integer.parseInt(pageSizeParam);
                if (pageSize <= 0) throw new BadPageableException();
            }
        } catch (BadPageableException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateBadPageableError())
                    .build();
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
        return Response
                .ok()
                .entity(resultPage)
                .build();
    }

    @GET
    @Path("/{id}")
    @Consumes("application/xml")
    @Produces("application/xml")
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
                    .entity(errorBodyGenerator.getFlatNotFoundError(id))
                    .build();
        } catch (ValidationException e){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(errorBodyGenerator.generateValidationError(id))
                    .build();
        }
    }

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response createFlat(@Valid FlatCreateDTO flatCreateDTO){
        try{
            Flat result = service.createFlat(flatCreateDTO);
            return Response
                    .ok()
                    .entity(result)
                    .build();
        }
        catch (ConstraintViolationException e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(e))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes("application/xml")
    @Produces("application/xml")
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
                    .entity(errorBodyGenerator.getFlatNotFoundError(id))
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
    @Consumes("application/xml")
    @Produces("application/xml")
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
                    .entity(errorBodyGenerator.getFlatNotFoundError(id))
                    .build();
        } catch (ValidationException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateValidationError(id))
                    .build();
        }
    }
}
