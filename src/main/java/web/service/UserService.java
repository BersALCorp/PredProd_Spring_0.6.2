package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;
import web.model.User;

import java.util.List;

@Service
public interface UserService {

    <T> void add(T t);

    User saveUser(User user);

    void updateUser(User user);

    <T> T getById(Class<T> tClass, long e);

    void saveCarForUser(Car car, long id);

    void updateCar(Car car, Long id);

    <T> List<T> listSortById(Class<T> tClass);

    <T> void deleteById(Class<T> tClass, long id);

    void deleteCarFromUser(long id);

    <T> List<T> getList(Class<T> cls);

    void recreateTable();

    void resetTable();
}
