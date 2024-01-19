package com.itmo.secondspring.controller;


import com.itmo.feignclient.entity.*;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping(value = "/agency",produces = MediaType.APPLICATION_XML_VALUE)
public class SecondController {



//    private final Client client = ClientBuilder.newBuilder().sslContext(SSLUtil.getInsercureSSLContext()).build();
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(SecondController.class);

    @Autowired
    public SecondController(RestTemplate restTemplate) throws NoSuchAlgorithmException, KeyManagementException {
        this.restTemplate = restTemplate;
    }


    @GetMapping(value = "/aaa")
    public ResponseEntity<?> get(){
        return ResponseEntity.ok(new Flat(1, "Hrus", new Coordinates(1D,2), LocalDate.now()
                ,100,3, Furnish.BAD, View.NORMAL, Transport.NORMAL, new House("hell",2,3),200L, false));
    }

    @GetMapping("/find-with-balcony/{cheapest}/{with-balcony}")
    public ResponseEntity<?> getCheapestOrExpensiveWithOrWithoutBalcony(@PathVariable("cheapest") String cheapest,
                                                                     @PathVariable("with-balcony") String balcony){
        try{
            logger.info("Got request to get cheaper or the most expensive with or without balcony");
            if ((cheapest == null || cheapest.isEmpty()) ||
                    (!Objects.equals(cheapest, "expensive") && !Objects.equals(cheapest, "cheapest"))
            ) throw new IllegalArgumentException("Must be expensive or cheapest!");

            if ((balcony == null || balcony.isEmpty()) ||
                    (!Objects.equals(balcony,"with-balcony") && !Objects.equals(balcony, "without-balcony"))
            ) throw new IllegalArgumentException("Must be with-balcony or without-balcony");
            String url = "http://localhost:8080/api/find-with-balcony/" + cheapest + "/" + balcony;
            ResponseEntity<?> entity = null;
            try{
                entity = restTemplate
                        .exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                Object.class);

                logger.info("Response: {}" , entity);
                logger.info("Success: {}", entity.getBody());
                return ResponseEntity
                        .ok(entity.getBody());
            } catch (HttpClientErrorException e){
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        } catch (IllegalArgumentException ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorBody.builder().errorCode(400).message("Parameters are wrong!").timestamp(LocalDateTime.now()).build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorBody.builder().errorCode(500).message("Internal Server Error").details("Servers are down").timestamp(LocalDateTime.now()).build());
        }
    }

    @GetMapping("/get-cheapest/{id1}/{id2}")
    public ResponseEntity<?> getCheaperOfTwo(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        logger.info("Got request to get cheaper of two flats");
        try{
            if (id1 == null || id1 < 1) throw new ValidationException("Id must be positive integer");
            if (id2 == null || id2 < 1) throw new ValidationException("Id must be positive integer");

            String url = "http://localhost:8080/api/get-cheapest/" + id1 + "/" + id2;
            ResponseEntity<?> entity = null;
            try{
                entity = restTemplate
                        .exchange(url,
                                HttpMethod.GET,
                                null,
                                Object.class);

                logger.info("Response: {}" , entity);
                logger.info("Success: {}", (entity.getBody()));
                return ResponseEntity
                        .ok(entity.getBody());
            } catch (HttpClientErrorException e){
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        } catch (ValidationException ex){
            logger.error("Validation error id must be positive integer");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorBody.builder().errorCode(400).message("Bad request").details(ex.getMessage()).timestamp(LocalDateTime.now()).build());
        }

    }

//    private static class SSLUtil{
//        protected static SSLContext getInsercureSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                        }
//
//                        @Override
//                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                        }
//
//                        @Override
//                        public X509Certificate[] getAcceptedIssuers() {
//                            return null;
//                        }
//                    }
//            };
//
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new SecureRandom());
//
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//            HostnameVerifier allHostsValid = (hostname, session) -> true;
//
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//            return sslContext;
//        }
//    }
}
