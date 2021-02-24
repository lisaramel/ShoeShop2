import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:11
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Category {
/*
    SOMMARSKO,
    VINTERSKO,
    SPORTSKO,
    FINSKO,
    PROMENADSKO;

 */

    private int id;
    private String name;
    private List<Product> products = new ArrayList<>();

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProducts(Product product) {
        products.add(product);
    }
}
