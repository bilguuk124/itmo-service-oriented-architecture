package com.itmo.mainspring.repository;

import com.itmo.mainspring.entity.Filter;
import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.Sort;
import com.itmo.mainspring.exception.HouseNotEmptyException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseCrudRepository {
    void save(House house);
    Optional<House> getByName(String name);
    boolean deleteByName(String name) throws HouseNotEmptyException;
    List<House> getAllPageableFilteredAndSorted(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize);
    boolean checkExist(House house);
    Long getNumberOfEntries();
    Predicate applyFilters(CriteriaBuilder criteriaBuilder, Root<?> root, List<Filter> filters);

}
