package com.itmo.mainspring.service.impl;

import com.itmo.feignclient.entity.*;
import com.itmo.mainspring.entity.*;
import com.itmo.mainspring.exception.FlatNotFoundException;
import com.itmo.mainspring.exception.HouseNotFoundException;
import com.itmo.mainspring.exception.JpaException;
import com.itmo.mainspring.repository.FlatCrudRepository;
import com.itmo.mainspring.service.FlatCrudService;
import com.itmo.mainspring.service.HouseCrudService;
import com.itmo.mainspring.utility.FilterAndSortUtility;
import jakarta.transaction.SystemException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlatCrudServiceImpl implements FlatCrudService {

    private final Logger logger = LoggerFactory.getLogger(FlatCrudServiceImpl.class);
    private final FlatCrudRepository repository;
    private final HouseCrudService houseCrudService;

    @Override
    public Flat createFlat(FlatCreateDTO flatCreateDTO) throws ConstraintViolationException, HouseNotFoundException {
        logger.info("Service to create a flat starting");
        House house = flatCreateDTO.getHouse();
        if (!houseCrudService.exists(house)) {
            throw new HouseNotFoundException(house.getName());
        }
        Flat flat = new Flat();
        flat.setCreationDate(LocalDate.now());
        flat.update(flatCreateDTO);
        repository.save(flat);
        logger.info("Service to create a flat ended successfully");
        return flat;

    }


    @Override
    public FlatPageableResponse getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) throws ValidationException, SystemException, JpaException {
        logger.info("Service to get all flats starting");
        List<Sort> sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
        List<Filter> filterList = FilterAndSortUtility.getFiltersFromStringList(filters, Flat.class);
        Validator.validateSortList(sortList, Flat.class);
        Validator.validateFilterList(filterList, Flat.class);
        logger.info("Sort List: ");
        sortList.forEach(s -> logger.info(s.toString()));
        logger.info("Filter List: ");
        filterList.forEach(s -> logger.info(s.toString()));
        if (page == null) page = FilterAndSortUtility.DEFAULT_PAGE;
        if (pageSize == null) pageSize = FilterAndSortUtility.DEFAULT_PAGE_SIZE;
        List<Flat> responseData = repository.getAllPageable(sortList, filterList, page, pageSize);
        Long numberOfEntries = repository.getNumberOfEntries();
        FlatPageableResponse response = new FlatPageableResponse(responseData, numberOfEntries);
        logger.info("Service to get all flats ended successfully");
        return response;
    }

    @Override
    public Flat getFlatByID(Integer id) throws FlatNotFoundException {
        logger.info("Service to get a flat by id starting");
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) {
            logger.warn("Service to get a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException(id);
        }
        logger.info("Service to get a flat by id ended successfully");
        return result.get();
    }

    @Override
    public Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException {
        logger.info("Service to update a flat by id starting");
        validateFlatCreateDto(flatCreateDTO);
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) {
            logger.warn("Service to update a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException(id);
        }
        Flat flat = result.get();
        flat.update(flatCreateDTO);
        repository.save(flat);
        logger.info("Service to update a flat by id ended successfully");
        return flat;
    }

    private void validateFlatCreateDto(FlatCreateDTO flatCreateDTO) {
        if (flatCreateDTO.getName() == null || flatCreateDTO.getName().isEmpty())
            throw new ValidationException("name: cannot be null or empty!");
        if (flatCreateDTO.getCoordinates().getX() > 548)
            throw new ValidationException("Coordinates: x must be less than 548!");
        if (flatCreateDTO.getCoordinates().getY() == null)
            throw new ValidationException("Coordinates: y must be not null");
    }

    @Override
    public void deleteById(Integer id) throws FlatNotFoundException {
        logger.info("Service to delete a flat by id starting");
        if (!repository.deleteById(id)) {
            logger.warn("Service to delete a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException(id);
        }
        logger.info("Service to delete a flat by id ended successfully");
    }

    @Override
    public void deleteFlatsOfTheHouse(String houseName) throws HouseNotFoundException {
        logger.info("Service to delete flats of same house starting");
        House house = houseCrudService.getHouseByName(houseName);
        if (house == null) {
            logger.warn("Service to delete flats of same house ended unsuccessfully, house not found, throwing exception");
            throw new HouseNotFoundException(houseName);
        }
        repository.deleteFlatsOfTheHouse(house);
        logger.info("Service to delete flats of same house ended successfully");
    }

    @Override
    public FlatCount getFlatCountOfHouse(String houseName) throws HouseNotFoundException {
        logger.info("Service to get flat count of same house starting");
        House house = houseCrudService.getHouseByName(houseName);
        if (house == null) {
            logger.warn("Service to get flat count of same house ended unsuccessfully, house not found, throwing exception");
            throw new HouseNotFoundException(houseName);
        }
        return new FlatCount(repository.getFlatCountWithSameHouse(house));
    }

    @Override
    public FlatCount getFlatCountWithLessRooms(Integer maxRoomNumber) {
        logger.info("Service to get flat count of with less than given number of rooms starting");
        logger.info("Service to get flat count of with less than given number of rooms ended successfully");
        return new FlatCount(repository.getFlatCountWithLessRooms(maxRoomNumber));
    }
}
