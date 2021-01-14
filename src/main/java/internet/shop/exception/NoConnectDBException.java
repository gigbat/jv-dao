package internet.shop.exception;

import java.sql.SQLException;

public class NoConnectDBException extends RuntimeException {
    public NoConnectDBException(String message, SQLException exception) {
        super(message, exception);
    }
}
