package internet.shop.dao.impl;

import internet.shop.dao.DriverDao;
import internet.shop.db.Storage;
import internet.shop.lib.Dao;
import internet.shop.model.Driver;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Dao
public class DriverDaoImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        Storage.addDriver(driver);
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        return Storage.getDrivers().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Driver> getAll() {
        return Storage.getDrivers();
    }

    @Override
    public Driver update(Driver driver) {
        List<Driver> dbDriver = Storage.getDrivers();
        IntStream.range(0, dbDriver.size())
                .filter(i -> dbDriver.get(i).equals(driver))
                .findFirst()
                .ifPresent(i -> dbDriver.set(i, driver));
        return driver;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getDrivers().removeIf(i -> i.getId().equals(id));
    }
}
