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
        Driver driver1 = new Driver();
        driver1.setId(1L);
        driver1.setName("Bob");
        driver1.setLicenceNumber("123-321");

        Driver driver2 = new Driver();
        driver2.setId(2L);
        driver2.setName("Alice");
        driver2.setLicenceNumber("4821-00");

        Driver driver3 = new Driver();
        driver3.setId(100L);
        driver3.setName("John");
        driver3.setLicenceNumber("998-12-33");

        DriverService driverService = (DriverService) injector
                .getInstance(DriverService.class);

        driverService.create(driver1);
        driverService.create(driver2);
        driverService.create(driver3);

        System.out.println(driverService.get(3L));
        System.out.println(driverService.get(2L));
        System.out.println(driverService.get(1L));

        System.out.println(driverService.getAll());

        Driver driver4 = new Driver();
        driver4.setId(1L);
        driver4.setName("Vasya");
        driver4.setLicenceNumber("192429-1243324");

        driverService.update(driver4);
        System.out.println(driverService.get(1L));

        Driver driver5 = new Driver();
        driver5.setId(3L);
        driver5.setName("Petya");
        driver5.setLicenceNumber("7878-444");

        driverService.update(driver5);
        System.out.println(driverService.get(3L));

        /*driverService.delete(1L);
        driverService.delete(2L);
        driverService.delete(3L);*/
        System.out.println(driverService.getAll());
        System.out.println("=============================================");
        Manufacturer manufacturer1 = new Manufacturer();
        manufacturer1.setId(1L);
        manufacturer1.setCountry("Germany");
        manufacturer1.setName("Intro");

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setId(2L);
        manufacturer2.setCountry("Britain");
        manufacturer2.setName("Flex");

        Manufacturer manufacturer3 = new Manufacturer();
        manufacturer3.setId(3L);
        manufacturer3.setCountry("Ukraine");
        manufacturer3.setName("Kozak");

        ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);

        manufacturerService.create(manufacturer1);
        manufacturerService.create(manufacturer2);
        manufacturerService.create(manufacturer3);

        System.out.println(manufacturerService.get(3L));
        System.out.println(manufacturerService.get(2L));
        System.out.println(manufacturerService.get(1L));

        System.out.println(manufacturerService.getAll());

        Manufacturer manufacturer4 = new Manufacturer();
        manufacturer4.setId(2L);
        manufacturer4.setCountry("India");
        manufacturer4.setName("Elephant");

        Manufacturer manufacturer5 = new Manufacturer();
        manufacturer5.setId(1L);
        manufacturer5.setCountry("China");
        manufacturer5.setName("Samurai");

        manufacturerService.update(manufacturer4);
        manufacturerService.update(manufacturer5);

        System.out.println(manufacturerService.get(2L));
        System.out.println(manufacturerService.get(1L));

        /*manufacturerService.delete(1L);
        manufacturerService.delete(2L);
        manufacturerService.delete(3L);*/
        System.out.println(manufacturerService.getAll());
        System.out.println("====================================");

        Car car1 = new Car();
        car1.setId(1L);
        car1.setModel("Audi");
        car1.setDrivers(driver1);
        car1.setDrivers(driver2);
        car1.setDrivers(driver3);
        car1.setManufacturer(manufacturer1);

        Car car2 = new Car();
        car2.setId(2L);
        car2.setModel("Tesla");
        car2.setDrivers(driver1);
        car2.setDrivers(driver3);
        car2.setManufacturer(manufacturer2);

        Car car3 = new Car();
        car3.setId(2L);
        car3.setModel("BMW");
        car3.setDrivers(driver1);
        car3.setManufacturer(manufacturer3);

        CarService carService = (CarService) injector.getInstance(CarService.class);

        carService.create(car1);
        carService.create(car2);
        carService.create(car3);

        carService.update(car1);
        carService.update(car2);
        carService.update(car3);

        System.out.println(carService.get(3L));
        System.out.println(carService.get(2L));
        System.out.println(carService.get(1L).getDrivers());
        System.out.println(carService.getAll());

        System.out.println(carService.getAllByDriver(1L));
    }
}
