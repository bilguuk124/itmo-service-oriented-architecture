package itmo.mainservice.service;

import itmo.library.House;
import itmo.library.HousePageableResponse;
import itmo.mainservice.exception.HouseExistsException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import jakarta.ejb.Local;
import jakarta.transaction.Transactional;

import java.util.List;

@Local
public interface HouseCrudService {
    @Transactional
    House createHouse(House house) throws HouseExistsException, JpaException;
    HousePageableResponse getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize);
    House getHouseByName(String name) throws HouseNotFoundException;
    @Transactional
    House updateHouseByName(String name, Integer newYear, Integer newNumberOfFloors) throws HouseNotFoundException, JpaException;
    void deleteByName(String name) throws HouseNotFoundException, JpaException;

    boolean exists(House house);
}
