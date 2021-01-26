package internet.shop.controllers.drivers;

import internet.shop.lib.Injector;
import internet.shop.model.Car;
import internet.shop.service.CarService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMyCurrentCarsController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private static final String DRIVER_ID = "driver_id";
    private CarService carService = (CarService) injector
            .getInstance(CarService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Car> carList = carService.getAllByDriver((Long) req
                .getSession().getAttribute(DRIVER_ID));
        req.setAttribute("cars", carList);
        req.getRequestDispatcher("/WEB-INF/views/cars/get.jsp")
                .forward(req, resp);
    }
}
