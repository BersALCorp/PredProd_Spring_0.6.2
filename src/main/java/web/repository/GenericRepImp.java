package web.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenericRepImp implements GenericRep {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> void add(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public <T> void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public <T> void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public <T> T find(Class<T> tClass, long id) {
        return entityManager.find(tClass, id);
    }

    @Override
    public <T> List<T> allItems(Class<T> tClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> root = query.from(tClass);
        query.select(root);
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void queryNameExecutor(String namedQuery) {
        entityManager.createNamedQuery(namedQuery).executeUpdate();
    }
}
