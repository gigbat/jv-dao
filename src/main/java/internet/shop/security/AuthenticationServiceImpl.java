package internet.shop.security;

import internet.shop.exception.AuthenticationException;
import internet.shop.lib.Inject;
import internet.shop.lib.Service;
import internet.shop.model.Driver;
import internet.shop.service.DriverService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private DriverService driverService;

    @Override
    public Driver login(String login, String password) throws AuthenticationException {
        Driver driver = driverService.findByLogin(login).orElseThrow(
                () -> new AuthenticationException("Incorrect login or password"));

        if (driver.getPassword().equals(password)) {
            return driver;
        }
        throw new AuthenticationException("Incorrect login or password");
    }
}
