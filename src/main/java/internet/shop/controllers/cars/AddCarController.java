package internet.shop.controllers.cars;

import internet.shop.lib.Injector;
import internet.shop.model.Car;
import internet.shop.model.Manufacturer;
import internet.shop.service.CarService;
import internet.shop.service.ManufacturerService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCarController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private CarService carService = (CarService) injector
            .getInstance(CarService.class);
    private ManufacturerService manufacturerService = (ManufacturerService) injector
            .getInstance(ManufacturerService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cars/create_car.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Manufacturer manufacturer = manufacturerService
                .get(Long.parseLong(req.getParameter("manufacturer_id")));
        String model = req.getParameter("model");

        Car car = new Car();
        car.setManufacturer(manufacturer);
        car.setModel(model);

        carService.create(car);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
