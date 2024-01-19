package com.itmo.mainspring.repository.impl;

import com.itmo.feignclient.entity.*;
import com.itmo.mainspring.entity.*;
import com.itmo.mainspring.repository.FlatCrudRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FlatCrudRepositoryImpl implements FlatCrudRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManagerProvider;

    @Transactional
    public void save(Flat flat) {
        entityManagerProvider.persist(flat);
        entityManagerProvider.flush();
    }

    public Optional<Flat> getById(Integer id) {
        Flat flat = entityManagerProvider.find(Flat.class, id);
        return flat != null ? Optional.of(flat) : Optional.empty();
    }

    public List<Flat> getAllPageable(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getCriteriaBuilder();
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);
        criteriaQuery.select(root);

        if (filters != null && !filters.isEmpty()) {
            criteriaQuery.where(applyFilters(criteriaBuilder, root, filters));
        }

        if (sorts != null && !sorts.isEmpty()) {
            criteriaQuery.orderBy(applySort(criteriaBuilder, root, sorts));
        }


        TypedQuery<Flat> query = entityManagerProvider.createQuery(criteriaQuery);
        return query
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public boolean deleteById(Integer id) {
        Flat existingFlat = entityManagerProvider.find(Flat.class, id);
        if (existingFlat == null) {
            return false;
        }
        entityManagerProvider.remove(existingFlat);
        return true;
    }

    @Transactional
    public void deleteFlatsOfTheHouse(House house) {
        EntityManager entityManager = entityManagerProvider;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Flat> deleteQuery = criteriaBuilder.createCriteriaDelete(Flat.class);

        Root<Flat> flatRoot = deleteQuery.from(Flat.class);
        deleteQuery.where(criteriaBuilder.equal(flatRoot.get("house"), house));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    public long getFlatCountWithSameHouse(House house) {
        EntityManager entityManager = entityManagerProvider;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Flat> flatRoot = countQuery.from(Flat.class);
        Predicate condition = criteriaBuilder.equal(flatRoot.get("house"), house);
        countQuery.select(criteriaBuilder.count(flatRoot)).where(condition);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    public long getFlatCountWithLessRooms(Integer maxRoomNumber) {
        EntityManager entityManager = entityManagerProvider;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Flat> flatRoot = countQuery.from(Flat.class);
        Predicate condition = criteriaBuilder.lessThan(flatRoot.get("numberOfRooms"), maxRoomNumber);
        countQuery.select(criteriaBuilder.count(flatRoot)).where(condition);

        return entityManager.createQuery(countQuery).getSingleResult();

    }

    public Optional<Flat> getCheapestOrExpensiveWithOrWithoutBalcony(String cheapness, String balcony) {
        EntityManager entityManager = entityManagerProvider;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flat> query = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = query.from(Flat.class);

        query.select(root);

        if (Objects.equals(cheapness, "cheapest")) {
            query.orderBy(criteriaBuilder.asc(root.get("price")));
        } else {
            query.orderBy(criteriaBuilder.desc(root.get("price")));
        }

        Predicate balconyPredicate;
        if (Objects.equals(balcony, "with-balcony")) {
            balconyPredicate = criteriaBuilder.isTrue(root.get("hasBalcony"));
        } else {
            balconyPredicate = criteriaBuilder.isFalse(root.get("hasBalcony"));
        }

        query.where(balconyPredicate);

        List<Flat> flat = entityManager.createQuery(query).getResultList();

        return !flat.isEmpty() ? Optional.of(flat.get(0)) : Optional.empty();
    }

    public Long getNumberOfEntries() {
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManagerProvider.createQuery(criteriaQuery).getSingleResult();
    }

    private Predicate applyFilters(CriteriaBuilder criteriaBuilder, Root<?> root, List<Filter> filters) {
        Predicate predicate = criteriaBuilder.conjunction();
        for (Filter filter : filters) {
            if (filter.getFieldName().equals("furnish") || filter.getFieldName().equals("view") || filter.getFieldName().equals("transport")) {
                predicate = applyEnumPredicate(criteriaBuilder, root, filter, predicate);
            } else if (filter.getFieldName().equals("creationDate")) {
                predicate = applyDatePredicate(criteriaBuilder, root, filter, predicate);
            } else if ((filter.getNestedName() != null && !filter.getNestedName().isEmpty()) && !(Objects.equals(filter.getNestedName(), "null"))) {
                predicate = applyNestedPredicate(criteriaBuilder, root, filter, predicate);
            } else {
                predicate = applyNormalPredicate(criteriaBuilder, root, filter, predicate);
            }
        }
        return predicate;
    }

    private List<Order> applySort(CriteriaBuilder criteriaBuilder, Root<?> root, List<Sort> sorts) {
        return sorts.stream()
                .map(sortParam -> {
                    if ((sortParam.getNestedName() != null && !sortParam.getNestedName().isEmpty()) || !(Objects.equals(sortParam.getNestedName(), "null"))) {
                        if (sortParam.isDesc()) {
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()));
                        } else {
                            return criteriaBuilder.asc(root.get(sortParam.getFieldName()));
                        }
                    } else {
                        if (sortParam.isDesc()) {
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()).get(sortParam.getNestedName()));
                        } else {
                            return criteriaBuilder.asc(root.get(sortParam.getNestedName()).get(sortParam.getNestedName()));
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    private static Predicate applyEnumPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, Filter filter, Predicate predicate) {
        switch (filter.getFieldName()){
            case "furnish" -> {
                switch (filter.getFilteringOperation()) {
                    case EQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), Furnish.fromValue(filter.getFieldValue())));
                    case NEQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()),  Furnish.fromValue(filter.getFieldValue())));
                    case GT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()),  Furnish.fromValue(filter.getFieldValue())));
                    case LT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()),  Furnish.fromValue(filter.getFieldValue())));
                    case GTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()),  Furnish.fromValue(filter.getFieldValue())));
                    case LTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()),  Furnish.fromValue(filter.getFieldValue())));
                    default -> throw new IllegalArgumentException("Invalid filtering operation");
                }
            }
            case "view" -> {
                switch (filter.getFilteringOperation()) {
                    case EQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), View.fromValue(filter.getFieldValue())));
                    case NEQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()),  View.fromValue(filter.getFieldValue())));
                    case GT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()),  View.fromValue(filter.getFieldValue())));
                    case LT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()),  View.fromValue(filter.getFieldValue())));
                    case GTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()),  View.fromValue(filter.getFieldValue())));
                    case LTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()),  View.fromValue(filter.getFieldValue())));
                    default -> throw new IllegalArgumentException("Invalid filtering operation");
                }
            }
            case "transport" -> {
                switch (filter.getFilteringOperation()) {
                    case EQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), Transport.fromValue(filter.getFieldValue())));
                    case NEQ ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()),  Transport.fromValue(filter.getFieldValue())));
                    case GT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()),  Transport.fromValue(filter.getFieldValue())));
                    case LT ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()),  Transport.fromValue(filter.getFieldValue())));
                    case GTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()),  Transport.fromValue(filter.getFieldValue())));
                    case LTE ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()),  Transport.fromValue(filter.getFieldValue())));
                    default -> throw new IllegalArgumentException("Invalid filtering operation");
                }
            }
            default -> {
                throw new IllegalArgumentException("Invalid enum");
            }
        }
        return predicate;
    }

    private static Predicate applyNestedPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, Filter filter, Predicate predicate) {
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
            default -> throw new IllegalArgumentException("Invalid filtering operation");
        };
        return predicate;
    }

    private static Predicate applyNormalPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, Filter filter, Predicate predicate) {
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
            default -> throw new IllegalArgumentException("Invalid filtering operation");
        };
        return predicate;
    }

    private static Predicate applyDatePredicate(CriteriaBuilder criteriaBuilder, Root<?> root, Filter filter, Predicate predicate) {
        predicate = switch (filter.getFilteringOperation()) {
            case EQ ->
                    criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            case NEQ ->
                    criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            case GT ->
                    criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            case LT ->
                    criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            case GTE ->
                    criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            case LTE ->
                    criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()), LocalDate.parse(filter.getFieldValue())));
            default -> throw new IllegalArgumentException("Invalid filtering operation");
        };
        return predicate;
    }

}

