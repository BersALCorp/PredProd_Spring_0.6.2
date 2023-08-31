package web.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GenericRepImp implements GenericRep {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public <T> void add(T entity) {
        entityManager.persist(entity);
    }

    @Override
    @Transactional
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

    @Transactional
    @Override
    public <T> List<T> allItems(Class<T> tClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> root = cq.from(tClass);
        cq.select(root);
        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public void queryNameExecutor(String namedQuery) {
        entityManager.createNamedQuery(namedQuery).executeUpdate();
    }
}
