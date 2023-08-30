package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;

import java.util.List;

@Service
public interface UserTableService {

    <T> void add(T t);

    <T> void update(T t, Long id);

    <T> T getById(Class<T> t, long e);

    <T> void delete(T t);

    void saveCarForUser(long id, Car car);

    void updateCar(Car car, long id);

    <T> List<T> listSortById(Class<T> t);

    <T> void deleteById(Class<T> t, long id);

    void deleteCarFromUser(long id);

    <T> List<T> getList(Class<T> cls);

    void resetTable();
}
