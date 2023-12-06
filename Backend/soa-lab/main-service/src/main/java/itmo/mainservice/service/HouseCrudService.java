package itmo.mainservice.service;

import itmo.library.House;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
import jakarta.ejb.Local;
import jakarta.transaction.Transactional;

import java.util.List;

@Local
public interface HouseCrudService {
    @Transactional
    House createHouse(House house) throws NotCreatedException;
    List<House> getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize);
    House getHouseByName(String name) throws HouseNotFoundException;
    @Transactional
    House updateHouseByName(String name, House house) throws HouseNotFoundException, NotCreatedException;
    void deleteByName(String name) throws HouseNotFoundException;
}
