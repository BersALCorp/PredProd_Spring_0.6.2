package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.repository.GenericRep;
import web.model.Car;
import web.model.MyEntity;
import web.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTableServiceImp implements UserTableService {

    private final GenericRep genericRep;

    @Autowired
    public UserTableServiceImp(GenericRep genericRep) {
        this.genericRep = genericRep;
    }

    @Override
    public <T> void add(T t) {
        genericRep.add(t);
    }

    @Override
    public <T> T getById(Class<T> t, long e) {
        return genericRep.find(t, e);
    }

    @Override
    public <T> void update(T t, Long id) {
        genericRep.update(t, id);
    }

    @Override
    public <T> void delete(T t) {
        genericRep.delete(t);
    }

    @Override
    public <T> void deleteById(Class<T> t, long id) {
        genericRep.deleteById(t, id);
    }

    @Transactional
    @Override
    public void deleteCarFromUser(long id) {
        User tempUser = genericRep.find(User.class, id);
        Car tempCar = tempUser.getCar();
        tempUser.setCar(null);
        genericRep.delete(tempCar);
        genericRep.merge(tempUser);
    }

    @Transactional
    @Override
    public void saveCarForUser(long id, Car car) {
        User user = genericRep.find(User.class, id);
        user.setCar(car);
        genericRep.merge(user);
    }

    @Transactional
    @Override
    public void updateCar(Car car, long id) {
        User tempUser = genericRep.find(User.class, id);
        genericRep.update(car, tempUser.getCar().getId());
    }

    @Override
    public <T> List<T> getList(Class<T> cls) {
        return genericRep.allItems(cls);
    }

    @Transactional
    @Override
    public void resetTable() {
        genericRep.queryExecutor("Car.deleteTable");
        genericRep.queryExecutor("Car.createTable");
        genericRep.queryExecutor("User.deleteTable");
        genericRep.queryExecutor("User.createTable");
    }

    @Override
    public <T> List<T> listSortById(Class<T> t) {
        List<T> list = genericRep.allItems(t);
        return list.stream()
                .sorted(Comparator.comparingLong(UserTableServiceImp::getIdFromObject))
                .collect(Collectors.toList());
    }

    private static long getIdFromObject(Object obj) {
        if (obj instanceof MyEntity) {
            return ((User) obj).getId();
        } else return 0;
    }
}
