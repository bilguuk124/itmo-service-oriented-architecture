package itmo.mainservice.service.impl;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.repository.FlatCrudRepository;
import itmo.mainservice.service.BonusService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Stateless
@Slf4j
public class BonusServiceImpl implements BonusService {

    @Inject
    private FlatCrudRepository repository;

    @Override
    public Flat getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony) throws HouseNotFoundException.NoMatchFoundException {
        log.info("Service is finding the flat");
        Optional<Flat> res = repository.getCheapestOrExpensiveWithOrWithoutBalcony(cheapness, balcony);
        if (res.isEmpty()) {
            log.error("There is no flat for given parameters");
            throw new HouseNotFoundException.NoMatchFoundException("There is no flat for given parameters: " + cheapness + ", " + balcony);
        }
        log.info("Flat was found returning flat {}", res.get());
        return res.get();
    }

    @Override
    public Flat getCheaperOfTwoFlats(Integer id1, Integer id2) throws FlatNotFoundException {
        log.info("Service is checking if flats with given id exists");
        Optional<Flat> flat1 = repository.getById(id1);
        Optional<Flat> flat2 = repository.getById(id2);

        if (flat1.isEmpty()) {
            log.error("Flat with id {} was not found", id1);
            throw new FlatNotFoundException(id1);
        }
        if (flat2.isEmpty()) {
            log.error("Flat with id {} was not found", id2);
            throw new FlatNotFoundException(id2);
        }

        log.info("Both flats are present checking cheapness");
        if (flat1.get().getPrice() >= flat2.get().getPrice()) {
            log.info("Flat 2 is cheaper, hence it will be returned");
            return flat2.get();
        } else {
            log.info("Flat 1 is cheaper, hence it will be returned");
            return flat1.get();
        }
    }
}
