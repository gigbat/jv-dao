package internet.shop.web.filters;

import internet.shop.lib.Injector;
import internet.shop.service.DriverService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {
    private static final String DRIVER_ID = "driver_id";
    private static final Set<String> urls = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urls.add("/login");
        urls.add("/drivers/create");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = req.getServletPath();
        if (urls.contains(url)) {
            filterChain.doFilter(req, resp);
            return;
        }

        Long id = (Long) req.getSession().getAttribute(DRIVER_ID);
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
