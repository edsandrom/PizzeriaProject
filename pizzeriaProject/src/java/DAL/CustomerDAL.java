/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BL.Customer;
import java.awt.print.Book;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 * EJB
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Singleton
public class CustomerDAL {

    private static CustomerDAL instance;

    public static CustomerDAL getInstance() {
        if (instance == null) {
            //first person in, we need to instantiate the object
            instance = new CustomerDAL();
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
            Logger.getLogger(CustomerDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected CustomerDAL() {

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
            Logger.getLogger(CustomerDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public Customer fetchCustomerById(int id) {

        String sql = "SELECT customerId, firstName, lastName, phoneNumber, email, houseNumber, "
                + "street, province, postalCode FROM customer WHERE customerid = ?";
        try {
            Customer customer = new Customer();
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            rs.first();
            customer.setId(rs.getInt("customerId"));
            customer.setFirstname(rs.getString(2));
            customer.setLastname(rs.getString(3));
            customer.setPhoneNumber(rs.getString(4));
            customer.setEmail(rs.getString(5));
            customer.setHouseNumber(rs.getInt(6));
            customer.setStreet(rs.getString(7));
            customer.setProvince(rs.getString(8));
            customer.setPostalCode(rs.getString(9));
            return customer;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public boolean reviewCustomer(Customer customer) {
        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "UPDATE customer SET firstname=?, lastname=?, "
                    + "phonenumber=?, email=?, housenumber=?, street=?, provicne=?, postalcode=?    "
                    + "WHERE customerid = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customer.getFirstname());
            statement.setString(2, customer.getLastname());
            statement.setString(3, customer.getPhoneNumber());
            statement.setString(4, customer.getEmail());
            statement.setInt(5, customer.getHouseNumber());
            statement.setString(6, customer.getStreet());
            statement.setString(7, customer.getProvince());
            statement.setString(8, customer.getPostalCode());
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

    public int fetchCustomerId(int id) {
        int result = -1;
        String sql = "SELECT customerId FROM orders WHERE orderId = ?";
        try {
            System.out.println(sql);
            Customer customer = new Customer();
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                customer.setId(rs.getInt("customerId"));
            }
            result = customer.getId();
        } catch (SQLException ex) {
        }
        return result;
    }
}
