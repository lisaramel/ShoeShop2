import javafx.util.Pair;

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

    private Connection con;
    private Properties p = new Properties();

        public Repository(){

            try {
                p.load(new FileInputStream("src/resources/Settings.properties"));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public List<Product> getProducts(){
            List<Product> products = new ArrayList<>();

            String query = "select id, euSizing, colour, price, brandId, amountInStock from product";

            try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)){

                while(rs.next()){
                    Product p = new Product(rs.getInt("id"), rs.getInt("euSizing"),
                            rs.getString("colour"), rs.getDouble("price"),
                            rs.getInt("brandId"), rs.getInt("amountInStock"));

                    products.add(p);
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

            return products;

        }

        public List<Object> getTables(String tableName){
            List<Object> list = new ArrayList<>();
            String query = "select id, name from ?";

            try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
                CallableStatement stmt = con.prepareCall(query)){

                ResultSet rs = stmt.executeQuery(query);

                stmt.setString(1, tableName);
                rs = stmt.executeQuery();

                if(tableName.equals("brand")){
                    while(rs.next()){
                        Brand b = new Brand(rs.getInt("id"),
                                rs.getString("name"));
                        list.add(b);
                    }
                } else if (tableName.equals("category")){
                    while(rs.next()){
                    Category c = new Category(rs.getInt("id"),
                            rs.getString("name"));
                    list.add(c);
                }  }


            }catch (SQLException e){
                e.printStackTrace();
            }
            return list;
        }


    public List<Brand> getBrands(){
        List<Brand> brands = new ArrayList<>();
        String query = "select id, name from brand";

        try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()){
                Brand b = new Brand(rs.getInt("id"),
                        rs.getString("name"));
                brands.add(b);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return brands;
    }


    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();
        String query = "select id, name from category";

        try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()){
                Category c = new Category(rs.getInt("id"),
                        rs.getString("name"));
                categories.add(c);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    public List<Pair> getCategoryBelongings(){
        List<Pair> belongings = new ArrayList<>();
        String query = "select productId, categoryId from categoryBelonging";

        try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()){
                Pair p = new Pair(rs.getInt("productId"), rs.getInt("categoryId"));
                belongings.add(p);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return belongings;
    }

    public List<Customer> readCustomers(){
            List<Customer> customers = new ArrayList<>();

            String query = "select id, name, addressId, password, username from customer";


            try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)){

                while(rs.next()){
                    Customer c = new Customer(rs.getInt("id"), rs.getString("name"),
                            rs.getInt("addressId"), rs.getString("password"),
                            rs.getString("username"));

                    customers.add(c);
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

            return customers;

        }

        public String newOrder(int customerId, int ordersID, int productId){
            ResultSet rs = null;
            String errorMsg = ""; // används för att ta emot select-sats med felmeddelande från sp
            String query = "CALL addToCart(?,?,?)";

            try(Connection con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));
                CallableStatement stmt = con.prepareCall(query)) {

                stmt.setInt(1, customerId);
                stmt.setInt(2, ordersID);
                stmt.setInt(3, productId);
                rs = stmt.executeQuery();

                while(rs != null && rs.next()){
                    errorMsg = rs.getString("error");
                }
                if(!errorMsg.equals("")){
                    return errorMsg;
                }



            } catch (Exception e){
                e.printStackTrace();


            }
            return "Ny order!";
        }


}
