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
        Storage.addManufacturer(manufacturer);
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        return Storage.getManufacturers().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.getManufacturers();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        List<Manufacturer> dbManufacturer = Storage.getManufacturers();
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (manufacturer.getId().equals(dbManufacturer.get(i).getId())) {
                dbManufacturer.set(i, manufacturer);
                return manufacturer;
            }
        }
        throw new RuntimeException("You can't update " + manufacturer + " in DB");
    }

    @Override
    public boolean delete(Long id) {
        return Storage.getManufacturers().removeIf(i -> i.getId().equals(id));
    }
}
