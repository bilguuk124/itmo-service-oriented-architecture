package itmo.mainservice.repository;

import itmo.library.*;
import itmo.mainservice.config.EntityManagerProvider;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class FlatCrudRepository {
    @Inject
    private EntityManagerProvider entityManagerProvider;

    @Transactional
    public void save(Flat flat){
        entityManagerProvider.getEntityManager().persist(flat);
        entityManagerProvider.getEntityManager().flush();
    }

    public Optional<Flat> getById(Integer id){
        Flat flat = entityManagerProvider.getEntityManager().find(Flat.class, id);
        return flat != null ? Optional.of(flat) : Optional.empty();
    }

    public List<Flat> getAllPageable(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize){
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);
        criteriaQuery.select(root);

        if (filters != null && !filters.isEmpty()){
            criteriaQuery.where(applyFilters(criteriaBuilder, root, filters));
        }

        if (sorts != null && !sorts.isEmpty()){
            criteriaQuery.orderBy(getJPAOrders(criteriaBuilder, root, sorts));
        }

        TypedQuery<Flat> query = entityManagerProvider.getEntityManager().createQuery(criteriaQuery);
        return query
                .setFirstResult((page-1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public boolean deleteById(Integer id){
        Flat existingFlat = entityManagerProvider.getEntityManager().find(Flat.class, id);
        if (existingFlat == null){
            return false;
        }
        entityManagerProvider.getEntityManager().remove(existingFlat);
        return true;
    }

    private Predicate applyFilters(CriteriaBuilder criteriaBuilder, Root<?> root,  List<Filter> filters){
        Predicate predicate = criteriaBuilder.conjunction();
        for(Filter filter : filters){
            if ( (filter.getNestedName() != null && !filter.getNestedName().isEmpty()) || !(Objects.equals(filter.getNestedName(), "null"))){
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
            else{
                predicate = switch (filter.getFilteringOperation()) {
                    case EQ ->
                            criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    case NEQ ->
                            criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    case GT ->
                            criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    case LT ->
                            criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    case GTE ->
                            criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    case LTE ->
                            criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()).get(filter.getNestedName()), filter.getFieldValue()));
                    default -> throw new IllegalArgumentException();
                };
            }
        }
        return predicate;
    }
    private List<Order> getJPAOrders(CriteriaBuilder criteriaBuilder, Root<?> root, List<Sort> sorts){
        return sorts.stream()
                .map(sortParam -> {
                    if( (sortParam.getNestedName() != null && !sortParam.getNestedName().isEmpty()) || !(Objects.equals(sortParam.getNestedName(), "null"))){
                        if (sortParam.isDesc()){
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()));
                        }
                        else{
                            return criteriaBuilder.asc(root.get(sortParam.getFieldName()));
                        }
                    }
                    else{
                        if (sortParam.isDesc()){
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()).get(sortParam.getNestedName()));
                        }
                        else{
                            return criteriaBuilder.asc(root.get(sortParam.getNestedName()).get(sortParam.getNestedName()));
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFlatsOfTheHouse(House house) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Flat> deleteQuery = criteriaBuilder.createCriteriaDelete(Flat.class);

        Root<Flat> flatRoot = deleteQuery.from(Flat.class);
        deleteQuery.where(criteriaBuilder.equal(flatRoot.get("house"),house));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    public long getFlatCountWithSameHouse(House house) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Flat> flatRoot = countQuery.from(Flat.class);
        Predicate condition = criteriaBuilder.equal(flatRoot.get("house"), house);
        countQuery.select(criteriaBuilder.count(flatRoot)).where(condition);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    public long getFlatCountWithLessRooms(Integer maxRoomNumber) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Flat> flatRoot = countQuery.from(Flat.class);
        Predicate condition = criteriaBuilder.lessThan(flatRoot.get("numberOfRooms"), maxRoomNumber);
        countQuery.select(criteriaBuilder.count(flatRoot)).where(condition);

        return entityManager.createQuery(countQuery).getSingleResult();

    }

    public Flat getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony){
        EntityManager entityManager= entityManagerProvider.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flat> query = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = query.from(Flat.class);

        query.select(root);

        if(Objects.equals(cheapness, "cheapest")){
            query.orderBy(criteriaBuilder.asc(root.get("price")));
        }
        else{
            query.orderBy(criteriaBuilder.desc(root.get("price")));
        }

        Predicate balconyPredicate;
        if (Objects.equals(balcony, "with-balcony")){
            balconyPredicate = criteriaBuilder.isTrue(root.get("hasBalcony"));
        }
        else{
            balconyPredicate = criteriaBuilder.isFalse(root.get("hasBalcony"));
        }

        query.where(balconyPredicate);

        return entityManager.createQuery(query).getSingleResult();
    }

    public Long getNumberOfEntries(){
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }
}
