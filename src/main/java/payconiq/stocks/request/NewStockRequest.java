package payconiq.stocks.request;

/**
 * Class containing data about incoming requests to add new stocks.
 */
public class NewStockRequest {

    private String name;

    private Double price;

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "NewStockRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
