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
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.hibernate.type.descriptor.java.CoercionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Stateless
public class FlatCrudServiceImpl implements FlatCrudService {

    private final Logger logger = LoggerFactory.getLogger(FlatCrudServiceImpl.class);
    @Inject
    private FlatCrudRepository repository;
    @Inject
    private HouseCrudService houseCrudService;

    @Override
    public Flat createFlat(FlatCreateDTO flatCreateDTO) throws JpaException, ConstraintViolationException, HouseNotFoundException {
        logger.info("Service to create a flat starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try {
            userTransaction.begin();
            House house = flatCreateDTO.getHouse();
            if (!houseCrudService.exists(house)) {
                throw new HouseNotFoundException(house.getName());
            }
            Flat flat = new Flat();
            flat.setCreationDate(LocalDate.now());
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
    public FlatPageableResponse getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) throws ValidationException, SystemException, JpaException {
        logger.info("Service to get all flats starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
            List<Sort> sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
            List<Filter> filterList = FilterAndSortUtility.getFiltersFromStringList(filters, Flat.class);
            Validator.validateSortList(sortList, Flat.class);
            Validator.validateFilterList(filterList, Flat.class);
            logger.info("Sort List: ");
            sortList.forEach(s -> logger.info(s.toString()));
            logger.info("Filter List: ");
            filterList.forEach(s -> logger.info(s.toString()));
            if (page == null) page = FilterAndSortUtility.DEFAULT_PAGE;
            if (pageSize == null) pageSize = FilterAndSortUtility.DEFAULT_PAGE_SIZE;
            List<Flat> responseData = repository.getAllPageable(sortList, filterList, page, pageSize);
            Long numberOfEntries = repository.getNumberOfEntries();
            FlatPageableResponse response = new FlatPageableResponse(responseData, numberOfEntries);
            logger.info("Service to get all flats ended successfully");
            return response;
        } catch (SystemException | NotSupportedException e) {
            userTransaction.rollback();
            throw new JpaException(e);
        } catch (IllegalArgumentException e){
            userTransaction.rollback();
            throw new ValidationException(e.getMessage());
        }
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
    public Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException {
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try {
            userTransaction.begin();
            logger.info("Service to update a flat by id starting");
            validateFlatCreateDto(flatCreateDTO);
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

    private void validateFlatCreateDto(FlatCreateDTO flatCreateDTO) {
        if (flatCreateDTO.getName() == null || flatCreateDTO.getName().isEmpty())
            throw new ValidationException("name: cannot be null or empty!");
        if (flatCreateDTO.getCoordinates().getX() > 548)
            throw new ValidationException("Coordinates: x must be less than 548!");
        if (flatCreateDTO.getCoordinates().getY() == null)
            throw new ValidationException("Coordinates: y must be not null");
    }

    @Override
    public void deleteById(Integer id) throws FlatNotFoundException, JpaException {
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();

        try {
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
        try {
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
        if (house == null) {
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
