package internet.shop.dao.impl;

import internet.shop.dao.ManufacturerDao;
import internet.shop.db.Storage;
import internet.shop.lib.Dao;
import internet.shop.model.Manufacturer;
import java.util.List;
import java.util.Optional;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        Storage.addProduct(manufacturer);
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        List<Manufacturer> dbManufacturer = Storage.getDbManufacturer();
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (id == dbManufacturer.get(i).getId()) {
                return Optional.of(dbManufacturer.get(i));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.getDbManufacturer();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        List<Manufacturer> dbManufacturer = Storage.getDbManufacturer();
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (manufacturer.getId() == dbManufacturer.get(i).getId()) {
                dbManufacturer.set(i, manufacturer);
                return manufacturer;
            }
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        List<Manufacturer> dbManufacturer = Storage.getDbManufacturer();
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (id == dbManufacturer.get(i).getId()) {
                dbManufacturer.remove(i);
                return true;
            }
        }
        return false;
    }
}
