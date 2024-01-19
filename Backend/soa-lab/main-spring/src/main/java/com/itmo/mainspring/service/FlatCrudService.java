package com.itmo.mainspring.service;

import com.itmo.feignclient.entity.Flat;
import com.itmo.mainspring.entity.FlatCount;
import com.itmo.feignclient.entity.FlatCreateDTO;
import com.itmo.mainspring.entity.FlatPageableResponse;
import com.itmo.mainspring.exception.FlatNotFoundException;
import com.itmo.mainspring.exception.HouseNotFoundException;

import java.util.List;

public interface FlatCrudService {
    Flat createFlat(FlatCreateDTO flatCreateDTO) throws  HouseNotFoundException, HouseNotFoundException;

    FlatPageableResponse getAllFlats(List<String> sort, List<String> filter, Integer page, Integer pageSize) throws Exception;

    Flat getFlatByID(Integer id) throws FlatNotFoundException;

    Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException;

    void deleteById(Integer id) throws FlatNotFoundException;

    void deleteFlatsOfTheHouse(String name) throws HouseNotFoundException;

    FlatCount getFlatCountOfHouse(String houseName) throws HouseNotFoundException;

    FlatCount getFlatCountWithLessRooms(Integer maxRoomNumber);
}
