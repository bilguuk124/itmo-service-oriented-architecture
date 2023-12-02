package itmo.soa.mainservice.repository;

import itmo.soa.library.*;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class FlatCrudRepository {
    @Inject
    private EntityManagerProvider entityManagerProvider;

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
            Predicate predicate = criteriaBuilder.conjunction();
            applyFilters(predicate, criteriaBuilder, root, filters);
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

    @Transactional
    public boolean deleteById(Integer id){
        Flat existingFlat = entityManagerProvider.getEntityManager().find(Flat.class, id);
        if (existingFlat == null){
            return false;
        }
        entityManagerProvider.getEntityManager().remove(existingFlat);
        return true;
    }

    private void applyFilters(Predicate predicate, CriteriaBuilder criteriaBuilder, Root<?> root,  List<Filter> filters){
        for(Filter filter : filters){
            if (filter.getNestedName() != null){
                switch (filter.getFilteringOperation()){
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
            else{
                Join<Flat, Coordinates> flatCoordinatesJoin = root.join(filter.getFieldName());
                switch (filter.getFilteringOperation()){
                    case EQ:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    case NEQ:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    case GT:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    case LT:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    case GTE:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    case LTE:
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(flatCoordinatesJoin.get(filter.getNestedName()), filter.getFieldValue()));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
    }
    private List<Order> getJPAOrders(CriteriaBuilder criteriaBuilder, Root<?> root, List<Sort> sorts){
        return sorts.stream()
                .map(sortParam -> {
                    if(sortParam.getNestedName() == null || sortParam.getNestedName().isEmpty()){
                        if (sortParam.isDesc()){
                            return criteriaBuilder.desc(root.get(sortParam.getFieldName()));
                        }
                        else{
                            return criteriaBuilder.asc(root.get(sortParam.getFieldName()));
                        }
                    }
                    else{
                        Join<Flat, Coordinates> flatCoordinatesJoin = root.join(sortParam.getFieldName());
                        if (sortParam.isDesc()){
                            return criteriaBuilder.desc(flatCoordinatesJoin.get(sortParam.getNestedName()));
                        }
                        else{
                            return criteriaBuilder.asc(flatCoordinatesJoin.get(sortParam.getNestedName()));
                        }
                    }
                })
                .collect(Collectors.toList());
    }

}
