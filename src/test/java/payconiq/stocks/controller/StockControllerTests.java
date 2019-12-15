package payconiq.stocks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import payconiq.stocks.exception.StockNotFoundException;
import payconiq.stocks.model.PriceHistory;
import payconiq.stocks.model.Stock;
import payconiq.stocks.repository.StockRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StockControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Test
    void testGetAllStocks() throws Exception {
        mockMvc.perform(
                get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[" +
                                "{\"id\":1,\"name\":\"London Stock\",\"currentPrice\":2.0,\"lastUpdate\":\"2019-12-11T22:58:34Z\"}," +
                                "{\"id\":4,\"name\":\"NewYork Stock\",\"currentPrice\":1.9,\"lastUpdate\":\"2019-12-11T23:59:56Z\"}" +
                                "]",
                        true
                ));
    }

    @Test
    void testGetStockById() throws Exception {
        mockMvc.perform(
                get("/api/stocks/4"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":4,\"name\":\"NewYork Stock\",\"currentPrice\":1.9,\"lastUpdate\":\"2019-12-11T23:59:56Z\"}",
                        true
                ));
    }

    @Test
    void testGetIncorrectId() throws Exception {
        Exception exception = mockMvc.perform(
                get("/api/stocks/2"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Stock with id 2 not found");
    }

    @Test
    void testGetUnparsableId() throws Exception {
        Exception exception = mockMvc.perform(
                get("/api/stocks/1_is_not_a_number"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isExactlyInstanceOf(MethodArgumentTypeMismatchException.class);
        assertThat(exception.getMessage()).isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'long'; nested exception is java.lang.NumberFormatException: For input string: \"1_is_not_a_number\"");
    }

    @Test
    void testGetHistory() throws Exception {
        mockMvc.perform(
                get("/api/stocks/4/history"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[" +
                                "{\"price\":1.9,\"startDate\":\"2019-12-11T23:59:56Z\",\"endDate\":null}," +
                                "{\"price\":1.94,\"startDate\":\"2019-12-10T07:03:00Z\",\"endDate\":\"2019-12-11T23:59:56Z\"}," +
                                "{\"price\":1.59,\"startDate\":\"2019-12-09T21:08:47Z\",\"endDate\":\"2019-12-10T07:03:00Z\"}" +
                                "]",
                        true
                ));
    }

    @Test
    void testGetHistoryIncorrectId() throws Exception {
        Exception exception = mockMvc.perform(
                get("/api/stocks/5/history"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Stock with id 5 not found");
    }

    @Test
    void testPostNewStock() throws Exception {
        Stock stock = new Stock();
        stock.setName("Tokyo Stock");
        stock.setCurrentPrice(2d);

        Instant timestampBeforeInsertion = Instant.now();

        mockMvc.perform(
                post("/api/stocks")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());

        Optional<Stock> expectedStock = stockRepository.findByName("Tokyo Stock");
        assertThat(expectedStock)
                .isPresent()
                .hasValueSatisfying(s -> Assertions.assertAll(
                        () -> assertThat(s.getId()).isNotNull(),
                        () -> assertThat(s.getName()).isEqualTo("Tokyo Stock"),
                        () -> assertThat(s.getCurrentPrice()).isEqualTo(2d),
                        () -> assertThat(s.getLastUpdate()).isAfter(timestampBeforeInsertion),
                        () -> {
                            List<PriceHistory> history = s.getHistory();
                            assertThat(history).isNotNull().hasSize(1);
                            PriceHistory priceHistory = history.get(0);
                            Assertions.assertAll(
                                    () -> assertThat(priceHistory.getId()).isNotNull(),
                                    () -> assertThat(priceHistory.getPrice()).isEqualTo(2d),
                                    () -> assertThat(priceHistory.getStock().getId()).isEqualTo(s.getId()),
                                    () -> assertThat(priceHistory.getStartDate()).isEqualTo(s.getLastUpdate()),
                                    () -> assertThat(priceHistory.getEndDate()).isNull()
                            );
                        }
                ));
    }

    @Test
    void testPutNewPrice() throws Exception {
        double newPrice = 2.1;

        Stock stock = new Stock();
        stock.setCurrentPrice(newPrice);

        Optional<Stock> expectedStock = stockRepository.findById(4L);
        assertThat(expectedStock).isPresent();
        Instant timestampBeforeUpdate = expectedStock.get().getLastUpdate();
        double priceBeforeUpdate = expectedStock.get().getCurrentPrice();
        int historySizeBeforeUpdate = expectedStock.get().getHistory().size();

        mockMvc.perform(
                put("/api/stocks/4")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());

        Optional<Stock> updatedStock = stockRepository.findById(4L);
        assertThat(updatedStock)
                .isPresent()
                .hasValueSatisfying(s -> Assertions.assertAll(
                        () -> assertThat(s.getCurrentPrice()).isEqualTo(newPrice),
                        () -> assertThat(s.getLastUpdate()).isAfter(timestampBeforeUpdate),
                        () -> {
                            List<PriceHistory> history = s.getHistory();
                            assertThat(history).isNotNull().hasSize(historySizeBeforeUpdate + 1);
                            PriceHistory lastPriceHistory = history.get(0);
                            Assertions.assertAll(
                                    () -> assertThat(lastPriceHistory.getId()).isNotNull(),
                                    () -> assertThat(lastPriceHistory.getPrice()).isEqualTo(newPrice),
                                    () -> assertThat(lastPriceHistory.getStock().getId()).isEqualTo(s.getId()),
                                    () -> assertThat(lastPriceHistory.getStartDate()).isEqualTo(s.getLastUpdate()),
                                    () -> assertThat(lastPriceHistory.getEndDate()).isNull()
                            );
                            PriceHistory previousPriceHistory = history.get(1);
                            Assertions.assertAll(
                                    () -> assertThat(previousPriceHistory.getPrice()).isEqualTo(priceBeforeUpdate),
                                    () -> assertThat(previousPriceHistory.getStock().getId()).isEqualTo(s.getId()),
                                    () -> assertThat(previousPriceHistory.getEndDate()).isEqualTo(s.getLastUpdate())
                            );
                        }
                ));
    }

    @Test
    void testPutNewPriceIncorrectId() throws Exception {
        Stock stock = new Stock();
        stock.setCurrentPrice(3d);

        Exception exception = mockMvc.perform(
                put("/api/stocks/6")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Stock with id 6 not found");
    }
}
