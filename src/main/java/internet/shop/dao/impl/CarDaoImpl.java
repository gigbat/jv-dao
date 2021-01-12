package internet.shop.dao.impl;

import internet.shop.dao.CarDao;
import internet.shop.db.Storage;
import internet.shop.lib.Dao;
import internet.shop.model.Car;
import internet.shop.model.Manufacturer;
import java.util.List;
import java.util.Optional;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        Storage.addProductCar(car);
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        return Storage.getDbCar().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Car> getAll() {
        return Storage.getDbCar();
    }

    @Override
    public Car update(Car car) {
        List<Car> dbCar = Storage.getDbCar();
        for (int i = 0; i < dbCar.size(); i++) {
            if (car.getId().equals(dbCar.get(i).getId())) {
                dbCar.set(i, car);
                return car;
            }
        }
        throw new RuntimeException("You can't update " + car.getModel() + " in DB");
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getDbCar().removeIf(i -> i.getId().equals(id));
    }
}
