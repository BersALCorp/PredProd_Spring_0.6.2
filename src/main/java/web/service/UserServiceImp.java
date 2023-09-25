package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Car;
import web.model.MyEntity;
import web.model.User;
import web.repository.GenericRep;

import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final GenericRep genericRep;

    @Autowired
    public UserServiceImp(GenericRep genericRep) {
        this.genericRep = genericRep;
    }

    private static long getIdFromObject(Object obj) {
        if (obj instanceof MyEntity) {
            return ((User) obj).getId();
        } else return 0;
    }

    @Override
    public <T> void add(T t) {
        genericRep.add(t);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        genericRep.add(user);
        return genericRep.find(User.class, user.getId());
    }

    @Override
    public <T> T getById(Class<T> tClass, long id) {
        return genericRep.find(tClass, id);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        User tempUser = genericRep.find(user.getClass(), user.getId());
        Car tempCar = tempUser.getCar();
        tempUser = user;
        tempUser.setCar(tempCar);
        genericRep.merge(tempUser);
    }

    @Transactional
    @Override
    public <T> void deleteById(Class<T> tClass, long id) {
        genericRep.delete(genericRep.find(tClass, id));
    }

    @Transactional
    @Override
    public void deleteCarFromUser(long id) {
        User tempUser = genericRep.find(User.class, id);
        Car tempCar = tempUser.getCar();
        tempUser.setCar(null);
        genericRep.delete(tempCar);
    }

    @Transactional
    @Override
    public void saveCarForUser(Car car, long id) {
        User user = genericRep.find(User.class, id);
        user.setCar(car);
        genericRep.merge(user);
    }

    @Transactional
    @Override
    public void updateCar(Car car, Long id) {
        Car tempCar = genericRep.find(User.class, id).getCar();
        tempCar.setBrand(car.getBrand());
        tempCar.setSeries(car.getSeries());
        tempCar.setModel(car.getModel());
        tempCar.setColor(car.getColor());
        genericRep.merge(tempCar);
    }

    @Transactional
    @Override
    public <T> List<T> getList(Class<T> cls) {
        return genericRep.allItems(cls);
    }

    @Transactional
    @Override
    public void recreateTable() {
        genericRep.queryNameExecutor("Car.deleteTable");
        genericRep.queryNameExecutor("Car.createTable");
        genericRep.queryNameExecutor("User.deleteTable");
        genericRep.queryNameExecutor("User.createTable");
    }

    @Transactional
    @Override
    public void resetTable() {
        genericRep.queryNameExecutor("User.cleanTable");
        genericRep.queryNameExecutor("Car.cleanTable");
    }

    @Override
    public <T> List<T> listSortById(Class<T> tClass) {
        List<T> list = genericRep.allItems(tClass);
        return list.stream()
                .sorted(Comparator.comparingLong(UserServiceImp::getIdFromObject))
                .toList();
    }
}
