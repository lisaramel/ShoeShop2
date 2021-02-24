import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 13:56
 * Project: ShoeShop2
. * Copywrite: MIT
 */
public class Order {

    private int id;
    private LocalDate date;
    private int customerId;
    private Customer customer;
    private List<Integer> productIds;
    private List<Product> products = new ArrayList<>();

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

    public void addProduct(Product p){
        products.add(p);
    }

    public List<Product> getProducts(){
        return products;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public String getOrderInfo(){
        double totalSum = 0;
        StringBuilder sb = new StringBuilder(date + "\n");
        for (Product p : products){
            sb.append(p.shopperView() +'\n');
            totalSum += p.getPrice();
        }
        sb.append("Det totala priset är " + totalSum);
        return sb.toString();

    }
}
