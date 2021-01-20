package internet.shop.controllers.manufacturers;

import internet.shop.lib.Injector;
import internet.shop.model.Manufacturer;
import internet.shop.service.ManufacturerService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllManufacturerController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private ManufacturerService manufacturerService = (ManufacturerService) injector
            .getInstance(ManufacturerService.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Manufacturer> manufacturers = manufacturerService.getAll();
        req.setAttribute("manufacturers", manufacturers);
        req.getRequestDispatcher("/WEB-INF/views/manufacturer/get.jsp").forward(req, resp);
    }
}
