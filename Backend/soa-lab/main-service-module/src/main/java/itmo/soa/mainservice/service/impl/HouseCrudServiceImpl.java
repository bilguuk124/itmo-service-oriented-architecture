package itmo.soa.mainservice.service.impl;

import itmo.soa.library.Filter;
import itmo.soa.library.House;
import itmo.soa.library.Sort;
import itmo.soa.mainservice.exception.HouseNotFoundException;
import itmo.soa.mainservice.repository.HouseCrudRepository;
import itmo.soa.mainservice.service.HouseCrudService;
import itmo.soa.mainservice.utility.FilterAndSortUtility;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HouseCrudServiceImpl implements HouseCrudService {



    @Inject
    private HouseCrudRepository repository;

    @Override
    public House createHouse(House house) {
        House house1 = new House();
        house1.update(house);
        repository.save(house1);
        return house1;
    }

    @Override
    public List<House> getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        List<Sort> sortList = new ArrayList<>();
        List<Filter> filterList = new ArrayList<>();
        FilterAndSortUtility.prepareDataForSortFilter(sorts, filters, page, pageSize, House.class, sortList, filterList);

        return repository.getAllPageableFilteredAndSorted(sortList, filterList, page, pageSize);
    }

    @Override
    public House getHouseByName(String name) throws HouseNotFoundException {
        Optional<House> result = repository.getByName(name);
        if(result.isEmpty()) throw new HouseNotFoundException();
        return result.get();
    }

    @Override
    public House updateHouseByName(String name, House house) throws HouseNotFoundException {
        Optional<House> result = repository.getByName(name);
        if (result.isEmpty()) throw new HouseNotFoundException();
        House house1 = result.get();
        house1.update(house);
        repository.save(house1);
        return house1;
    }

    @Override
    public void deleteByName(String name) throws HouseNotFoundException {
        if (!repository.deleteByName(name)) throw new HouseNotFoundException();
    }
}
