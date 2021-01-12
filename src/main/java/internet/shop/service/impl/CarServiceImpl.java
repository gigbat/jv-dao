package internet.shop.service.impl;

import internet.shop.dao.CarDao;
import internet.shop.lib.Inject;
import internet.shop.lib.Service;
import internet.shop.model.Car;
import internet.shop.model.Driver;
import internet.shop.service.CarService;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Inject
    private CarDao carDao;

    @Override
    public Car create(Car car) {
        return carDao.create(car);
    }

    @Override
    public Car get(Long id) {
        return carDao.get(id).orElseThrow(()
                -> new RuntimeException("Not exist id"));
    }

    @Override
    public List<Car> getAll() {
        return carDao.getAll();
    }

    @Override
    public Car update(Car car) {
        if (carDao.update(car) == null) {
            throw new RuntimeException("Not exist car");
        }
        return carDao.update(car);
    }

    @Override
    public boolean delete(Long id) {
        return carDao.delete(id);
    }

    @Override
    public void addDriverToCar(Driver driver, Car car) {
        car.setDrivers(driver);
        if (!carDao.get(car.getId()).isEmpty()) {
            carDao.update(car);
            return;
        }
        carDao.create(car);
    }

    @Override
    public void removeDriverFromCar(Driver driver, Car car) {
        List<Driver> drivers = car.getDrivers();
        drivers.remove(drivers.get(drivers.indexOf(driver)));
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        List<Car> result = new ArrayList<>();
        List<Car> cars = carDao.getAll();
        for (int i = 0; i < cars.size(); i++) {
            List<Driver> drivers = cars.get(i).getDrivers();
            for (int j = 0; j < drivers.size(); j++) {
                if (driverId.equals(drivers.get(j).getId())) {
                    result.add(cars.get(i));
                }
            }
        }
        return result;
    }
}
