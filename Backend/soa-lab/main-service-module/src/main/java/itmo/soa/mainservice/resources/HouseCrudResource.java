package itmo.soa.mainservice.resources;

import itmo.soa.library.House;
import itmo.soa.mainservice.exception.BadPageableException;
import itmo.soa.mainservice.service.HouseCrudService;
import itmo.soa.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/houses")
public class HouseCrudResource {
    @Inject
    private HouseCrudService service;

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @GET
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response getAllHousesFilteredAndSorted(@Context final HttpServletRequest request){
        String[] sortParameters = request.getParameterValues("sort");
        String[] filterParameters = request.getParameterValues("filter");
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");

        Integer page = null, pageSize = null;

        try{
            if (!pageParam.isEmpty()){
                page = Integer.parseInt(pageParam);
                if (page <= 0) throw new BadPageableException();
            }
            if(!pageSizeParam.isEmpty()){
                pageSize = Integer.parseInt(pageSizeParam);
                if(pageSize <= 0) throw new BadPageableException();
            }
        } catch (BadPageableException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorBodyGenerator.generateBadPageableError())
                    .build();
        }

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
        return Response
                .ok()
                .entity(resultPage)
                .build();
    }

}
