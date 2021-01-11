package internet.shop.db;

import internet.shop.model.Manufacturer;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static long manufacturerId = 1;
    private static final List<Manufacturer> dbManufacturer = new ArrayList<>();

    public static void addProduct(Manufacturer manufacturer) {
        dbManufacturer.add(manufacturer);
        manufacturer.setId(manufacturerId++);
    }

    public static List<Manufacturer> getDbManufacturer() {
        return dbManufacturer;
    }
}
