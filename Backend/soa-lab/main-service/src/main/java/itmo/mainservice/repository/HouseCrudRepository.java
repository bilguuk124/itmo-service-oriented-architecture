package itmo.mainservice.repository;

import itmo.library.House;
import itmo.library.Filter;
import itmo.library.Sort;
import itmo.mainservice.config.EntityManagerProvider;
import itmo.mainservice.exception.HouseNotEmptyException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class HouseCrudRepository {

    @Inject
    private EntityManagerProvider entityManagerProvider;
    private final Logger logger = LoggerFactory.getLogger(HouseCrudRepository.class);

    public void save(House house){
        entityManagerProvider.getEntityManager().persist(house);
        entityManagerProvider.getEntityManager().flush();
    }

    public Optional<House> getByName(String name){
        House house = entityManagerProvider.getEntityManager().find(House.class, name);
        if (house == null) return Optional.empty();
        return Optional.of(house);
    }

    public boolean deleteByName(String name) throws HouseNotEmptyException {
        logger.info("Getting the house");
        House house = entityManagerProvider.getEntityManager().find(House.class, name);
        if(house == null) {
            logger.info("House was not found");
            return false;
        }
        logger.info("House was found");
        try{
            entityManagerProvider.getEntityManager().remove(house);
            entityManagerProvider.getEntityManager().flush();
        } catch (ConstraintViolationException exception){
            logger.warn("House was not empty!");
            throw new HouseNotEmptyException(exception);
        }
        return true;
    }

    public List<House> getAllPageableFilteredAndSorted(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize){
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.select(root);

        if (filters != null && !filters.isEmpty()){
            criteriaQuery.where(applyFilters(criteriaBuilder, root, filters));
        }

        if (sorts != null && !sorts.isEmpty()){
            criteriaQuery.orderBy(getJPAOrders(criteriaBuilder, root, sorts));
        }

        TypedQuery<House> query = entityManagerProvider.getEntityManager().createQuery(criteriaQuery);
        return query
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    private List<Order> getJPAOrders(CriteriaBuilder criteriaBuilder, Root<House> root, List<Sort> sorts) {
        return sorts.stream()
                .map(sortParam -> {
                        if (sortParam.isDesc()){
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()));
                        }
                        else{
                            return criteriaBuilder.asc(root.get(sortParam.getFieldName()));
                        }
                })
                .collect(Collectors.toList());
    }

    public Predicate applyFilters(CriteriaBuilder criteriaBuilder, Root<?> root, List<Filter> filters){
        Predicate predicate = criteriaBuilder.conjunction();

        for (Filter filter: filters){
            predicate = switch (filter.getFilteringOperation()) {
                case EQ ->
                        criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), filter.getFieldValue()));
                case NEQ ->
                        criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()), filter.getFieldValue()));
                case GT ->
                        criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()), filter.getFieldValue()));
                case LT ->
                        criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()), filter.getFieldValue()));
                case GTE ->
                        criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()), filter.getFieldValue()));
                case LTE ->
                        criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()), filter.getFieldValue()));
                default -> throw new IllegalArgumentException();
            };
        }
        return predicate;
    }

    public boolean checkExist(House house) {
       Optional<House> result = getByName(house.getName());
       return result.isPresent();
    }

    public Long getNumberOfEntries(){
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<House> root = criteriaQuery.from(House.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }
}
