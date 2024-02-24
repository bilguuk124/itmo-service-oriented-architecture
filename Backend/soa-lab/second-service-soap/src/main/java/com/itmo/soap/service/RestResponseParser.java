package com.itmo.soap.service;


import com.itmo.soap.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class RestResponseParser {
    private static final Logger logger = LoggerFactory.getLogger(RestResponseParser.class);
    public static Flat parse(ResponseEntity<?> response) throws DatatypeConfigurationException, ClassCastException {
        logger.info("Parsing the response");
        Flat flat = new Flat();
        LinkedHashMap<?,?> map = (LinkedHashMap<?, ?>) response.getBody();
        logger.info("Cast to linkedhashmap");
        assert map != null;
        LinkedHashMap<?,?> coordinatesMap = (LinkedHashMap<?, ?>) map.get("coordinates");
        LinkedHashMap<?,?> houseMap = (LinkedHashMap<?, ?>) map.get("house");

        Coordinates coordinates = new Coordinates();
        coordinates.setCoordinateX(Double.parseDouble(String.valueOf(coordinatesMap.get("coordinate_x"))));
        coordinates.setCoordinateY(Integer.parseInt(String.valueOf(coordinatesMap.get("coordinate_y"))));
        logger.info("Coordinates: {}", coordinates);

        House house = new House();
        house.setName(String.valueOf(houseMap.get("name")));
        house.setYear(Integer.parseInt(String.valueOf(houseMap.get("year"))));
        house.setNumberOfFloors(Integer.parseInt(String.valueOf(houseMap.get("numberOfFloors"))));
        logger.info("House: {}", house);
        logger.info("Creation date: {}", LocalDate.parse(String.valueOf(map.get("creationDate"))));

        flat.setId(Integer.parseInt(String.valueOf(map.get("id"))));
        flat.setName(String.valueOf(map.get("name")));
        flat.setCoordinates(coordinates);
        flat.setCreationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(String.valueOf(map.get("creationDate"))));
        flat.setArea(Integer.parseInt(String.valueOf(map.get("area"))));
        flat.setNumberOfRooms(Long.parseLong(String.valueOf(map.get("numberOfRooms"))));
        flat.setFurnish(Furnish.fromValue(String.valueOf(map.get("furnish")).toUpperCase()));
        flat.setView(View.fromValue(String.valueOf(map.get("view")).toUpperCase()));
        flat.setTransport(Transport.fromValue(String.valueOf(map.get("transport")).toUpperCase()));
        flat.setPrice(Long.parseLong(String.valueOf(map.get("price"))));
        flat.setHasBalcony(Boolean.parseBoolean(String.valueOf(map.get("hasBalcony"))));
        flat.setHouse(house);
        logger.info("Parsing successful");
        return flat;
    }
}
