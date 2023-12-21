package itmo.secondservice.controller;

import itmo.library.*;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Path("/agency")
public class FlatResource {

    private final String BASE_URI = "https://localhost:16000/api";

    private final Client client = ClientBuilder.newBuilder().sslContext(SSLUtil.getInsercureSSLContext()).build();

    private final Logger logger = LoggerFactory.getLogger(FlatResource.class);

    public FlatResource() throws NoSuchAlgorithmException, KeyManagementException {
    }


    @GET
    @Path("/aaa")
    @Produces(MediaType.APPLICATION_XML)
    public Response get(){
        return Response.ok(new Flat(1, "Hrus", new Coordinates(1,2), LocalDate.now()
                ,100,3, Furnish.BAD, View.NORMAL, Transport.NORMAL, new House("hell",2,3),200L, false)).build();
    }

    @GET
    @Path("/find-with-balcony/{cheapest}/{with-balcony}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCheapestOrExpensiveWithOrWithoutBalcony(@PathParam("cheapest") String cheapest,
                                                                     @PathParam("with-balcony") String balcony){
        try{
            logger.info("Got request to get cheaper or the most expensive with or without balcony");
            if ((cheapest == null || cheapest.isEmpty()) ||
                    (!Objects.equals(cheapest, "expensive") && !Objects.equals(cheapest, "cheapest"))
            ) throw new IllegalArgumentException("Must be expensive or cheapest!");

            if ((balcony == null || balcony.isEmpty()) ||
                    (!Objects.equals(balcony,"with-balcony") && !Objects.equals(balcony, "without-balcony"))
            ) throw new IllegalArgumentException("Must be with-balcony or without-balcony");
            String path = "/find-with-balcony/" + cheapest + "/" + balcony;
            Response response = client
                    .target(BASE_URI)
                    .path(path)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            if(response.getStatus() == 204) throw new NoContentException("No flats were found for " + cheapest + " and " + balcony);
            Flat flat = response.readEntity(Flat.class);
            return Response.ok(flat).build();

        } catch (IllegalArgumentException ex){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorBody.builder().errorCode(400).message("Parameters are wrong!").timestamp(LocalDateTime.now()).build())
                    .build();
        } catch (NoContentException e) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(ErrorBody.builder().errorCode(204).message("No Content").details(e.getMessage()).timestamp(LocalDateTime.now()).build())
                    .build();
        }

    }

    @GET
    @Path("/get-cheapest/{id1}/{id2}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCheaperOfTwo(@PathParam("id1") Integer id1, @PathParam("id2") Integer id2) {
        logger.info("Got request to get cheaper of two flats");
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

    private static class SSLUtil{
        protected static SSLContext getInsercureSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            return sslContext;
        }
    }
}
