package itmo.mainservice.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ext.Provider;
import lombok.Getter;

@Getter
@Provider
public class EntityManagerProvider {
    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;
}
