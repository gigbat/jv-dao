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
        System.out.println(manufacturerService.create(manufacturer2));

        Manufacturer manufacturer3 = new Manufacturer();
        manufacturer3.setCountry("Italian");
        manufacturer3.setName("Pastarozo");

        System.out.println(manufacturerService.create(manufacturer3));
        System.out.println(manufacturerService.get(3L));
        System.out.println("====================================");
        Driver driver1 = new Driver();
        driver1.setName("Vasya");
        driver1.setLicenceNumber("123-987-312");

        Driver driver2 = new Driver();
        driver2.setName("Petya");
        driver2.setLicenceNumber("1023-61253");

        Driver driver3 = new Driver();
        driver3.setName("Anton");
        driver3.setLicenceNumber("1244-1235-1235664");

        DriverService driverService = (DriverService) injector
                .getInstance(DriverService.class);

        System.out.println(driverService.create(driver1));
        System.out.println(driverService.get(1L));
        System.out.println(driverService.create(driver2));
        System.out.println(driverService.get(2L));
        System.out.println(driverService.create(driver3));
        System.out.println(driverService.get(3L));

        System.out.println(driverService.create(driver1));
        System.out.println(driverService.create(driver2));
        driver3.setId(5L);
        System.out.println(driverService.update(driver3));
        System.out.println(driverService.getAll());
        System.out.println("=============================================");
        Car car1 = new Car();
        car1.setDrivers(driver1);
        car1.setManufacturer(manufacturer);
        car1.setModel("Audi");

        Car car2 = new Car();
        car2.setDrivers(driver1);
        car2.setDrivers(driver2);
        car2.setManufacturer(manufacturer2);
        car2.setModel("BMW");

        Car car3 = new Car();
        car3.setDrivers(driver1);
        car3.setDrivers(driver2);
        car3.setDrivers(driver3);
        car3.setManufacturer(manufacturer3);
        car3.setModel("Mercedes");

        Car car5 = new Car();
        car5.setId(1L);
        car5.setManufacturer(manufacturer);
        car5.setModel("Tesla");

        CarService carService = (CarService) injector
                .getInstance(CarService.class);

        System.out.println(carService.create(car1));
        System.out.println(carService.update(car5));
        System.out.println(carService.get(1L));
        System.out.println(carService.create(car2));
        System.out.println(carService.update(car5));
        System.out.println(carService.get(2L));
        System.out.println(carService.create(car3));
        System.out.println(carService.update(car5));
        System.out.println(carService.get(3L));

        carService.getAllByDriver(3L).forEach(System.out::println);

        Car car4 = new Car();
        car4.setId(4L);
        car4.setModel("ASdasd");

        Manufacturer manufacturer4 = new Manufacturer();
        manufacturer4.setId(10L);
        manufacturer4.setName("QWeqefdfg");
        manufacturer4.setCountry("Franc");

        car4.setManufacturer(manufacturer4);
        car4.setDrivers(driver3);
        car4.setDrivers(driver2);

        System.out.println(carService.update(car4));
        System.out.println("=====================================");
        System.out.println(carService.getAll());
    }
}
