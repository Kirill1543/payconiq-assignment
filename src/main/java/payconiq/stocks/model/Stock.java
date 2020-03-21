package payconiq.stocks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Stock entity which contains info about stock, its name and price.
 * Is linked with the {@link PriceHistory}.
 */
@Entity
public class Stock {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private double currentPrice;

    private Instant lastUpdate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stock")
    @OrderBy("startDate DESC")
    @JsonIgnore
    private List<PriceHistory> history = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public List<PriceHistory> getHistory() {
        return history;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setHistory(List<PriceHistory> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdate=" + lastUpdate +
                ", history=" + history +
                '}';
    }
}
