package payconiq.stocks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payconiq.stocks.exception.StockNotFoundException;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.StockRepository;

import java.time.Instant;
import java.util.Collection;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PriceHistoryService priceHistoryService;

    @Transactional
    public Collection<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional
    public Stock lookupStock(long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException("Stock with id " + id + " not found"));
    }

    @Transactional
    public Stock updateStockPrice(long id, double price) {
        Stock stock = lookupStock(id);
        stock.setCurrentPrice(price);
        return saveStockWithPrice(stock);
    }

    @Transactional
    public Stock addNewStock(Stock stock) {
        stock.setId(null);
        return saveStockWithPrice(stock);
    }

    @Transactional
    private Stock saveStockWithPrice(Stock stock) {
        Instant lastUpdate = Instant.now();
        stock.setLastUpdate(lastUpdate);

        Stock savedStock = stockRepository.save(stock);

        priceHistoryService.updateStockPrice(stock, lastUpdate);

        return savedStock;
    }
}
