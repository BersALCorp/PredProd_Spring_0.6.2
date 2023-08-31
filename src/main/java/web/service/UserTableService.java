package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;
import web.model.User;

import java.util.List;

@Service
public interface UserTableService {

    <T> void add(T t);

    void updateUser(User user);

    <T> T getById(Class<T> tClass, long e);

    <T> void delete(T t);

    void saveCarForUser(Car car, long id);

    void updateCar(Car car, long id);

    <T> List<T> listSortById(Class<T> tClass);

    <T> void deleteById(Class<T> tClass, long id);

    void deleteCarFromUser(long id);

    <T> List<T> getList(Class<T> cls);

    void recreateTable();

    void resetTable();
}
