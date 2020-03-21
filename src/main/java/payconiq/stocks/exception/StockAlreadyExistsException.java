package payconiq.stocks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception which will be thrown on attempt to add {@link payconiq.stocks.model.Stock}
 * with already existing name.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockAlreadyExistsException extends RuntimeException {

    public StockAlreadyExistsException(String message) {
        super(message);
    }
}
