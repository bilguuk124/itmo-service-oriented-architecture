package com.itmo.mainspring.repository;

import com.itmo.mainspring.entity.Filter;
import com.itmo.feignclient.entity.Flat;
import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatCrudRepository {
    void save(Flat flat);
    Optional<Flat> getById(Integer id);
    List<Flat> getAllPageable(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize);
    public boolean deleteById(Integer id);
    long getFlatCountWithSameHouse(House house);
    void deleteFlatsOfTheHouse(House house);
    long getFlatCountWithLessRooms(Integer maxRoomNumber);
    Optional<Flat> getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony);
    Long getNumberOfEntries();
}
