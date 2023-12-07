package itmo.mainservice.service.impl;

import itmo.library.*;
import itmo.mainservice.config.TransactionProvider;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.HouseNotFoundException;
import itmo.mainservice.exception.JpaException;
import itmo.mainservice.repository.FlatCrudRepository;
import itmo.mainservice.service.FlatCrudService;
import itmo.mainservice.service.HouseCrudService;
import itmo.mainservice.utility.FilterAndSortUtility;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Stateless
public class FlatCrudServiceImpl implements FlatCrudService {

    @Inject
    private FlatCrudRepository repository;
    @Inject
    private HouseCrudService houseCrudService;

    private final Logger logger = LoggerFactory.getLogger(FlatCrudServiceImpl.class);


    @Override
    @Transactional
    public Flat createFlat(FlatCreateDTO flatCreateDTO) throws JpaException, ConstraintViolationException, HouseNotFoundException {
        logger.info("Service to create a flat starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            House house = flatCreateDTO.getHouse();
            if (!houseCrudService.exists(house)) {
                throw new HouseNotFoundException(house.getName());
            }
            Flat flat = new Flat();
            LocalDate date = LocalDate.now();
            flat.setCreationDate(date);
            flat.update(flatCreateDTO);
            repository.save(flat);
            userTransaction.commit();
            logger.info("Service to create a flat ended successfully");
            return flat;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to create a flat ended unsuccessfully, transaction error, throwing an exception");
            throw new JpaException(e);
        }

    }



    @Override
    public List<Flat> getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) throws IllegalArgumentException {
        logger.info("Service to get all flats starting");
        List<Sort> sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
        List<Filter> filterList = FilterAndSortUtility.getFiltersFromStringList(filters, Flat.class);
        logger.info("Sort List: ");
        sortList.forEach(s -> logger.info(s.toString()));
        logger.info("Filter List: ");
        filterList.forEach(s -> logger.info(s.toString()));
        if (page == null) page = FilterAndSortUtility.DEFAULT_PAGE;
        if (pageSize == null) pageSize = FilterAndSortUtility.DEFAULT_PAGE_SIZE;
        logger.info("Service to get all flats ended successfully");
        return repository.getAllPageable(sortList, filterList, page, pageSize);
    }

    @Override
    public Flat getFlatByID(Integer id) throws FlatNotFoundException {
        logger.info("Service to get a flat by id starting");
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) {
            logger.warn("Service to get a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException(id);
        }
        logger.info("Service to get a flat by id ended successfully");
        return result.get();
    }

    @Override
    public Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException{
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            logger.info("Service to update a flat by id starting");
            Optional<Flat> result = repository.getById(id);
            if (result.isEmpty()) {
                logger.warn("Service to update a flat by id ended unsuccessfully, flat was not found, throwing an exception");
                throw new FlatNotFoundException(id);
            }
            Flat flat = result.get();
            flat.update(flatCreateDTO);
            repository.save(flat);
            userTransaction.commit();
            logger.info("Service to update a flat by id ended successfully");
            return flat;
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException |
                 RollbackException e) {
            logger.warn("Service to update a flat by id ended unsuccessfully, transaction error, throwing an exception");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) throws FlatNotFoundException, JpaException {
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();

        try{
            userTransaction.begin();
            logger.info("Service to delete a flat by id starting");
            if (!repository.deleteById(id)) {
                logger.warn("Service to delete a flat by id ended unsuccessfully, flat was not found, throwing an exception");
                throw new FlatNotFoundException(id);
            }
            userTransaction.commit();
            logger.info("Service to delete a flat by id ended successfully");
        } catch (HeuristicRollbackException | SystemException | HeuristicMixedException | NotSupportedException |
                 RollbackException e) {
            throw new JpaException(e);
        }
    }

    @Override
    public void deleteFlatsOfTheHouse(String houseName) throws HouseNotFoundException, JpaException {
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            logger.info("Service to delete flats of same house starting");
            House house = houseCrudService.getHouseByName(houseName);
            if (house == null) {
                logger.warn("Service to delete flats of same house ended unsuccessfully, house not found, throwing exception");
                throw new HouseNotFoundException(houseName);
            }
            repository.deleteFlatsOfTheHouse(house);
            logger.info("Service to delete flats of same house ended successfully");
            userTransaction.commit();
        } catch (HeuristicRollbackException | SystemException | HeuristicMixedException | NotSupportedException |
                 RollbackException e) {
            throw new JpaException(e);
        }
    }

    @Override
    public FlatCount getFlatCountOfHouse(String houseName) throws HouseNotFoundException {
        logger.info("Service to get flat count of same house starting");
        House house = houseCrudService.getHouseByName(houseName);
        if (house == null){
            logger.warn("Service to get flat count of same house ended unsuccessfully, house not found, throwing exception");
            throw new HouseNotFoundException(houseName);
        }
        return new FlatCount(repository.getFlatCountWithSameHouse(house));
    }

    @Override
    public FlatCount getFlatCountWithLessRooms(Integer maxRoomNumber) {
        logger.info("Service to get flat count of with less than given number of rooms starting");
        logger.info("Service to get flat count of with less than given number of rooms ended successfully");
        return new FlatCount(repository.getFlatCountWithLessRooms(maxRoomNumber));
    }
}
