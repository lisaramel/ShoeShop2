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
                p.load(new FileInputStream("SkoButik/src/resources/Settings.properties"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


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
