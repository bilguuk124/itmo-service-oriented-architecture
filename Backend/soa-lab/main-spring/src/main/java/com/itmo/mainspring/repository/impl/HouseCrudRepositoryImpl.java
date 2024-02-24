package com.itmo.mainspring.repository.impl;

import com.itmo.mainspring.entity.Filter;
import com.itmo.feignclient.entity.House;
import com.itmo.mainspring.entity.Sort;
import com.itmo.mainspring.exception.HouseNotEmptyException;
import com.itmo.mainspring.repository.HouseCrudRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HouseCrudRepositoryImpl implements HouseCrudRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManagerProvider;

    @Transactional
    public void save(House house) {
        entityManagerProvider.persist(house);
        entityManagerProvider.flush();
    }
    public Optional<House> getByName(String name) {
        House house = entityManagerProvider.find(House.class, name);
        if (house == null) return Optional.empty();
        return Optional.of(house);
    }

    @Transactional
    public boolean deleteByName(String name) throws HouseNotEmptyException {
        log.info("Getting the house");
        House house = entityManagerProvider.find(House.class, name);
        if (house == null) {
            log.info("House was not found");
            return false;
        }
        log.info("House was found");
        try {
            entityManagerProvider.remove(house);
            entityManagerProvider.flush();
        } catch (ConstraintViolationException exception) {
            log.warn("House was not empty!");
            throw new HouseNotEmptyException(exception);
        }
        return true;
    }
    public List<House> getAllPageableFilteredAndSorted(List<Sort> sorts, List<Filter> filters, Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.select(root);

        if (filters != null && !filters.isEmpty()) {
            criteriaQuery.where(applyFilters(criteriaBuilder, root, filters));
        }

        if (sorts != null && !sorts.isEmpty()) {
            criteriaQuery.orderBy(applySort(criteriaBuilder, root, sorts));
        }

        TypedQuery<House> query = entityManagerProvider.createQuery(criteriaQuery);
        return query
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    public boolean checkExist(House house) {
        Optional<House> result = getByName(house.getName());
        return result.isPresent();
    }
    public Long getNumberOfEntries() {
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<House> root = criteriaQuery.from(House.class);

        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManagerProvider.createQuery(criteriaQuery).getSingleResult();
    }
    public Predicate applyFilters(CriteriaBuilder criteriaBuilder, Root<?> root, List<Filter> filters) {
        Predicate predicate = criteriaBuilder.conjunction();

        for (Filter filter : filters) {
            switch (filter.getFilteringOperation()) {
                case EQ:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                case NEQ:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                case GT:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                case LT:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                case GTE:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                case LTE:
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(filter.getFieldName()), filter.getFieldValue()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return predicate;
    }


    private List<Order> applySort(CriteriaBuilder criteriaBuilder, Root<House> root, List<Sort> sorts) {
        return sorts.stream()
                .map(sortParam -> {
                    if (sortParam.isDesc()) {
                        return criteriaBuilder.desc(root.get(sortParam.getFieldName()));
                    } else {
                        return criteriaBuilder.asc(root.get(sortParam.getFieldName()));
                    }
                })
                .collect(Collectors.toList());
    }



}
