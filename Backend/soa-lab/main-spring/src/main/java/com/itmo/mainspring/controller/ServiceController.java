package com.itmo.mainspring.controller;

import com.itmo.feignclient.entity.Flat;
import com.itmo.mainspring.exception.FlatNotFoundException;
import com.itmo.mainspring.exception.HouseNotFoundException;
import com.itmo.mainspring.exception.NoFlatsExistsException;
import com.itmo.mainspring.service.BonusService;
import com.itmo.mainspring.service.impl.ErrorBodyGenerator;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping(value = "api/", produces = MediaType.APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class ServiceController {

    private final BonusService bonusService;
    private final ErrorBodyGenerator errorBodyGenerator;

    @GetMapping("/find-with-balcony/{cheapest}/{with-balcony}")
    public ResponseEntity<?> getCheapestOrExpensiveWithOrWithoutBalconyFlat(@PathVariable("cheapest") String cheapness, @PathVariable("with-balcony") String balcony){
        log.info("Got request to get cheapest or most expensive flat with or without balcony");
        try {
            if (
                    (cheapness == null || cheapness.isEmpty()) ||
                            (!Objects.equals(cheapness, "expensive") && !Objects.equals(cheapness, "cheapest"))
            ) {
                log.error("Bad Request parameter must be either 'cheapest' or 'expensive' (without '')");
                throw new IllegalArgumentException("Must be expensive or cheapest!");
            }

            if (
                    (balcony == null || balcony.isEmpty()) ||
                            (!Objects.equals(balcony, "with-balcony") && !Objects.equals(balcony, "without-balcony"))
            ) {
                log.error("Bad Request parameter must be either 'with-balcony' or 'without-balcony' (without '')");
                throw new IllegalArgumentException("Must be with-balcony or without-balcony");
            }

            Flat flat = bonusService.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
            log.info("Sending successful result");
            return ResponseEntity
                    .ok(flat);

        } catch (IllegalArgumentException ex) {
            log.info("Returning error body for bad request");
            return ResponseEntity
                    .status(400)
                    .body(errorBodyGenerator.generateValidationError(ex.getMessage()));
        } catch (HouseNotFoundException.NoMatchFoundException e) {
            log.info("Returning error body for no matching flat");
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(errorBodyGenerator.generateNoResultException(cheapness, balcony));
        } catch (NoFlatsExistsException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(errorBodyGenerator.generateNoFlatExistsException(e.getMessage()));
        }
    }

    @GetMapping("/get-cheapest/{id1}/{id2}")
    public ResponseEntity<?> getCheaperOfTwo(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2)  {
        try {
            log.info("Got request to get cheaper of flats with id {} and {}", id1, id2);
            if (id1 == null || id1 < 1) throw new ValidationException("Id must be a positive integer");
            if (id2 == null || id2 < 1) throw new ValidationException("Id must be a positive integer");

            Flat flat = bonusService.getCheaperOfTwoFlats(id1, id2);
            return ResponseEntity
                    .ok(flat);
        } catch (FlatNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(errorBodyGenerator.generateFlatNotFoundError(e.getMessage()));
        }
    }
}
