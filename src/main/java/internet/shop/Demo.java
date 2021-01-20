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
        driver1.setName("Bob");
        driver1.setLicenceNumber("123-321");

        Driver driver2 = new Driver();
        driver2.setName("Alice");
        driver2.setLicenceNumber("4821-00");

        Driver driver3 = new Driver();
        driver3.setName("John");
        driver3.setLicenceNumber("998-12-33");

        DriverService driverService = (DriverService) injector
                .getInstance(DriverService.class);

        driverService.create(driver1);
        driver1.setName("asd");
        driver1.setLicenceNumber("000-000");
        driverService.update(driver1);
        driverService.create(driver2);
        driverService.create(driver3);

        System.out.println(driverService.get(driver3.getId()));
        System.out.println(driverService.get(driver2.getId()));
        System.out.println(driverService.get(driver1.getId()));

        System.out.println(driverService.getAll());

        Driver driver4 = new Driver();
        driver4.setName("Vasya");
        driver4.setLicenceNumber("192429-1243324");

        driverService.create(driver4);
        driver4.setName("Ivan");
        driverService.update(driver4);
        System.out.println(driverService.get(driver4.getId()));

        Driver driver5 = new Driver();
        driver5.setName("Petya");
        driver5.setLicenceNumber("7878-444");

        driverService.create(driver5);
        System.out.println(driverService.get(driver5.getId()));

        System.out.println(driverService.getAll());
        System.out.println("=============================================");
        Manufacturer manufacturer1 = new Manufacturer();
        manufacturer1.setCountry("Germany");
        manufacturer1.setName("Intro");

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCountry("Britain");
        manufacturer2.setName("Flex");

        Manufacturer manufacturer3 = new Manufacturer();
        manufacturer3.setCountry("Ukraine");
        manufacturer3.setName("Kozak");

        ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);

        manufacturerService.create(manufacturer1);
        manufacturer1.setName("Lamba");
        manufacturerService.update(manufacturer1);
        manufacturerService.create(manufacturer2);
        manufacturerService.create(manufacturer3);

        System.out.println(manufacturerService.get(manufacturer3.getId()));
        System.out.println(manufacturerService.get(manufacturer2.getId()));
        System.out.println(manufacturerService.get(manufacturer1.getId()));

        System.out.println(manufacturerService.getAll());

        Manufacturer manufacturer4 = new Manufacturer();
        manufacturer4.setCountry("India");
        manufacturer4.setName("Elephant");

        Manufacturer manufacturer5 = new Manufacturer();
        manufacturer5.setCountry("China");
        manufacturer5.setName("Samurai");

        manufacturerService.create(manufacturer4);
        manufacturerService.create(manufacturer5);
        manufacturer5.setName("asd");
        manufacturer5.setCountry("kfgjdfk");
        manufacturerService.update(manufacturer5);

        System.out.println(manufacturerService.get(manufacturer4.getId()));
        System.out.println(manufacturerService.get(manufacturer5.getId()));

        System.out.println(manufacturerService.getAll());
        System.out.println("====================================");

        Car car1 = new Car();
        car1.setModel("Audi");
        car1.setManufacturer(manufacturer1);

        Car car2 = new Car();
        car2.setModel("Tesla");
        car2.setManufacturer(manufacturer2);

        Car car3 = new Car();
        car3.setModel("BMW");
        car3.setManufacturer(manufacturer3);

        CarService carService = (CarService) injector.getInstance(CarService.class);

        carService.create(car1);
        carService.create(car2);
        carService.create(car3);

        carService.addDriverToCar(driver1, car1);
        carService.addDriverToCar(driver2, car1);
        carService.addDriverToCar(driver3, car1);

        carService.addDriverToCar(driver1, car2);
        carService.addDriverToCar(driver3, car2);

        carService.addDriverToCar(driver1, car3);

        Car car4 = new Car();
        car4.setModel("QWqsdasf");
        car4.addDriver(driver1);
        car4.setManufacturer(manufacturer3);

        carService.create(car4);

        car1.setModel("Reno");
        carService.update(car1);
        car2.setModel("x5");
        carService.update(car2);

        System.out.println(carService.get(car1.getId()));
        System.out.println(carService.get(car4.getId()));
        System.out.println(carService.get(car2.getId()).getDrivers());
        System.out.println(carService.getAll());

        System.out.println(carService.getAllByDriver(driver1.getId()));
    }
}
