package web.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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
        try {
            entityManager.persist(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in add. DAO error: " + e);
            throw e;
        }
    }

    @Override
    @Transactional
    public <T> void merge(T entity) {
        try {
            entityManager.merge(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in merge. DAO error: " + e);
            throw e;
        }
    }

    @Override
    public <T> void delete(T entity) {
        try {
            entityManager.remove(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in delete. DAO error: " + e);
            throw e;
        }
    }

    @Override
    public <T> T find(Class<T> tClass, long id) {
        try {
            return entityManager.find(tClass, id);
        } catch (EntityNotFoundException e) {
            System.out.println("Error in find. DAO error: " + e);
            throw e;
        }
    }

    @Transactional
    @Override
    public <T> List<T> allItems(Class<T> tClass) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(tClass);
            Root<T> root = cq.from(tClass);
            cq.select(root);
            TypedQuery<T> query = entityManager.createQuery(cq);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            System.out.println("Error in allItems. DAO error: " + e);
            throw e;
        }
    }

    @Override
    public void queryNameExecutor(String namedQuery) {
        try {
            entityManager.createNamedQuery(namedQuery).executeUpdate();
        } catch (PersistenceException e) {
            System.out.println("Error in queryNameExecutor. DAO error: " + e);
            throw e;
        }
    }
}
