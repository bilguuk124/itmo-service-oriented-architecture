package itmo.mainservice.service;

import itmo.library.House;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import jakarta.ejb.Local;
import jakarta.transaction.Transactional;

import java.util.List;

@Local
public interface HouseCrudService {
    @Transactional
    House createHouse(House house) throws JpaException;
    List<House> getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize);
    House getHouseByName(String name) throws HouseNotFoundException;
    @Transactional
    House updateHouseByName(String name, House house) throws HouseNotFoundException, JpaException;
    void deleteByName(String name) throws HouseNotFoundException, JpaException;

    boolean exists(House house);
}
