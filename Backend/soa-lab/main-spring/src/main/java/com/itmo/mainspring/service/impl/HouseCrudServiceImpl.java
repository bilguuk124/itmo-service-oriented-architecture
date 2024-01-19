package com.itmo.mainspring.service.impl;

import com.itmo.mainspring.entity.Filter;
import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.HousePageableResponse;
import com.itmo.mainspring.entity.Sort;
import com.itmo.mainspring.exception.HouseExistsException;
import com.itmo.mainspring.exception.HouseNotEmptyException;
import com.itmo.mainspring.exception.HouseNotFoundException;
import com.itmo.mainspring.repository.HouseCrudRepository;
import com.itmo.mainspring.service.HouseCrudService;
import com.itmo.mainspring.utility.FilterAndSortUtility;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HouseCrudServiceImpl implements HouseCrudService {
    private final Logger logger = LoggerFactory.getLogger(HouseCrudServiceImpl.class);
    private final HouseCrudRepository repository;

    @Override
    public House createHouse(House house) throws HouseExistsException {
        logger.info("Service to create a house starting");
        if (repository.checkExist(house)) throw new HouseExistsException(house.getName());
        House house1 = new House(house);
        repository.save(house1);
        logger.info("Service to create a house ended successfully");
        return house1;
    }

    @Override
    public HousePageableResponse getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        logger.info("Service to get all houses starting");
        List<Sort> sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
        List<Filter> filterList = FilterAndSortUtility.getFiltersFromStringList(filters, House.class);
        Validator.validateSortList(sortList, House.class);
        Validator.validateFilterList(filterList, House.class);
        logger.info("Sort List: ");
        sortList.forEach(s -> logger.info(s.toString()));
        logger.info("Filter List: ");
        filterList.forEach(s -> logger.info(s.toString()));
        if (page == null) page = FilterAndSortUtility.DEFAULT_PAGE;
        if (pageSize == null) pageSize = FilterAndSortUtility.DEFAULT_PAGE_SIZE;

        List<House> responseData = repository.getAllPageableFilteredAndSorted(sortList, filterList, page, pageSize);
        Long numberOfEntries = repository.getNumberOfEntries();
        HousePageableResponse response = new HousePageableResponse(responseData, numberOfEntries);
        logger.info("Service to get all houses ended successfully");
        return response;
    }

    @Override
    public House getHouseByName(String name) throws HouseNotFoundException {
        logger.info("Service to get a house by name starting");
        Optional<House> result = repository.getByName(name);
        if (result.isEmpty()) {
            logger.warn("Service to get a house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException(name);
        }
        logger.info("Service to get a house by name ended successfully");
        return result.get();
    }

    @Override
    public House updateHouseByName(String name, Integer newYear, Integer newNumberOfFloors) throws HouseNotFoundException {
        logger.info("Service to update the house by name starting");
        Optional<House> result = repository.getByName(name);
        if (result.isEmpty()) {
            logger.warn("Service to update the house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException(name);
        }
        House house1 = result.get();
        house1.update(newYear, newNumberOfFloors);
        Validator.validateHouse(house1);
        repository.save(house1);
        logger.info("Service to update the house by name ended successfully");
        return house1;
    }

    @Override
    public void deleteByName(String name) throws HouseNotFoundException, HouseNotEmptyException {
        logger.info("Service to delete a house by name starting");
        if (!repository.deleteByName(name)) {
            logger.warn("Service to delete a house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException(name);
        }
        logger.info("Service to delete a house by name ended successfully");
    }

    @Override
    public boolean exists(House house) {
        return repository.checkExist(house);
    }
}
