import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:08
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Customer {

    private int ID;
    private String userName;
    private String passWord;
    private String name;
    private String address;
    private int addressId;
    private List<Order> orders;

    public Customer(int id, String userName, String name) {
        this.ID = id;
        this.userName = userName;
        this.name = name;
    }


    public Customer(int ID, String name, int addressId, String userName, String passWord) {
        this.ID = ID;
        this.userName = userName;
        this.passWord = passWord;
        this.name = name;
        this.addressId = addressId;
    }

    public void ordersSetup(List<Order> orders){
        this.orders = orders;
    }

    public int getID() {
        return ID;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "ID=" + ID +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", addressId=" + addressId +
                '}';
    }

    public String getName() {
        return name;
    }
}
