package itmo.mainservice.service;

import itmo.library.Flat;
import itmo.library.FlatCount;
import itmo.library.FlatCreateDTO;
import itmo.library.FlatPageableResponse;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import jakarta.ejb.Local;
import jakarta.transaction.Transactional;

import java.util.List;

@Local
public interface FlatCrudService {
    @Transactional
    Flat createFlat(FlatCreateDTO flatCreateDTO) throws JpaException, HouseNotFoundException;
    FlatPageableResponse getAllFlats(List<String> sort, List<String> filter, Integer page, Integer pageSize) throws Exception;
    Flat getFlatByID(Integer id) throws FlatNotFoundException;
    Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException;
    void deleteById(Integer id) throws FlatNotFoundException, JpaException;

    void deleteFlatsOfTheHouse(String name) throws HouseNotFoundException, JpaException;
    FlatCount getFlatCountOfHouse(String houseName) throws HouseNotFoundException;
    FlatCount getFlatCountWithLessRooms(Integer maxRoomNumber);
}
