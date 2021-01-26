package internet.shop.security;

import internet.shop.exception.AuthenticationException;
import internet.shop.model.Driver;

public interface AuthenticationService {
    Driver login(String login, String password) throws AuthenticationException;
}
