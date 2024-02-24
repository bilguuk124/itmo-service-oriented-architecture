package com.itmo.mainspring.controller;

import com.itmo.feignclient.entity.*;
import com.itmo.mainspring.entity.*;
import com.itmo.mainspring.exception.FlatNotFoundException;
import com.itmo.mainspring.exception.HouseNotFoundException;
import com.itmo.mainspring.service.FlatCrudService;
import com.itmo.mainspring.service.impl.Validator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/flats", produces = MediaType.APPLICATION_XML_VALUE)
public class FlatController {
    private final FlatCrudService service;

    @GetMapping("/aaa")
    public ResponseEntity<?> get(){
        return ResponseEntity.ok(
                new Flat(1,"Hrus", new Coordinates(1D,2), LocalDate.now()
                ,100, 3, Furnish.BAD, View.BAD, Transport.FEW, new House("hell", 2, 3)
                , 200L, false)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllFlats(
            @RequestParam("page") String pageStr,
            @RequestParam("pageSize") String pageSizeStr,
            @RequestParam(value = "sort", required = false) String sortParam,
            @RequestParam(value = "filter", required = false) String filterParam
    ) throws Exception {
        log.info("Received a request to get all flats");
        int page = Validator.validatePageable(pageStr);
        int pageSize = Validator.validatePageable(pageSizeStr);

        List<String> sort = (sortParam == null)
                ? new ArrayList<>()
                : Stream.of(sortParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).collect(Collectors.toList());
        List<String> filter = (filterParam == null)
                ? new ArrayList<>()
                : Stream.of(filterParam.split(",")).filter(s -> !s.isEmpty()).map(String::trim).collect(Collectors.toList());

        log.info("page = {}, pageSize = {}, sort = {} filter = {}", page, pageSize, Arrays.toString(sort.toArray()), Arrays.toString(filter.toArray()));

        FlatPageableResponse response = service.getAllFlats(sort, filter, page, pageSize);

        log.info("Sending result, result =" + response.toString());
        return ResponseEntity
                .ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFlatById(@PathVariable("id") Integer id) throws FlatNotFoundException {
        if (id <= 0) throw new ValidationException("Id must be a positive integer");
        return ResponseEntity
                .ok(service.getFlatByID(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createFlat(@RequestBody FlatCreateDTO flatCreateDTO) throws HouseNotFoundException, ValidationException {
        log.info("New flat request {}", flatCreateDTO);
        Validator.validateNewFlatRequest(flatCreateDTO);
        Flat result = service.createFlat(flatCreateDTO);
        return ResponseEntity
                .ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlatByIdOrHouse(@PathVariable("id") String param) throws  HouseNotFoundException, FlatNotFoundException {
        int id;
        try {
            if (param == null || param.isEmpty()) throw new ValidationException("Id must not be empty!");
            id = Integer.parseInt(param);
            service.deleteById(id);
            return ResponseEntity
                    .ok()
                    .build();

        } catch (NumberFormatException ex) {
            service.deleteFlatsOfTheHouse(param);
            return ResponseEntity.ok().build();

        }
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updateFlatById(@PathVariable("id") String idStr,@RequestBody FlatCreateDTO dto) throws FlatNotFoundException, ValidationException {
        try {
            if (idStr == null || idStr.isEmpty()) throw new ValidationException("Id cannot be empty");
            int id = Integer.parseInt(idStr);
            if (id <= 0) throw new ValidationException("Id must be positive integer");
            Validator.validateNewFlatRequest(dto);
            Flat result = service.updateFlatById(id, dto);
            return ResponseEntity
                    .ok(result);
        } catch (NumberFormatException ex) {
            throw new ValidationException("Id must be a positive number");
        }
    }

    @GetMapping("/{houseName}/count")
    public ResponseEntity<?> getCountOfFlatsInTheSameHouse(@PathVariable("houseName") String houseName) throws HouseNotFoundException {
            if (houseName == null || houseName.isEmpty()) throw new IllegalArgumentException();
            return ResponseEntity
                    .ok(service.getFlatCountOfHouse(houseName));
    }

    @GetMapping("/numberOfRooms/{numberOfRooms}")
    public ResponseEntity<?> getFlatsWithRoomsLessThan(@PathVariable("numberOfRooms") Integer numberOfRooms) {
        if (numberOfRooms == null) throw new IllegalArgumentException("number of rooms can't be null");
        return ResponseEntity.ok(service.getFlatCountWithLessRooms(numberOfRooms));
    }


}
