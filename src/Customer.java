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
}
