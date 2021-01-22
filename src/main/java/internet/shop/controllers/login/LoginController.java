package internet.shop.controllers.login;

import internet.shop.exception.AuthenticationException;
import internet.shop.lib.Injector;
import internet.shop.model.Driver;
import internet.shop.security.AuthenticationService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("internet.shop");
    private AuthenticationService authenticationService = (AuthenticationService) injector
            .getInstance(AuthenticationService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/accounts/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            Driver driver = authenticationService.login(login, password);
            HttpSession session = req.getSession();
            session.setAttribute("driver_id", driver.getId());
        } catch (AuthenticationException e) {
            req.setAttribute("exception", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/accounts/login.jsp")
                    .forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
