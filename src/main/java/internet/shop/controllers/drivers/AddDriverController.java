package internet.shop.controllers.drivers;

import internet.shop.lib.Injector;
import internet.shop.model.Driver;
import internet.shop.service.DriverService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddDriverController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private DriverService driverService = (DriverService) injector
            .getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/drivers/create/create_driver.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String licenseNumber = req.getParameter("ln");

        Driver driver = new Driver();
        driver.setName(name);
        driver.setLicenceNumber(licenseNumber);

        driverService.create(driver);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
