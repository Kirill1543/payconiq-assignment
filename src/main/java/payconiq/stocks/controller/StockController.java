package payconiq.stocks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.service.StockService;

import java.util.Collection;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public Collection<Stock> getStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStock(@PathVariable long id) {
        return stockService.lookupStock(id);
    }

    @GetMapping("/{id}/history")
    public Collection<PriceHistory> getHistory(@PathVariable long id) {
        return getStock(id).getHistory();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePrice(@RequestBody Stock newStock, @PathVariable long id) {
        stockService.updateStockPrice(id, newStock.getCurrentPrice());
        return ResponseEntity.ok("Stock price updated");
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addStock(@RequestBody Stock newStock) {
        stockService.addNewStock(newStock);
        return ResponseEntity.ok("New stock added");
    }
}
