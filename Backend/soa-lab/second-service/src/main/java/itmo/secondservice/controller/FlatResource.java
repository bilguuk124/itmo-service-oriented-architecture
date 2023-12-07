package itmo.secondservice.controller;

import itmo.library.ErrorBody;
import itmo.library.Flat;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

@Path("/")
public class FlatResource {

    private final String BASE_URI = "http://localhost:16000/api";
    private final Client client = ClientBuilder.newClient();

    @GET
    @Path("/find-with-balcony/{cheapest}/{with-balcony}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCheapestOrExpensiveWithOrWithoutBalcony(@PathParam("cheapest") String cheapest,
                                                               @PathParam("with-balcony") String balcony){
        String path = "/find-with-balcony/" + cheapest + "/" + balcony;
        Flat flat = client
                .target(BASE_URI)
                .path(path)
                .request(MediaType.APPLICATION_XML)
                .get(Flat.class);
        if (flat == null) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorBody.builder().errorCode(500).message("Internal Server Error").details("").timestamp(LocalDateTime.now()).build())
                    .build();
        }
        return Response.ok(flat).build();
    }

    @GET
    @Path("/get-cheapest/{id1}/{id2}")
    public Response getCheaperOfTwo(@PathParam("id1") Integer id1, @PathParam("id2") Integer id2) {
        if (id1 == null || id1 < 1) throw new ValidationException();
        if (id2 == null || id2 < 1) throw new ValidationException();

        String path = "/get-cheapest/" + id1 + "/" + id2;

        Flat flat = client
                .target(BASE_URI)
                .path(path)
                .request(MediaType.APPLICATION_XML)
                .get(Flat.class);

        if (flat == null) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorBody.builder().errorCode(500).message("Internal Server Error").details("").timestamp(LocalDateTime.now()).build())
                    .build();
        }
        return Response
                .ok(flat)
                .build();
    }
}
