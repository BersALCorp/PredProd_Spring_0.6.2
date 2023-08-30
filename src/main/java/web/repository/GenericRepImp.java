package web.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

@Repository
public class GenericRepImp implements GenericRep {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public <T> void add(T entity) {
        try {
            entityManager.persist(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in add. DAO error: " + e);
        }
    }

    @Transactional
    @Override
    public <T> void merge(T entity) {
        try {
            entityManager.merge(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in merge. DAO error: " + e);
        }
    }

    @Transactional
    @Override
    public <T> void update(T entity, long id) {
        try {
            var temp = entityManager.find(entity.getClass(), id);
            Arrays.stream(temp.getClass().getDeclaredFields())
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(entity);
                            if (value != null) field.set(temp, value);
                        } catch (IllegalAccessException e) {
                            System.out.println("Fields cast exception: " + e);
                        }
                    });
            entityManager.merge(temp);
        } catch (Exception e) {
            System.out.println("Error in update. DAO error: " + e);
        }
    }

    @Transactional
    @Override
    public <T> void delete(T entity) {
        try {
            entityManager.remove(entity);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in delete. DAO error: " + e);
        }
    }

    @Transactional
    @Override
    public <T> void deleteById(Class<T> entity, long id) {
        try {
            T temp = entityManager.find(entity, id);
            entityManager.remove(temp);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in deleteById. DAO error: " + e);
        }
    }

    @Transactional
    @Override
    public <T> T find(Class<T> cls, long id) {
        try {
            return entityManager.find(cls, id);
        } catch (EntityNotFoundException e) {
            System.out.println("Error in find. DAO error: " + e);
            return null;
        }
    }

    @Transactional
    @Override
    public <T> List<T> allItems(Class<T> cls) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(cls);
            Root<T> root = cq.from(cls);
            cq.select(root);
            TypedQuery<T> query = entityManager.createQuery(cq);
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            System.out.println("Error in allItems. DAO error: " + e);
            return null;
        }
    }

    @Transactional
    @Override
    public void queryExecutor(String namedQuery) {
        entityManager.createNamedQuery(namedQuery).executeUpdate();
    }
}
