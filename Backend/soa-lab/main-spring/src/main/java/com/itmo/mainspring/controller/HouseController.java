package com.itmo.mainspring.controller;

import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.HousePageableResponse;
import com.itmo.mainspring.exception.*;
import com.itmo.mainspring.service.HouseCrudService;
import com.itmo.mainspring.service.impl.Validator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "api/houses", produces = MediaType.APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class HouseController {

    private final Logger logger = LoggerFactory.getLogger(HouseController.class);
    private final HouseCrudService service;

    @GetMapping(value = "/aaa")
    public ResponseEntity<?> get() {
        logger.info("Got test request");
        House house = new House("hello", 2, 3);
        List<House> houses = new ArrayList<>();
        houses.add(house);
        return ResponseEntity
                .ok(houses);
    }

    @GetMapping
    public ResponseEntity<?> getAllHousesFilteredAndSorted(@RequestParam("page") String pageStr,
                                                  @RequestParam("pageSize") String pageSizeStr,
                                                  @RequestParam(value = "sort",required = false) String sortParam,
                                                  @RequestParam(value = "filter",required = false) String filterParam) throws BadPageableException {
        logger.info("Got request to get all houses");

        int page = Validator.validatePageable(pageStr);
        int pageSize = Validator.validatePageable(pageSizeStr);

        List<String> sort = (sortParam == null)
                ? new ArrayList<>()
                : Stream.of(sortParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();
        List<String> filter = (filterParam == null)
                ? new ArrayList<>()
                : Stream.of(filterParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).toList();

        logger.info("page = {}, pageSize = {}, sort = {} filter = {}", page, pageSize, Arrays.toString(sort.toArray()), Arrays.toString(filter.toArray()));

        HousePageableResponse response = service.getAllHousesFilteredAndSorted(sort, filter, page, pageSize);
        logger.info("Successfully processed the request");
        return ResponseEntity
                .ok(response);
    }


    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createHouse(@RequestBody House house) throws HouseExistsException, JpaException {
        logger.info("Got request to create a new house");
        Validator.validateHouse(house);
        House result = service.createHouse(house);
        return ResponseEntity
                .ok(result);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteHouseByName(@PathVariable("name") String name) throws  HouseNotFoundException, HouseNotEmptyException {
        logger.info("Got request to delete a house with name = {}", name);
        if (name == null || name.isEmpty()) throw new ValidationException("House name cannot be empty");
        service.deleteByName(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getHouseByName(@PathVariable("name") String name) throws HouseNotFoundException {
        logger.info("Got request to get a house with name = {}", name);
        return ResponseEntity
                .ok(service.getHouseByName(name));
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateHouseByName(@PathVariable("name") String name, @RequestParam("year") Integer newYear, @RequestParam("numberOfFloors") Integer newNumberOfFloors) throws  HouseNotFoundException {
        logger.info("Got request to update a house with name = {}", name);
        if (name == null || name.isEmpty()) throw new ValidationException("Name of the house can not be empty!");
        if (newYear == null || newYear <= 0 || newYear > 634)
            throw new ValidationException("Year of the house must be > 0 and <= 634!");
        if (newNumberOfFloors == null || newNumberOfFloors <= 0)
            throw new ValidationException("Number of floors must be greater than 0!");
        House result = service.updateHouseByName(name, newYear, newNumberOfFloors);
        return ResponseEntity
                .ok(result);

    }
}
