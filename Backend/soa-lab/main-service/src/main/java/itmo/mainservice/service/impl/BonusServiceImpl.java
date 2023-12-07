package itmo.mainservice.service.impl;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NoFlatsExistsException;
import itmo.mainservice.repository.FlatCrudRepository;
import itmo.mainservice.service.BonusService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Optional;

@Stateless
public class BonusServiceImpl implements BonusService {

    @Inject
    private FlatCrudRepository repository;

    @Override
    public Flat getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony) throws NoFlatsExistsException {
        Flat res = repository.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
        if (res == null) throw new NoFlatsExistsException();
        return res;
    }

    @Override
    public Flat getCheaperOfTwoFlats(Integer id1, Integer id2) throws FlatNotFoundException{
        Optional<Flat> flat1 = repository.getById(id1);
        Optional<Flat> flat2 = repository.getById(id2);

        if (flat1.isEmpty()) throw new FlatNotFoundException(id1);
        if (flat2.isEmpty()) throw new FlatNotFoundException(id2);

        if (flat1.get().getPrice() >= flat2.get().getPrice()) return flat2.get();
        else return flat1.get();
    }
}
