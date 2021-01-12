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
        Storage.addProductDriver(driver);
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        return Storage.getDbDriver().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Driver> getAll() {
        return Storage.getDbDriver();
    }

    @Override
    public Driver update(Driver driver) {
        List<Driver> dbDriver = Storage.getDbDriver();
        IntStream.range(0, dbDriver.size())
                .filter(i -> dbDriver.get(i).equals(driver))
                .findFirst()
                .ifPresent(i -> dbDriver.set(i, driver));
        return driver;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getDbDriver().removeIf(i -> i.getId().equals(id));
    }
}
