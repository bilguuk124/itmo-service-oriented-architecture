package com.itmo.mainspring.service;

import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.HousePageableResponse;
import com.itmo.mainspring.exception.HouseExistsException;
import com.itmo.mainspring.exception.HouseNotEmptyException;
import com.itmo.mainspring.exception.HouseNotFoundException;

import java.util.List;

public interface HouseCrudService {
    House createHouse(House house) throws HouseExistsException;

    HousePageableResponse getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize);

    House getHouseByName(String name) throws HouseNotFoundException;

    House updateHouseByName(String name, Integer newYear, Integer newNumberOfFloors) throws HouseNotFoundException;

    void deleteByName(String name) throws HouseNotFoundException, HouseNotEmptyException;

    boolean exists(House house);
}
