package internet.shop.controllers.drivers;

import internet.shop.lib.Injector;
import internet.shop.model.Driver;
import internet.shop.service.DriverService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllDriverController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private DriverService driverService = (DriverService) injector
            .getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Driver> drivers = driverService.getAll();

        req.setAttribute("drivers", drivers);
        req.getRequestDispatcher("/WEB-INF/views/drivers/get.jsp")
                .forward(req, resp);
    }
}
