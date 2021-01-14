package internet.shop.exception;

import java.sql.SQLException;

public class NoConnectException extends RuntimeException {
    public NoConnectException(String message, SQLException exception) {
        super(message, exception);
    }
}
