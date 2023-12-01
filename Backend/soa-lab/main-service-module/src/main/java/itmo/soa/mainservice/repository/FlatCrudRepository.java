package itmo.soa.mainservice.repository;

import itmo.soa.library.Filter;
import itmo.soa.library.Flat;
import itmo.soa.library.FlatCreateDTO;
import itmo.soa.library.Sort;
import itmo.soa.mainservice.exception.FlatNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

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

    // TODO: Filter and sort
    public List<Flat> getAllPageable(List<Sort> sort, List<Filter> filter, Integer page, Integer pageSize){
        CriteriaBuilder criteriaBuilder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);
        criteriaQuery.select(root);


    }

    @Transactional
    public Optional<Flat> updateById(Integer id, FlatCreateDTO newFlat) {
        Flat existingFlat = entityManagerProvider.getEntityManager().find(Flat.class, id);
        if (existingFlat == null) {
            return Optional.empty();
        }
        existingFlat.update(newFlat);
        entityManagerProvider.getEntityManager().merge(existingFlat);
        return Optional.of(existingFlat);
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
}
