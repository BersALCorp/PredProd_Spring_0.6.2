package web.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GenericDao {
    <T> void add(T entity);

    <T> void merge(T entity);

    <T> void update(T entity, long id);

    <T> void delete(T entity);

    <T> void deleteById(Class<T> t, long id);

    <T> T find(Class<T> cls, long id);

    <T> List<T> allItems(Class<T> cls);

    void resetTable();
}
