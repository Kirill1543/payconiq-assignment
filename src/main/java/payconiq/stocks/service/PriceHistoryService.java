package payconiq.stocks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.PriceHistoryRepository;

import java.time.Instant;

@Service
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public void updateStockPrice(Stock stock, Instant lastUpdate) {
        if (!stock.getHistory().isEmpty()) {
            PriceHistory lastPriceHistory = stock.getHistory().get(0);
            lastPriceHistory.setEndDate(lastUpdate);
            priceHistoryRepository.saveAndFlush(lastPriceHistory);
        }

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(stock.getCurrentPrice());
        priceHistory.setStock(stock);
        priceHistory.setStartDate(lastUpdate);
        priceHistoryRepository.saveAndFlush(priceHistory);
    }
}
