package payconiq.stocks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import payconiq.stocks.exception.IncorrectRequestException;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.request.NewStockRequest;
import payconiq.stocks.request.PriceUpdateRequest;
import payconiq.stocks.service.StockService;

import java.util.Collection;

/**
 * Main Stocks REST controller
 */
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * Returns list of all {@link Stock}s.
     *
     * @return list of all {@link Stock}s.
     */
    @GetMapping
    @NonNull
    public Collection<Stock> getStocks() {
        return stockService.getAllStocks();
    }

    /**
     * Returns {@link Stock} by its id.
     *
     * @param id - id of stock to lookup.
     * @return {@link Stock} by its id.
     * @throws payconiq.stocks.exception.StockNotFoundException when there is no stock with such id.
     */
    @GetMapping("/{id}")
    @NonNull
    public Stock getStock(@PathVariable long id) {
        return stockService.lookupStock(id);
    }

    /**
     * Returns a price history for requested {@link Stock}.
     *
     * @param id - id of stock to lookup.
     * @return a price history for requested {@link Stock}.
     * @throws payconiq.stocks.exception.StockNotFoundException when there is no stock with such id.
     */
    @GetMapping("/{id}/history")
    @NonNull
    public Collection<PriceHistory> getHistory(@PathVariable long id) {
        return getStock(id).getHistory();
    }

    /**
     * Updates a price of a given stock.
     *
     * @param priceUpdateRequest - {@link PriceUpdateRequest} of price update.
     * @param id                 - id of stock to update.
     * @return update result response.
     * @throws payconiq.stocks.exception.StockNotFoundException       when there is no stock with such id.
     * @throws IncorrectRequestException when price is 0 or below.
     */
    @PutMapping("/{id}")
    @ResponseBody
    @NonNull
    public ResponseEntity<?> updatePrice(@RequestBody @NonNull PriceUpdateRequest priceUpdateRequest, @PathVariable long id) {
        stockService.updateStockPrice(id, priceUpdateRequest.getPrice());
        return ResponseEntity.ok("Stock price updated");
    }

    /**
     * Adding new stock by request.
     *
     * @param newStockRequest - {@link NewStockRequest} of new stock to add.
     * @return adding result response.
     * @throws IncorrectRequestException when price is 0 or below or stock name is empty.
     * @throws payconiq.stocks.exception.StockAlreadyExistsException  when stock with such name already exists.
     */
    @PostMapping
    @ResponseBody
    @NonNull
    public ResponseEntity<?> addStock(@RequestBody @NonNull NewStockRequest newStockRequest) {
        stockService.addNewStock(newStockRequest);
        return ResponseEntity.ok("New stock added");
    }
}
