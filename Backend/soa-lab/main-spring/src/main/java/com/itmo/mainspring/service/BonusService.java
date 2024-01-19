package com.itmo.mainspring.service;

import com.itmo.feignclient.entity.Flat;
import com.itmo.mainspring.exception.FlatNotFoundException;
import com.itmo.mainspring.exception.HouseNotFoundException;
import com.itmo.mainspring.exception.NoFlatsExistsException;

public interface BonusService {
    Flat getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony) throws NoFlatsExistsException, HouseNotFoundException.NoMatchFoundException;

    Flat getCheaperOfTwoFlats(Integer id1, Integer id2) throws FlatNotFoundException;
}
