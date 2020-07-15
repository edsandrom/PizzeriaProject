/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BL.Customer;
import BL.Order;
import BL.Toppings;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

/**
 * EJB
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Singleton
public class OrderDAL {

    private static OrderDAL instance;

    public static OrderDAL getInstance() {
        if (instance == null) {
            //first person in, we need to instantiate the object
            instance = new OrderDAL();
            conn = GetConnection();
        }
        return instance;
    }

    static private Connection conn;

    @PreDestroy
    public void CloseConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected OrderDAL() {

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
            conn.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(OrderDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public Order fetchOrderById(int id) {

        String sql = "SELECT * FROM orders WHERE orderId = ?";
        try {
            System.out.println(sql);
            Order order = new Order();
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                order.setId(id);
                order.setTotalPrice(rs.getBigDecimal(2));
                order.setDeliveryDateTime(rs.getTimestamp(3).toLocalDateTime());
                order.setPlacedDateTime(rs.getTimestamp(4).toLocalDateTime());
                order.setOrderStatus(rs.getString(6));
            }
            return order;
        } catch (SQLException ex) {
        }
        return null;
    }

    public boolean reviewOrder(Order order) {
        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "UPDATE orders SET totalPrice=?, deliveryDateTime=?, "
                    + "placedDateTime=?, orderStatus=? "
                    + "WHERE orderId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBigDecimal(1, order.getTotalPrice());
            statement.setTimestamp(2, Timestamp.valueOf(order.getDeliveryDateTime()));
            statement.setTimestamp(3, Timestamp.valueOf(order.getPlacedDateTime()));
            statement.setString(4, order.getOrderStatus());
            statement.setInt(5, order.getId());
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

    public BigDecimal totalOrder(Order o) {
        String sql = "SELECT price FROM pizza WHERE orderId = ?";
        BigDecimal total = new BigDecimal("0.00");
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, o.getId());
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                total.add(rs.getBigDecimal(1));
            }
            return total;
        } catch (SQLException ex) {
            return total;
        }
    }

    public ArrayList<Order> fetchAllValidOrders() {
        try {
            ArrayList<Order> orderList = new ArrayList<>();
            String select = "SELECT * FROM orders WHERE orderStatus != 'NOT AN OFFICIAL ORDER YET'";
            PreparedStatement statement = conn.prepareStatement(select);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("orderId"));
                order.setTotalPrice(rs.getBigDecimal("totalPrice"));
                order.setDeliveryDateTime(rs.getTimestamp("deliveryDateTime").toLocalDateTime());
                order.setPlacedDateTime(rs.getTimestamp("placedDateTime").toLocalDateTime());
                order.setOrderStatus(rs.getString("orderStatus"));
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean endOrder(int orderId) {
        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "UPDATE orders SET deliveryDateTime = CURRENT_TIMESTAMP, orderStatus = 'Finished'"
                    + "WHERE orderId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, orderId);
            row = statement.executeUpdate();
            System.out.println(statement);
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row > 0) {
            successFlag = true;
        }
        return successFlag;
    }

    public boolean orderStatusCompare(String orderStatus) {
        if (orderStatus.equalsIgnoreCase("Finished")) {
            return false;
        } else {
            return true;
        }
    }

}
