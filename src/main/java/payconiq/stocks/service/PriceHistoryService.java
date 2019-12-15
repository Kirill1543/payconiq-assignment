package payconiq.stocks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.PriceHistoryRepository;

import java.time.Instant;

/**
 * Service to perform business logic for {@link PriceHistory} entities.
 */
@Service
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    /**
     * Updates price history for specified {@link Stock}.
     * Closes current last price history record with provided timestamp (if present).
     * Opens new price history record with empty {@link PriceHistory#getEndDate()}.
     *
     * @param stock      - stock to update price for.
     * @param lastUpdate - timestamp which was taken for {@link Stock#getLastUpdate()}.
     */
    @Transactional
    public void updateStockPrice(@NonNull Stock stock, @NonNull Instant lastUpdate) {
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
