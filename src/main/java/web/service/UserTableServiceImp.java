package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.GenericDao;
import web.model.Car;
import web.model.MyEntity;
import web.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTableServiceImp implements UserTableService {

    private final GenericDao genericDao;

    @Autowired
    public UserTableServiceImp(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public <T> void add(T t) {
        genericDao.add(t);
    }

    @Override
    public <T> T getById(Class<T> t, long e) {
        return genericDao.find(t, e);
    }

    @Override
    public <T> void update(T t, Long id) {
        genericDao.update(t, id);
    }

    @Override
    public <T> void delete(T t) {
        genericDao.delete(t);
    }

    @Override
    public <T> void deleteById(Class<T> t, long id) {
        genericDao.deleteById(t, id);
    }

    @Transactional
    @Override
    public void deleteCarFromUser(long id) {
        User tempUser = genericDao.find(User.class, id);
        Car tempCar = tempUser.getCar();
        tempUser.setCar(null);
        genericDao.delete(tempCar);
        genericDao.merge(tempUser);
    }

    @Transactional
    @Override
    public void saveCarForUser(long id, Car car) {
        User user = genericDao.find(User.class, id);
        user.setCar(car);
        genericDao.merge(user);
    }

    @Transactional
    @Override
    public void updateCar(Car car, long id) {
        User tempUser = genericDao.find(User.class, id);
        genericDao.update(car, tempUser.getCar().getId());
    }

    @Override
    public <T> List<T> getList(Class<T> cls) {
        return genericDao.allItems(cls);
    }

    @Override
    public void resetTable() {
        genericDao.resetTable();
    }

    @Override
    public <T> List<T> listSortById(Class<T> t) {
        List<T> list = genericDao.allItems(t);
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
