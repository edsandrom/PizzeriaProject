/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BL.Employee;
import BL.Toppings;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Singleton
public class ToppingsDAL {

    public ToppingsDAL() {
    }

    public static Connection GetConnection() {
        Connection conn = null;
        //JDBC
        String dbURL = "jdbc:mysql://localhost:3306/pizzadb?useSSL=false";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Get Connection Method error");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void Disconnect(Connection conn) {

        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertTopping(Toppings top, int empId) {
        Connection conn = ToppingsDAL.GetConnection();
        boolean successFlag = false;
        int row = 0;
        try {
            BigDecimal bd = top.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            String insert = "INSERT INTO toppings (name, price, createdate, empAddedBy, isActive) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            statement.setString(1, top.getName());
            statement.setBigDecimal(2, bd.setScale(2, BigDecimal.ROUND_HALF_EVEN));
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(4, empId);
            statement.setBoolean(5, top.isIsActive());
            row = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row == 1) {
            successFlag = true;
        }
        return successFlag;
    }

    public boolean removeTopping(Toppings top) {
        Connection conn = ToppingsDAL.GetConnection();

        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "DELETE FROM toppings WHERE name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, top.getName());
            row = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row == 1) {
            successFlag = true;
        }
        return successFlag;
    }

    public boolean reviseTopping(Toppings top) {
        Connection conn = ToppingsDAL.GetConnection();

        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "UPDATE toppings SET price=?, isActive=? WHERE toppingId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBigDecimal(1, top.getPrice());
            statement.setBoolean(2, top.isIsActive());
            statement.setInt(3, top.getId());
            row = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row == 1) {
            successFlag = true;
        }
        return successFlag;
    }

    public ArrayList<Toppings> fetchAllToppings() {
        Connection conn = ToppingsDAL.GetConnection();
        try {
            ArrayList<Toppings> toppingsList = new ArrayList<>();
            String select = "SELECT * FROM toppings";
            PreparedStatement statement = conn.prepareStatement(select);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Toppings top = new Toppings(rs.getInt("toppingId"), rs.getString("name"), rs.getBigDecimal("price"), rs.getBoolean("isActive"));
                toppingsList.add(top);
            }
            return toppingsList;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Toppings fetchToppingById(int toppingId) {
        Connection conn = ToppingsDAL.GetConnection();
        try {
            Toppings topping = new Toppings();
            topping.setId(toppingId);
            String select = "SELECT * FROM toppings WHERE toppingId=?";
            PreparedStatement statement = conn.prepareStatement(select);
            statement.setInt(1, topping.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                topping.setName(rs.getString(2));
                topping.setPrice(rs.getBigDecimal(3));
                topping.setIsActive(rs.getBoolean(6));
            }
            return topping;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<Toppings> fetchAllAvailableToppings() {
        Connection conn = ToppingsDAL.GetConnection();
        try {
            ArrayList<Toppings> toppingsList = new ArrayList<>();
            String select = "SELECT * FROM toppings WHERE isActive = 1";
            PreparedStatement statement = conn.prepareStatement(select);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Toppings top = new Toppings(rs.getInt("toppingId"), rs.getString("name"), rs.getBigDecimal("price"), rs.getBoolean("isActive"));
                toppingsList.add(top);
            }
            return toppingsList;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<Toppings> fetchPizzaToppings(int pizzaId) {
        Connection conn = ToppingsDAL.GetConnection();
        try {
            ArrayList<Toppings> toppingsList = new ArrayList<>();
            String select = "SELECT t.toppingId, t.name FROM toppings t "
                    + "INNER JOIN pizza_toppings_map m ON t.toppingId = m.toppingId "
                    + "WHERE m.pizzaId = ?";
            PreparedStatement statement = conn.prepareStatement(select);
            statement.setInt(1, pizzaId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Toppings top = new Toppings();
                top.setName(rs.getString("name"));
                top.setId(rs.getInt("toppingId"));
                toppingsList.add(top);
            }
            return toppingsList;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
