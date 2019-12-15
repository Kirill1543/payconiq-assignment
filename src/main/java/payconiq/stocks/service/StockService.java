package payconiq.stocks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payconiq.stocks.exception.IncorrectStockStateException;
import payconiq.stocks.exception.StockAlreadyExistsException;
import payconiq.stocks.exception.StockNotFoundException;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.StockRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

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
        validatePrice(price);
        Stock stock = lookupStock(id);
        stock.setCurrentPrice(price);
        return saveStockWithPrice(stock);
    }

    @Transactional
    public Stock addNewStock(Stock stock) {
        validatePrice(stock.getCurrentPrice());
        validateName(stock);
        stock.setId(null);
        return saveStockWithPrice(stock);
    }

    @Transactional
    private Stock saveStockWithPrice(Stock stock) {
        Instant lastUpdate = Instant.now();
        stock.setLastUpdate(lastUpdate);

        Stock savedStock = stockRepository.save(stock);

        priceHistoryService.updateStockPrice(savedStock, lastUpdate);

        return savedStock;
    }

    private static void validatePrice(double price) {
        if (price <= 0) {
            throw new IncorrectStockStateException("Stock price should be greater than zero");
        }
    }

    private void validateName(Stock stock) {
        String name = stock.getName();
        if(name == null || name.isEmpty() || name.isBlank()) {
            throw new IncorrectStockStateException("Stock name can't be empty");
        }
        String cleanName = name.trim();
        Optional<Stock> actualStock = stockRepository.findByName(cleanName);
        if (actualStock.isPresent()) {
            throw new StockAlreadyExistsException("Stock already exists with name: " + cleanName);
        }
        stock.setName(cleanName);
    }
}
