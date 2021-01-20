package internet.shop.controllers.cars;

import internet.shop.lib.Injector;
import internet.shop.model.Car;
import internet.shop.service.CarService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllCarController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private CarService carService = (CarService) injector
            .getInstance(CarService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Car> cars = carService.getAll();
        req.setAttribute("cars", cars);
        req.getRequestDispatcher("/WEB-INF/views/car/get.jsp").forward(req, resp);
    }
}
