package internet.shop.controllers.drivers;

import internet.shop.exception.DataProcessingException;
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
        req.getRequestDispatcher("/WEB-INF/views/drivers/create_driver.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("rpassword");

        if (!password.equals(repeatPassword)) {
            req.setAttribute("exception", "Password aren't the same");
            req.getRequestDispatcher("/WEB-INF/views/drivers/create_driver.jsp")
                    .forward(req, resp);
            return;
        }
        String name = req.getParameter("name");
        String licenseNumber = req.getParameter("ln");
        String login = req.getParameter("login");

        Driver driver = new Driver();
        driver.setName(name);
        driver.setLicenceNumber(licenseNumber);
        driver.setLogin(login);
        driver.setPassword(password);

        try {
            driverService.create(driver);
        } catch (DataProcessingException exception) {
            req.setAttribute("exception", "Login is existed or passwords aren't the same");
            req.getRequestDispatcher("/WEB-INF/views/drivers/create_driver.jsp")
                    .forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
