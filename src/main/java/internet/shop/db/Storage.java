package internet.shop.db;

import internet.shop.model.Car;
import internet.shop.model.Driver;
import internet.shop.model.Manufacturer;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static long manufacturerId = 1;
    private static long carId = 1;
    private static long driverId = 1;
    private static final List<Manufacturer> dbManufacturer = new ArrayList<>();
    private static final List<Car> dbCar = new ArrayList<>();
    private static final List<Driver> dbDriver = new ArrayList<>();

    public static void addProductManufacturer(Manufacturer manufacturer) {
        dbManufacturer.add(manufacturer);
        manufacturer.setId(manufacturerId++);
    }

    public static void addProductCar(Car car) {
        dbCar.add(car);
        car.setId(carId++);
    }

    public static void addProductDriver(Driver driver) {
        dbDriver.add(driver);
        driver.setId(driverId++);
    }

    public static List<Manufacturer> getDbManufacturer() {
        return dbManufacturer;
    }

    public static List<Car> getDbCar() {
        return dbCar;
    }

    public static List<Driver> getDbDriver() {
        return dbDriver;
    }
}
