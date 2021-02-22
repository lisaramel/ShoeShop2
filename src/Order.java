import java.time.LocalDate;
import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 13:56
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Order {

    private int id;
    private LocalDate date;
    private int customerId;
    private Customer customer;
    private List<Integer> productIds;

    public Order(int id, String date, int customerId) {
        this.id = id;
        this.date = LocalDate.parse(date);
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}
