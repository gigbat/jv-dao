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
    private static final List<Manufacturer> manufacturers = new ArrayList<>();
    private static final List<Car> cars = new ArrayList<>();
    private static final List<Driver> drivers = new ArrayList<>();

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturers.add(manufacturer);
        manufacturer.setId(manufacturerId++);
    }

    public static void addCar(Car car) {
        cars.add(car);
        car.setId(carId++);
    }

    public static void addDriver(Driver driver) {
        drivers.add(driver);
        driver.setId(driverId++);
    }

    public static List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public static List<Car> getCars() {
        return cars;
    }

    public static List<Driver> getDrivers() {
        return drivers;
    }
}
