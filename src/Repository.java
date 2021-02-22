import java.io.FileInputStream;
import java.sql.*;
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

    public Repository() {
        try {
            p.load(new FileInputStream("src/Settings.properties"));
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public String justToRemember(String name, String elfInCharge) {

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt1 = con.prepareStatement("SELECT id FROM nissar WHERE name = ?");
             CallableStatement stmt2 = con.prepareCall("CALL addPresent(?,?)");) {

            stmt1.setString(1, elfInCharge);
            ResultSet rs = stmt1.executeQuery();

            int elfInChargeId = 0;

            while(rs.next()){
                elfInChargeId = rs.getInt("id");
            }

            stmt2.setString(1, name);
            stmt2.setInt(2, elfInChargeId);
            stmt2.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }*/

}
