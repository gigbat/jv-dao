package internet.shop;

import internet.shop.lib.Injector;
import internet.shop.model.Car;
import internet.shop.model.Driver;
import internet.shop.model.Manufacturer;
import internet.shop.service.CarService;
import internet.shop.service.DriverService;
import internet.shop.service.ManufacturerService;

public class Demo {
    private static Injector injector = Injector.getInstance("internet.shop");

    public static void main(String[] args) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(10L);
        manufacturer.setCountry("Ukraine");
        manufacturer.setName("MiksMaks");

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setId(20L);
        manufacturer2.setCountry("Germany");
        manufacturer2.setName("Ulder");

        ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);

        System.out.println(manufacturerService.create(manufacturer));
        System.out.println(manufacturer.getId());
        System.out.println(manufacturerService.create(manufacturer2));
        System.out.println(manufacturer2.getId());

        System.out.println(manufacturerService.get(3L));
        System.out.println(manufacturerService.get(4L));

        System.out.println(manufacturerService.delete(1L));
        System.out.println(manufacturerService.delete(2L));
        System.out.println(manufacturerService.delete(700L));

        Manufacturer manufacturer3 = new Manufacturer();
        manufacturer3.setCountry("Italian");
        manufacturer3.setName("Pastarozo");
        manufacturer3.setId(3L);

        manufacturerService.update(manufacturer3);
        System.out.println(manufacturerService.get(3L));
        System.out.println(manufacturerService.get(100L));
        /*System.out.println("====================================");
        Driver driver1 = new Driver();
        driver1.setId(1L);
        driver1.setName("Vasya");
        driver1.setLicenceNumber("123-987-312");

        Driver driver2 = new Driver();
        driver2.setId(2L);
        driver2.setName("Petya");
        driver2.setLicenceNumber("1023-61253");

        Driver driver3 = new Driver();
        driver3.setId(3L);
        driver3.setName("Anton");
        driver3.setLicenceNumber("1244-1235-1235664");

        Car car1 = new Car();
        car1.setDrivers(driver1);
        car1.setId(1L);
        car1.setManufacturer(manufacturer);
        car1.setModel("Audi");

        Car car2 = new Car();
        car2.setDrivers(driver1);
        car2.setDrivers(driver2);
        car2.setId(2L);
        car2.setManufacturer(manufacturer2);
        car2.setModel("BMW");

        Car car3 = new Car();
        car3.setDrivers(driver1);
        car3.setDrivers(driver2);
        car3.setDrivers(driver3);
        car3.setId(3L);
        car3.setManufacturer(manufacturer3);
        car3.setModel("Mercedes");

        CarService carService = (CarService) injector
                .getInstance(CarService.class);

        carService.create(car1);
        carService.create(car2);
        carService.create(car3);
        printResultManufacturerCar(carService);

        carService.delete(1L);
        carService.delete(2L);
        carService.delete(3L);
        printResultManufacturerCar(carService);

        carService.create(car2);
        printResultManufacturerCar(carService);
        car3.setId(4L);
        car3.setModel("-");
        carService.update(car3);
        printResultManufacturerCar(carService);
        System.out.println("======================================================");
        DriverService driverService = (DriverService) injector
                .getInstance(DriverService.class);

        driverService.create(driver1);
        driverService.create(driver2);
        driverService.create(driver3);
        printResultManufacturerDriver(driverService);

        driverService.delete(1L);
        driverService.delete(2L);
        driverService.delete(3L);
        printResultManufacturerDriver(driverService);

        driverService.create(driver2);
        printResultManufacturerDriver(driverService);
        driver3.setId(4L);
        driver3.setLicenceNumber("000");
        driverService.update(driver3);
        printResultManufacturerDriver(driverService);*/
    }

    private static void printResultManufacturerCar(CarService carService) {
        List<Car> all = carService.getAll();
        all.forEach(System.out::println);
    }

    private static void printResultManufacturerDriver(DriverService driverService) {
        List<Driver> all = driverService.getAll();
        all.forEach(System.out::println);
    }
}
