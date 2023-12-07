package itmo.mainservice.repository;

import itmo.library.House;
import itmo.library.Filter;
import itmo.library.Sort;
import itmo.mainservice.config.EntityManagerProvider;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class HouseCrudRepository {

    @Inject
    private EntityManagerProvider entityManagerProvider;

    public void save(House house){
        entityManagerProvider.getEntityManager().persist(house);
        entityManagerProvider.getEntityManager().flush();
    }

    public Optional<House> getByName(String name){
        House house = entityManagerProvider.getEntityManager().find(House.class, name);
        if (house == null) return Optional.empty();
        return Optional.of(house);
    }

    public boolean deleteByName(String name){
        House house = entityManagerProvider.getEntityManager().find(House.class, name);
        if(house == null) return false;
        entityManagerProvider.getEntityManager().remove(house);
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
        return predicate;
    }

    public boolean checkExist(House house) {
       Optional<House> result = getByName(house.getName());
       return result.isPresent();
    }
}
