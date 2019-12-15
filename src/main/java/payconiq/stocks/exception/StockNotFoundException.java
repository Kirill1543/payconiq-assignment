package payconiq.stocks.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception which will be thrown when there is no such {@link payconiq.stocks.model.Stock}
 * to perform some operation on it.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String message) {
        super(message);
    }
}
