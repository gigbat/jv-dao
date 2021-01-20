package internet.shop.controllers.cars;

import internet.shop.lib.Injector;
import internet.shop.model.Car;
import internet.shop.model.Driver;
import internet.shop.service.CarService;
import internet.shop.service.DriverService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddDriverToCarController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private final CarService carService = (CarService) injector
            .getInstance(CarService.class);
    private final DriverService driverService = (DriverService) injector
            .getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cars/create"
                + "/addDriverToCar/addDriverToCar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long driverId = Long.valueOf(request.getParameter("driver_id"));
        Long carId = Long.valueOf(request.getParameter("car_id"));

        Car car = carService.get(carId);
        Driver driver = driverService.get(driverId);

        carService.addDriverToCar(driver, car);
        response.sendRedirect("/");
    }
}
