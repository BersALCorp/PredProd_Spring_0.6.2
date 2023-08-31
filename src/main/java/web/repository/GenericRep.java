package web.repository;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GenericRep {
    <T> void add(T entity);

    <T> void merge(T entity);

    <T> void delete(T entity);

    <T> T find(Class<T> cls, long id);

    <T> List<T> allItems(Class<T> cls);

    void queryNameExecutor(String namedQuery);
}
