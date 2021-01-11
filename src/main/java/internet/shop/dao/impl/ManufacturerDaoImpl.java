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
        return Optional.ofNullable(Storage.getProduct(id));
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.getDbManufacturer();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        return Optional.ofNullable(Storage.update(manufacturer))
                .orElseThrow(() -> new RuntimeException("Not exist manufacture"));
    }

    @Override
    public boolean delete(Long id) {
        return Storage.delete(id);
    }
}
