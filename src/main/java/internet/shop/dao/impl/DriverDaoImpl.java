package internet.shop.dao.impl;

import internet.shop.dao.DriverDao;
import internet.shop.db.Storage;
import internet.shop.lib.Dao;
import internet.shop.model.Car;
import internet.shop.model.Driver;
import java.util.List;
import java.util.Optional;

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
        for (int i = 0; i < dbDriver.size(); i++) {
            if (driver.getId().equals(dbDriver.get(i).getId())) {
                dbDriver.set(i, driver);
                return driver;
            }
        }
        throw new RuntimeException("You can't update " + driver.getName() + " in DB");
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getDbDriver().removeIf(i -> i.getId().equals(id));
    }
}
