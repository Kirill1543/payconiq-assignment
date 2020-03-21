package payconiq.stocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.PriceHistoryRepository;
import payconiq.stocks.repository.StockRepository;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

/**
 * Stocks Application main class. Is driven by Spring Boot 2.
 */
@SpringBootApplication
public class Application {

    /**
     * Application executing class.
     *
     * @param args - args which can be consumed by Spring Boot 2.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    /**
     * Initial state of the application on startup.
     * Is used for tests as well, so, when it will be decided
     * to remove it from here, it should be moved to test scope bean.
     */
    @PostConstruct
    void prepareStocks() {
        Instant stock1Update1 = LocalDateTime.of(2019, Month.DECEMBER, 11, 22, 56, 4).toInstant(ZoneOffset.UTC);
        Instant stock1Update2 = LocalDateTime.of(2019, Month.DECEMBER, 11, 22, 58, 34).toInstant(ZoneOffset.UTC);

        Stock stock1 = new Stock();
        stock1.setName("London Stock");
        stock1.setCurrentPrice(2d);
        stock1.setLastUpdate(stock1Update2);
        Stock createdStock1 = stockRepository.saveAndFlush(stock1);

        PriceHistory stock1History1 = new PriceHistory();
        stock1History1.setStartDate(stock1Update1);
        stock1History1.setEndDate(stock1Update2);
        stock1History1.setPrice(2.06);
        stock1History1.setStock(createdStock1);
        priceHistoryRepository.saveAndFlush(stock1History1);

        PriceHistory stock1History2 = new PriceHistory();
        stock1History2.setStartDate(stock1Update2);
        stock1History2.setPrice(2d);
        stock1History2.setStock(createdStock1);
        priceHistoryRepository.saveAndFlush(stock1History2);

        Instant stock2Update1 = LocalDateTime.of(2019, Month.DECEMBER, 9, 21, 8, 47).toInstant(ZoneOffset.UTC);
        Instant stock2Update2 = LocalDateTime.of(2019, Month.DECEMBER, 10, 7, 3, 0).toInstant(ZoneOffset.UTC);
        Instant stock2Update3 = LocalDateTime.of(2019, Month.DECEMBER, 11, 23, 59, 56).toInstant(ZoneOffset.UTC);

        Stock stock2 = new Stock();
        stock2.setName("NewYork Stock");
        stock2.setCurrentPrice(1.9);
        stock2.setLastUpdate(stock2Update3);
        Stock createdStock2 = stockRepository.saveAndFlush(stock2);

        PriceHistory stock2History1 = new PriceHistory();
        stock2History1.setStartDate(stock2Update1);
        stock2History1.setEndDate(stock2Update2);
        stock2History1.setPrice(1.59);
        stock2History1.setStock(createdStock2);
        priceHistoryRepository.saveAndFlush(stock2History1);

        PriceHistory stock2History2 = new PriceHistory();
        stock2History2.setStartDate(stock2Update2);
        stock2History2.setEndDate(stock2Update3);
        stock2History2.setPrice(1.94);
        stock2History2.setStock(createdStock2);
        priceHistoryRepository.saveAndFlush(stock2History2);

        PriceHistory stock2History3 = new PriceHistory();
        stock2History3.setStartDate(stock2Update3);
        stock2History3.setPrice(1.9);
        stock2History3.setStock(createdStock2);
        priceHistoryRepository.saveAndFlush(stock2History3);
    }
}
