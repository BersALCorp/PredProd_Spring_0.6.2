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
import java.util.stream.Collectors;

@Service
public class UserTableServiceImp implements UserTableService {

    private final GenericRep genericRep;

    @Autowired
    public UserTableServiceImp(GenericRep genericRep) {
        this.genericRep = genericRep;
    }

    private static long getIdFromObject(Object obj) {
        if (obj instanceof MyEntity) {
            return ((User) obj).getId();
        } else return 0;
    }

    @Override
    public <T> void add(T t) {
        try {
            genericRep.add(t);
        } catch (Exception e) {
            System.out.println("Error in service add - " + e);
        }
    }

    @Override
    public <T> T getById(Class<T> tClass, long id) {
        try {
            return genericRep.find(tClass, id);
        } catch (Exception e) {
            System.out.println("Error in service getById - " + e);
        }
        return null;
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        try {
            User tempUser = genericRep.find(user.getClass(), user.getId());
            Car tempCar = tempUser.getCar();
            tempUser = user;
            tempUser.setCar(tempCar);
            genericRep.merge(tempUser);
        } catch (Exception e) {
            System.out.println("Error in service updateUser - " + e);
        }
    }

    @Transactional
    @Override
    public <T> void deleteById(Class<T> tClass, long id) {
        try {
            genericRep.delete(genericRep.find(tClass, id));
        } catch (Exception e) {
            System.out.println("Error in service deleteById - " + e);
        }
    }

    @Transactional
    @Override
    public void deleteCarFromUser(long id) {
        try {
            User tempUser = genericRep.find(User.class, id);
            Car tempCar = tempUser.getCar();
            tempUser.setCar(null);
            genericRep.delete(tempCar);
        } catch (Exception e) {
            System.out.println("Error in service deleteCarFromUser - " + e);
        }
    }

    @Transactional
    @Override
    public void saveCarForUser(Car car, long id) {
        try {
            User user = genericRep.find(User.class, id);
            user.setCar(car);
        } catch (Exception e) {
            System.out.println("Error in service saveCarForUser - " + e);
        }
    }

    @Transactional
    @Override
    public void updateCar(Car car, long id) {
        try {
            User tempUser = genericRep.find(User.class, id);
            tempUser.setCar(car);
        } catch (Exception e) {
            System.out.println("Error in service updateCar - " + e);
        }
    }

    @Override
    public <T> List<T> getList(Class<T> cls) {
        try {
            return genericRep.allItems(cls);
        } catch (Exception e) {
            System.out.println("Error in service getList - " + e);
        }
        return null;
    }

    @Transactional
    @Override
    public void recreateTable() {
        try {
            genericRep.queryNameExecutor("Car.deleteTable");
            genericRep.queryNameExecutor("Car.createTable");
            genericRep.queryNameExecutor("User.deleteTable");
            genericRep.queryNameExecutor("User.createTable");
        } catch (Exception e) {
            System.out.println("Error in service recreateTable - " + e);
        }
    }

    @Transactional
    @Override
    public void resetTable() {
        try {
            genericRep.queryNameExecutor("User.cleanTable");
            genericRep.queryNameExecutor("Car.cleanTable");
        } catch (Exception e) {
            System.out.println("Error in service resetTable - " + e);
        }
    }

    @Override
    public <T> List<T> listSortById(Class<T> tClass) {
        try {
            List<T> list = genericRep.allItems(tClass);
            return list.stream()
                    .sorted(Comparator.comparingLong(UserTableServiceImp::getIdFromObject))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error in service listSortById - " + e);
        }
        return null;
    }
}
