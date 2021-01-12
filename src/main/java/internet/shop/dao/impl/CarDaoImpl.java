package internet.shop.dao.impl;

import internet.shop.dao.CarDao;
import internet.shop.db.Storage;
import internet.shop.lib.Dao;
import internet.shop.model.Car;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        IntStream.range(0, dbCar.size())
                .filter(i -> dbCar.get(i).equals(car))
                .findFirst()
                .ifPresent(i -> dbCar.set(i, car));
        return car;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getDbCar().removeIf(i -> i.getId().equals(id));
    }
}
