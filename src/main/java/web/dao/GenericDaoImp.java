package web.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

@Repository
public class GenericDaoImp implements GenericDao {

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
    public void resetTable() {

        String dropUserTableSql = "DROP TABLE IF EXISTS users CASCADE;";
        String dropCarTableSql = "DROP TABLE IF EXISTS cars CASCADE;";

        String createUserTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "firstName VARCHAR(255) NOT NULL," +
                "lastName VARCHAR(255) NOT NULL," +
                "age INT NOT NULL," +
                "address VARCHAR(255)," +
                "email VARCHAR(255) NOT NULL," +
                "sex VARCHAR(50)," +
                "role VARCHAR(50)," +
                "car_id BIGINT REFERENCES cars(id) ON DELETE CASCADE" +
                ");";

        String createCarTableSql = "CREATE TABLE IF NOT EXISTS cars (" +
                "id SERIAL PRIMARY KEY," +
                "brand VARCHAR(255) NOT NULL," +
                "series VARCHAR(255)," +
                "model VARCHAR(255)," +
                "color VARCHAR(255)," +
                "user_id BIGINT REFERENCES users(id) ON DELETE CASCADE" +
                ");";

        entityManager.createNativeQuery(dropCarTableSql).executeUpdate();
        entityManager.createNativeQuery(createCarTableSql).executeUpdate();
        entityManager.createNativeQuery(dropUserTableSql).executeUpdate();
        entityManager.createNativeQuery(createUserTableSql).executeUpdate();
    }
}
