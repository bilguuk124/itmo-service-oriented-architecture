package itmo.mainservice.service.impl;

import itmo.library.Filter;
import itmo.library.House;
import itmo.library.HousePageableResponse;
import itmo.library.Sort;
import itmo.mainservice.config.TransactionProvider;
import itmo.mainservice.exception.HouseExistsException;
import itmo.mainservice.exception.HouseNotEmptyException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import itmo.mainservice.repository.HouseCrudRepository;
import itmo.mainservice.service.HouseCrudService;
import itmo.mainservice.utility.FilterAndSortUtility;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Stateless
public class HouseCrudServiceImpl implements HouseCrudService {
    @Inject
    private HouseCrudRepository repository;

    private final Logger logger = LoggerFactory.getLogger(HouseCrudServiceImpl.class);


    @Override
    @Transactional
    public House createHouse(House house) throws HouseExistsException, JpaException {
        logger.info("Service to create a house starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            if (repository.getByName(house.getName()).isPresent()) throw new HouseExistsException(house.getName());
            House house1 = new House(house);
            repository.save(house1);
            userTransaction.commit();
            logger.info("Service to create a house ended successfully");
            return house1;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to create a house ended unsuccessfully transaction error throwing an exception");
            throw new JpaException(e);
        }

    }

    @Override
    public HousePageableResponse getAllHousesFilteredAndSorted(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        logger.info("Service to get all houses starting");
        List<Sort> sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
        List<Filter> filterList = FilterAndSortUtility.getFiltersFromStringList(filters, House.class);
        logger.info("Sort List: ");
        sortList.forEach(s -> logger.info(s.toString()));
        logger.info("Filter List: ");
        filterList.forEach(s -> logger.info(s.toString()));
        if(page == null) page = FilterAndSortUtility.DEFAULT_PAGE;
        if (pageSize == null) pageSize = FilterAndSortUtility.DEFAULT_PAGE_SIZE;

        List<House> responseData = repository.getAllPageableFilteredAndSorted(sortList, filterList, page, pageSize);
        Long numberOfEntries = repository.getNumberOfEntries();
        HousePageableResponse response = new HousePageableResponse(responseData, numberOfEntries);
        logger.info("Service to get all houses ended successfully");
        return response;
    }

    @Override
    public House getHouseByName(String name) throws HouseNotFoundException {
        logger.info("Service to get a house by name starting");
        Optional<House> result = repository.getByName(name);
        if(result.isEmpty()) {
            logger.warn("Service to get a house by name ended unsuccessfully, house was not found, throwing an exception");
            throw new HouseNotFoundException(name);
        }
        logger.info("Service to get a house by name ended successfully");
        return result.get();
    }

    @Override
    @Transactional
    public House updateHouseByName(String name, Integer newYear, Integer newNumberOfFloors) throws HouseNotFoundException, JpaException {
        logger.info("Service to update the house by name starting");
        UserTransaction transaction = TransactionProvider.getUserTransaction();
        try{
            transaction.begin();
            Optional<House> result = repository.getByName(name);
            if (result.isEmpty()) {
                logger.warn("Service to update the house by name ended unsuccessfully, house was not found, throwing an exception");
                throw new HouseNotFoundException(name);
            }
            House house1 = result.get();
            house1.update(newYear, newNumberOfFloors);
            repository.save(house1);
            transaction.commit();
            logger.info("Service to update the house by name ended successfully");
            return house1;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to update the house ended unsuccessfully transaction error throwing an exception");
            throw new JpaException(e);
        }

    }

    @Override
    public void deleteByName(String name) throws HouseNotFoundException, JpaException, HouseNotEmptyException {
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            logger.info("Service to delete a house by name starting");
            if (!repository.deleteByName(name)) {
                logger.warn("Service to delete a house by name ended unsuccessfully, house was not found, throwing an exception");
                userTransaction.rollback();
                throw new HouseNotFoundException(name);
            }
            userTransaction.commit();
            logger.info("Service to delete a house by name ended successfully");
        } catch (HeuristicRollbackException | SystemException | HeuristicMixedException | NotSupportedException |
                 RollbackException e) {
            logger.error(e.getMessage());
            throw new JpaException(e);
        }
    }

    @Override
    public boolean exists(House house) {
        return repository.checkExist(house);
    }
}
