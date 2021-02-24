import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:25
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Repository {

    private Properties p = new Properties();

    public Repository() {

        try {
            p.load(new FileInputStream("src/resources/Settings.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        String query = "select id, euSizing, colour, price, brandId, amountInStock from product";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product p = new Product(rs.getInt("id"), rs.getInt("euSizing"),
                        rs.getString("colour"), rs.getDouble("price"),
                        rs.getInt("brandId"), rs.getInt("amountInStock"));

                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;

    }

    public List<Rating> getRatings() {
        List<Rating> ratings = new ArrayList<>();

        String query = "select id, name, number, created from rating";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Rating r = new Rating(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("number"));

                ratings.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ratings;

    }


    public List<Brand> getBrands() {
        List<Brand> brands = new ArrayList<>();
        String query = "select id, name from brand";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Brand b = new Brand(rs.getInt("id"),
                        rs.getString("name"));
                brands.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public List<Order> getOrders(int customerId) {
        List<Order> orders = new ArrayList<>();
        // men egentligen behöver vi väl inte customerId
        String query = "select id, dateOfOrder, customerId from orders where customerId = ?";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Order o = new Order(rs.getInt("id"), rs.getString("dateOfOrder"), rs.getInt("customerId"));
                orders.add(o);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

        return orders;
    }

    public List<Integer> getCarts(int ordersId){
        List<Integer> productIds = new ArrayList<>();

        String query = "select productId from cart where ordersId = ?";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)) {

            stmt.setInt(1, ordersId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Integer i = rs.getInt("productId");
                productIds.add(i);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

        return productIds;


    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "select id, name from category";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Category c = new Category(rs.getInt("id"),
                        rs.getString("name"));
                categories.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void setRating(int customerId, int productId, int ratingId, String text) {
        String query1 = "CALL rate(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt1 = con.prepareCall(query1)){

            stmt1.setInt(1, customerId);
            stmt1.setInt(2, productId);
            stmt1.setInt(3, ratingId);
            stmt1.setString(4, text);
            stmt1.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
    }


   public List<CategoryBelonging> getCategoryBelongings(){
        List<CategoryBelonging> belongings = new ArrayList<>();
        String query = "select id, productId, categoryId from categoryBelonging";

        try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()){
                CategoryBelonging cb = new CategoryBelonging(rs.getInt("id"), rs.getInt("productId"), rs.getInt("categoryId"));
                belongings.add(cb);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return belongings;
    }

    public List<Customer> readCustomers() {
        List<Customer> customers = new ArrayList<>();

        String query = "select id, name, addressId, password, username from customer";


        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Customer c = new Customer(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("addressId"), rs.getString("password"),
                        rs.getString("username"));

                customers.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;

    }

    public String newOrder(int customerId, int ordersID, int productId) {
        ResultSet rs = null;
        String errorMsg = ""; // används för att ta emot select-sats med felmeddelande från sp
        String query = "CALL addToCart(?,?,?)";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, ordersID);
            stmt.setInt(3, productId);
            rs = stmt.executeQuery();

            while (rs != null && rs.next()) {
                errorMsg = rs.getString("error");
            }
            if (!errorMsg.equals("")) {
                return errorMsg;
            }


        } catch (Exception e) {
            e.printStackTrace();


        }
        return "Ny order!";
    }

    public List<String> getReviews(int productId){
        List<String> reviews = new ArrayList<>();

        String query = "select text, name, number from review join rating r on review.ratingId = r.id where productId = ?";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                StringBuilder sb = new StringBuilder();
                String text = rs.getString("text");
                String ratingName = rs.getString("name");
                int ratingGrade = rs.getInt("number");
                sb.append("Kommentar : " + text + '\n');
                sb.append("Betyg: " + ratingName + '\n');
                sb.append("Poäng: "+ ratingGrade + '\n');

                reviews.add(sb.toString());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }


        return reviews;
    }

    public double getAvgRating(int productId){
        double avgRate = 0;
        String query = "{? = call get_avg_rate(?)}";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)) {

            stmt.registerOutParameter(1, Types.DOUBLE);
            stmt.setInt(2, productId);
            stmt.execute();
            avgRate = stmt.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return avgRate;
    }

    public Customer getCustomer(String userName) {

        String query = "select * from customer where username like " + '\'' + userName + '\'';

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            Customer c = null;

            while (rs.next()) {
                c = new Customer(rs.getInt("id"), rs.getString("name"), rs.getInt("addressId"), rs.getString("username"), rs.getString("password"));
            }
            return c;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer signIn(String userName, String password) {

        String query = "select * from customer where username like " + '\'' + userName + '\'' + " and password like " + '\'' + password + '\'';

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

                Customer customer = null;

                while (rs.next()) {
                    customer = new Customer(rs.getInt("id"), rs.getString("name"), rs.getInt("addressId"), rs.getString("username"), rs.getString("password"));
                    //System.out.println("Välkommen " + customer.getName() + "!");
                }
                return customer;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String setNullValue(String tableName, String columnName) {
        String query = "insert into " + tableName + "(" + columnName + ") values ?";
        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
             PreparedStatement ps = con.prepareStatement(query);) {

            ps.setNull(1, Types.NULL);
            ps.executeUpdate();

           /* try (ResultSet rs = ps.executeQuery()) {

                String findNull = "";

                while (rs.next()) {
                    findNull = rs.getString(columnName);
                    return findNull;
                }
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
