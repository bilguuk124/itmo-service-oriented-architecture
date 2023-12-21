package itmo.mainservice.service;

import itmo.library.Flat;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NoFlatsExistsException;
import itmo.mainservice.service.impl.NoMatchFoundException;
import jakarta.ejb.Local;

@Local
public interface BonusService {
    Flat getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony) throws NoFlatsExistsException, NoMatchFoundException;

    Flat getCheaperOfTwoFlats(Integer id1, Integer id2) throws FlatNotFoundException;
}
