package itmo.mainservice.service.impl;

import itmo.library.Filter;
import itmo.library.House;
import itmo.library.Sort;
import itmo.mainservice.config.TransactionProvider;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
import itmo.mainservice.repository.HouseCrudRepository;
import itmo.mainservice.service.HouseCrudService;
import itmo.mainservice.utility.FilterAndSortUtility;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class HouseCrudServiceImpl implements HouseCrudService {
    @Inject
    private HouseCrudRepository repository;

    private final Logger logger = LoggerFactory.getLogger(HouseCrudServiceImpl.class);


    @Override
    @Transactional
    public House createHouse(House house) throws NotCreatedException {
        logger.info("Service to create a house starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            House house1 = new House();
            house1.update(house);
            repository.save(house1);
            userTransaction.commit();
            logger.info("Service to create a house ended successfully");
            return house1;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to create a house ended unsuccessfully transaction error throwing an exception");
            throw new NotCreatedException(e);
        }

    }

    @Override
    public List<House> getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        logger.info("Service to get all houses starting");
        List<Sort> sortList = new ArrayList<>();
        List<Filter> filterList = new ArrayList<>();
        FilterAndSortUtility.prepareDataForSortFilter(sorts, filters, page, pageSize, House.class, sortList, filterList);
        logger.info("Sort List: " + sortList.stream().toString());
        logger.info("Filter List: " + filterList.stream().toString());
        if(page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        logger.info("Service to get all houses ended successfully");
        return repository.getAllPageableFilteredAndSorted(sortList, filterList, page, pageSize);
    }

    @Override
    public House getHouseByName(String name) throws HouseNotFoundException {
        logger.info("Service to get a house by name starting");
        Optional<House> result = repository.getByName(name);
        if(result.isEmpty()) {
            logger.warn("Service to get a house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException();
        }
        logger.info("Service to get a house by name ended successfully");
        return result.get();
    }

    @Override
    @Transactional
    public House updateHouseByName(String name, House house) throws HouseNotFoundException, NotCreatedException {
        logger.info("Service to update the house by name starting");
        UserTransaction transaction = TransactionProvider.getUserTransaction();
        try{
            transaction.begin();
            Optional<House> result = repository.getByName(name);
            if (result.isEmpty()) {
                logger.warn("Service to update the house by name ended unsuccessfully, house was not found, throwing an exception");
                throw new HouseNotFoundException();
            }
            House house1 = result.get();
            house1.update(house);
            repository.save(house1);
            transaction.commit();
            logger.info("Service to update the house by name ended successfully");
            return house1;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to update the house ended unsuccessfully transaction error throwing an exception");
            throw new NotCreatedException(e);
        }

    }

    @Override
    public void deleteByName(String name) throws HouseNotFoundException {
        logger.info("Service to delete a house by name starting");
        if (!repository.deleteByName(name)) {
            logger.warn("Service to delete a house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException();
        }
        logger.info("Service to delete a house by name ended successfully");
    }
}
