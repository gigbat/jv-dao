package internet.shop.db;

import internet.shop.model.Manufacturer;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static long manufacturerId = 1;
    private static final List<Manufacturer> dbManufacturer = new ArrayList<>();

    public static void addProduct(Manufacturer manufacturer) {
        manufacturer.setId(manufacturerId++);
        dbManufacturer.add(manufacturer);
    }

    public static Manufacturer getProduct(Long id) {
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (id == dbManufacturer.get(i).getId()) {
                return dbManufacturer.get(i);
            }
        }
        return null;
    }

    public static List<Manufacturer> getDbManufacturer() {
        return dbManufacturer;
    }

    public static boolean delete(Long id) {
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (id == dbManufacturer.get(i).getId()) {
                dbManufacturer.remove(i);
                return true;
            }
        }
        return false;
    }

    public static Manufacturer update(Manufacturer manufacturer) {
        boolean isUpdated = false;
        for (int i = 0; i < dbManufacturer.size(); i++) {
            if (manufacturer.getId() == dbManufacturer.get(i).getId()) {
                dbManufacturer.remove(i);
                isUpdated = true;
                break;
            }
        }
        if (isUpdated) {
            dbManufacturer.add(manufacturer);
            return manufacturer;
        } else {
            return null;
        }
    }
}
