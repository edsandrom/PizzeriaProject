/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BL.Employee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Singleton
public class EmployeeBean {

    public EmployeeBean() {

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
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Get Connection Method error");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void Disconnect(Connection conn) {

        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean checkLogin(String username, String password) {
        boolean successFlag = false;
        Connection conn = EmployeeBean.GetConnection();
        try {
            String sql = "SELECT username, password FROM employee WHERE username=? AND password=?";
            PreparedStatement statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next() == false) {
                System.out.println("Incorrect input info. Please try again.");
                successFlag = false;
            } else {
                successFlag = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            EmployeeBean.Disconnect(conn);
        }
        return successFlag;
    }

    public static boolean InsertEmployee(String username, String password) {
        Connection conn = EmployeeBean.GetConnection();
        boolean successFlag = false;
        int row = 0;
        try {
            String insert = "INSERT INTO employee (username, password) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            statement.setString(1, username);
            statement.setString(2, password);
            row = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row == 1) {
            successFlag = true;
        }
        return successFlag;
    }

    public static Employee FetchEmployeeByUserName(String username) {
        Connection conn = EmployeeBean.GetConnection();
        Employee emp = new Employee();
        try {
            String select = "SELECT * FROM employee WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(select);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                emp.setEmpId(rs.getInt(1));
                emp.setUsername(rs.getString(2));
                emp.setPassword(rs.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return emp;
    }
}
