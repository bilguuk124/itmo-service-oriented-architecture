package itmo.soa.mainservice.service.impl;

import itmo.soa.library.*;
import itmo.soa.mainservice.exception.FlatNotFoundException;
import itmo.soa.mainservice.repository.FlatCrudRepository;
import itmo.soa.mainservice.service.FlatCrudService;
import itmo.soa.mainservice.utility.FilterAndSortUtility;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class FlatCrudServiceImpl implements FlatCrudService {

    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int DEFAULT_PAGE = 1;

    @Inject
    private FlatCrudRepository repository;

    @Override
    public Flat createFlat(FlatCreateDTO flatCreateDTO) {
        Flat flat = new Flat();
        LocalDate date = LocalDate.now();
        flat.setCreationDate(date);
        flat.update(flatCreateDTO);
        repository.save(flat);
        return flat;
    }

    @Override
    public List<Flat> getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {

        List<Sort> sortList = new ArrayList<>();
        List<Filter> filterList = new ArrayList<>();
        FilterAndSortUtility.prepareDataForSortFilter(sorts, filters, page, pageSize, Flat.class, sortList, filterList);

        return repository.getAllPageable(sortList, filterList, page, pageSize);
    }

    @Override
    public Flat getFlatByID(Integer id) throws FlatNotFoundException {
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) throw new FlatNotFoundException();
        return result.get();
    }

    @Override
    public Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException{
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) throw new FlatNotFoundException();
        Flat flat = result.get();
        flat.update(flatCreateDTO);
        repository.save(flat);
        return flat;
    }

    @Override
    public void deleteById(Integer id) throws FlatNotFoundException {
        if (!repository.deleteById(id)) throw new FlatNotFoundException();
    }
}
