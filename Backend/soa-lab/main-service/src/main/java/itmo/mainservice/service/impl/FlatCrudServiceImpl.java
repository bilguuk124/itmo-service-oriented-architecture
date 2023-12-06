package itmo.mainservice.service.impl;

import itmo.library.*;
import itmo.mainservice.config.TransactionProvider;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
import itmo.mainservice.repository.FlatCrudRepository;
import itmo.mainservice.service.FlatCrudService;
import itmo.mainservice.utility.FilterAndSortUtility;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class FlatCrudServiceImpl implements FlatCrudService {

    @Inject
    private FlatCrudRepository repository;

    private final Logger logger = LoggerFactory.getLogger(FlatCrudServiceImpl.class);


    @Override
    @Transactional
    public Flat createFlat(FlatCreateDTO flatCreateDTO) throws NotCreatedException, ConstraintViolationException {
        logger.info("Service to create a flat starting");
        UserTransaction userTransaction = TransactionProvider.getUserTransaction();
        try{
            userTransaction.begin();
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
            throw new NotCreatedException(e);
        }

    }



    @Override
    public List<Flat> getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        logger.info("Service to get all flats starting");
        List<Sort> sortList = new ArrayList<>();
        List<Filter> filterList = new ArrayList<>();
        FilterAndSortUtility.prepareDataForSortFilter(sorts, filters, page, pageSize, Flat.class, sortList, filterList);
        logger.info("Sort List: " + sortList.stream().toString());
        logger.info("Filter List: " + filterList.stream().toString());
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 1;
        logger.info("Service to get all flats ended successfully");
        return repository.getAllPageable(sortList, filterList, page, pageSize);
    }

    @Override
    public Flat getFlatByID(Integer id) throws FlatNotFoundException {
        logger.info("Service to get a flat by id starting");
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) {
            logger.warn("Service to get a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException();
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
                throw new FlatNotFoundException();
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
    public void deleteById(Integer id) throws FlatNotFoundException {
        logger.info("Service to delete a flat by id starting");
        if (!repository.deleteById(id)) {
            logger.warn("Service to delete a flat by id ended unsuccessfully, flat was not found, throwing an exception");
            throw new FlatNotFoundException();
        }
        logger.info("Service to delete a flat by id ended successfully");
    }
}
