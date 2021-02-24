import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:11
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Product {

    private int id;
    private int size;
    private String colour;
    private double price;
    private int brandId;
    private Brand brand;
    private int amountInStock;
    // category ?


    public Product(int id, int size, String colour, double price, int brandId, int amountInStock) {
        this.id = id;
        this.size = size;
        this.colour = colour;
        this.price = price;
        this.brandId = brandId;
        this.amountInStock = amountInStock;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        // slå upp Brand-objekt via brandId
        this.brand = brand;
    }

    public int getBrandId(){
        return brandId;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getColour() {
        return colour;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public String shopperView(){
        return colour + " " + brand.getName() + " - storlek " + size + ", " + price + " kr.";
    }

    public void lowerStock(){
        amountInStock -= 1;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", size=" + size +
                ", colour='" + colour + '\'' +
                ", price=" + price +
                ", brand=" + brand.getName() +
                ", amountInStock=" + amountInStock +
                '}';
    }
}
