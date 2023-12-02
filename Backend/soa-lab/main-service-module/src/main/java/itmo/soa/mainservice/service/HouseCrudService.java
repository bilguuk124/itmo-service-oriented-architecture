package itmo.soa.mainservice.service;

import itmo.soa.library.House;
import itmo.soa.mainservice.exception.HouseNotFoundException;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface HouseCrudService {
    House createHouse(House house);
    List<House> getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize);
    House getHouseByName(String name) throws HouseNotFoundException;
    House updateHouseByName(String name, House house) throws HouseNotFoundException;
    void deleteByName(String name) throws HouseNotFoundException;
}
