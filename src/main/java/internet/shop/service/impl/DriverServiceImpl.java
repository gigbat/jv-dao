package internet.shop.service.impl;

import internet.shop.dao.DriverDao;
import internet.shop.lib.Inject;
import internet.shop.lib.Service;
import internet.shop.model.Driver;
import internet.shop.service.DriverService;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Inject
    private DriverDao driverDao;

    @Override
    public Driver create(Driver driver) {
        return driverDao.create(driver);
    }

    @Override
    public Driver get(Long id) {
        return driverDao.get(id).orElseThrow(()
                -> new RuntimeException("Not exist id"));
    }

    @Override
    public List<Driver> getAll() {
        return driverDao.getAll();
    }

    @Override
    public Driver update(Driver driver) {
        if (driverDao.update(driver) == null) {
            throw new RuntimeException("Not exist driver");
        }
        return driverDao.update(driver);
    }

    @Override
    public boolean delete(Long id) {
        return driverDao.delete(id);
    }
}