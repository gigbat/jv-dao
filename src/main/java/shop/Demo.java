package shop;

import internet.shop.lib.Injector;
import internet.shop.model.Manufacturer;
import internet.shop.service.ManufacturerService;
import java.util.List;

public class Demo {
    private static Injector injector = Injector.getInstance("internet.shop");

    public static void main(String[] args) {
        Manufacturer manufacturer = new Manufacturer();
        Manufacturer manufacturer2 = new Manufacturer();

        manufacturer.setCountry("Ukraine");
        manufacturer.setName("MiksMaks");

        manufacturer2.setCountry("Germany");
        manufacturer2.setName("Ulder");

        ManufacturerService manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);

        manufacturerService.create(manufacturer);
        manufacturerService.create(manufacturer2);

        printResult(manufacturerService);
        manufacturerService.delete(1L);
        manufacturerService.delete(3L);
        printResult(manufacturerService);

        Manufacturer manufacturer3 = new Manufacturer();
        manufacturer3.setCountry("Italian");
        manufacturer3.setName("Pastarozo");
        manufacturer3.setId(2L);

        manufacturerService.update(manufacturer3);
        printResult(manufacturerService);
    }

    private static void printResult(ManufacturerService manufacturerService) {
        List<Manufacturer> all = manufacturerService.getAll();
        for (int i = 0; i < all.size(); i++) {
            System.out.println("Id: " + all.get(i).getId()
                    + ", country: " + all.get(i).getCountry()
                    + ", name: " + all.get(i).getName());
        }
    }
}
