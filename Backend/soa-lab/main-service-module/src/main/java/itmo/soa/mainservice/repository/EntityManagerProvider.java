package itmo.soa.mainservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ext.Provider;
import lombok.Getter;

@Getter
@Provider
public class EntityManagerProvider {
    @PersistenceContext
    private EntityManager entityManager;
}
