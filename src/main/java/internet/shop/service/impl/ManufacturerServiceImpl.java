package internet.shop.service.impl;

import internet.shop.dao.ManufacturerDao;
import internet.shop.lib.Inject;
import internet.shop.lib.Service;
import internet.shop.model.Manufacturer;
import internet.shop.service.ManufacturerService;
import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Inject
    private ManufacturerDao manufacturerDao;

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        manufacturerDao.create(manufacturer);
        return manufacturer;
    }

    @Override
    public Manufacturer get(Long id) {
        return manufacturerDao.get(id)
                .orElseThrow(() -> new RuntimeException("Not exist id"));
    }

    @Override
    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        if (manufacturerDao.update(manufacturer) == null) {
            throw new RuntimeException("Not exist manufacturer");
        }
        return manufacturerDao.update(manufacturer);
    }

    @Override
    public boolean delete(Long id) {
        manufacturerDao.delete(id);
        return false;
    }
}
