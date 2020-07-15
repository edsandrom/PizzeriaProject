/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BL.Pizza;
import BL.Toppings;
import java.awt.print.Book;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class PizzaDAL {

    private static PizzaDAL instance;

    public static PizzaDAL getInstance() {
        if (instance == null) {
            instance = new PizzaDAL();
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
            Logger.getLogger(PizzaDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected PizzaDAL() {

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
            Logger.getLogger(PizzaDAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public BigDecimal calcPizzaPrice(Pizza pizza) { //CalculatePrice() in Business Layer
        BigDecimal total = pizza.getPrice();
        BigDecimal sz = new BigDecimal("0");
        BigDecimal bd = new BigDecimal("0");

        if (pizza.getCrustType() != "" && pizza.getSize() != "") {

            if (pizza.getCrustType().equalsIgnoreCase("Thin Crust")) {
                bd = new BigDecimal("0.00");
            } else if (pizza.getCrustType().equalsIgnoreCase("Handmade Pan")) {
                bd = new BigDecimal("5.00");
            } else if (pizza.getCrustType().equalsIgnoreCase("Original")) {
                bd = new BigDecimal("3.25");
            } else if (pizza.getCrustType().equalsIgnoreCase("Gluten")) {
                bd = new BigDecimal("1.50");
            } else {
                bd = new BigDecimal("6.00");
            }
        }
//            switch (pizza.getSize()) {
        if (pizza.getSize().equalsIgnoreCase("small")) {
            sz = (new BigDecimal("12.00"));
        } else if (pizza.getSize().equalsIgnoreCase("medium")) {
            sz = (new BigDecimal("15.00"));
        } else {
            sz = (new BigDecimal("16.50"));
        }

    total  = bd.add(sz);

    if (instance.listToppings (pizza.getPizzaId 
        ()) != null) {
            ArrayList<Toppings> toppingsList = listToppings(pizza.getPizzaId());
        for (int i = 0; i < toppingsList.size(); i++) {
            total.add(toppingsList.get(i).getPrice());
        }
    }

    return total.multiply (

new BigDecimal("1.15"));
    }

    public ArrayList<Toppings> listToppings(int pizzaId) {//GetToppings() in Business Layer
        String sql = "SELECT t.toppingId, t.name, t.price "
                + "FROM pizza_toppings_map m INNER JOIN toppings t ON "
                + "t.toppingId = m.toppingId "
                + "WHERE m.pizzaId = ? ORDER BY t.toppingId ASC";
        try {
            ArrayList<Toppings> toppingsList = new ArrayList<>();
            PreparedStatement statement = conn.prepareCall(sql);
            statement.setInt(1, pizzaId);
            ResultSet rs = statement.executeQuery();
            rs.first();
            while (rs.next()) {
                Toppings t = new Toppings();
                t.setId(rs.getInt(1));
                t.setName(rs.getString(2));
                t.setPrice(rs.getBigDecimal(3));
                toppingsList.add(t);
            }
            return toppingsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertPizza(Pizza p, int orderId, ArrayList<Toppings> toppingsList) {
        String sql = "INSERT INTO pizza(sizeId, isFinished, crustTypeId, price, orderId) "
                + "VALUES(?, ?, ?, ?, ?);";
        try {
            int pizzaId = -1;
            int row = 0;
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, fetchSizeByName(p.getSize()));
            statement.setBoolean(2, p.isIsFinished());
            statement.setInt(3, fetchCrustByName(p.getCrustType()));
            statement.setBigDecimal(4, p.getPrice());
            statement.setInt(5, orderId);

            if (statement.executeUpdate() > 0) {
                ResultSet genKeys = statement.getGeneratedKeys();
                if (genKeys.next()) {
                    pizzaId = genKeys.getInt(1);
                }
                String topMapsSQL = "INSERT INTO pizza_toppings_map (toppingId, pizzaId) "
                        + "VALUES (?, ?)";
                PreparedStatement topMapsStatement = conn.prepareStatement(topMapsSQL);
                for (int i = 0; i < toppingsList.size(); i++) {
                    topMapsStatement.setInt(1, toppingsList.get(i).getId());
                    topMapsStatement.setInt(2, pizzaId);
                    row += topMapsStatement.executeUpdate();
                }
                if (row > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Pizza fetchPizzaById(int pizzaId) {
        try {
            Pizza pizza = new Pizza();
            pizza.setPizzaId(pizzaId);
            String select = "SELECT * FROM pizza WHERE pizzaId=?";
            PreparedStatement statement = conn.prepareStatement(select);
            statement.setInt(1, pizza.getPizzaId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                pizza.setSize(rs.getString(2));
                pizza.setIsFinished(rs.getBoolean(3));
                pizza.setCrustType(rs.getString(4));
                pizza.setPrice(rs.getBigDecimal("price"));
            }
            return pizza;
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL

.class  


.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int[] createFake() {//Creates temporary customer and order -> they'll be updated at the end
        String sqlCus = "INSERT INTO customer "
                + "(firstName, lastName, phoneNumber, "
                + "email, houseNumber, street, province, postalCode) "
                + "VALUES ('NOT VALID YET', 'Guest', '(000) 111 2222', 'guest@guest.ca', "
                + "'1', 'Street st', 'NB', 'E3B 3B4')";
        int[] result = new int[2];
        try {
            int cusId = -1;
            int orderId = -1;

            PreparedStatement statement = conn.prepareStatement(sqlCus, Statement.RETURN_GENERATED_KEYS);
            if (statement.executeUpdate() > 0) {
                ResultSet genKeys = statement.getGeneratedKeys();
                if (genKeys.next()) {
                    cusId = genKeys.getInt(1);
                }
                String sqlOrder = "INSERT INTO orders "
                        + "(orderId, totalPrice, deliveryDateTime, "
                        + "placedDateTime, customerId, orderStatus) "
                        + "VALUES (NULL, '0', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, "
                        + cusId + ", 'NOT AN OFFICIAL ORDER YET')";
                PreparedStatement orderStatement = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                if (orderStatement.executeUpdate() > 0) {
                    ResultSet ordGenKeys = orderStatement.getGeneratedKeys();
                    if (ordGenKeys.next()) {
                        orderId = ordGenKeys.getInt(1);
                        result[0] = cusId;
                        result[1] = orderId;
                        return result;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean reviewPizza(Pizza p) {
        boolean successFlag = false;
        int row = 0;
        try {
            String sql = "UPDATE pizza SET sizeId=?, isFinished=?, crustTypeId=?, price =? WHERE pizzaId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, fetchSizeByName(p.getSize()));
            statement.setBoolean(2, p.isIsFinished());
            statement.setInt(3, fetchCrustByName(p.getCrustType()));
            statement.setBigDecimal(4, p.getPrice());
            statement.setInt(5, p.getPizzaId());

            row = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ToppingsDAL

.class  


.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        }
        if (row == 1) {
            successFlag = true;
        }
        return successFlag;
    }

    public int fetchCrustByName(String crustTypeName) {
        int id = -1;
        String sql = "SELECT crustTypeId FROM crusttypes WHERE name = ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, crustTypeName);
            ResultSet rs = s.executeQuery();
            rs.first();
            id = rs.getInt(1);
            return id;
        } catch (SQLException ex) {
            return id;
        }
    }

    public int fetchSizeByName(String sizeName) {
        int id = -1;
        String sql = "SELECT sizeId FROM sizes WHERE name = ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, sizeName);
            ResultSet rs = s.executeQuery();
            rs.first();
            id = rs.getInt(1);
            return id;
        } catch (SQLException ex) {
            return id;
        }
    }

    public ArrayList<Pizza> fetchAllPizzasByOrderId(int orderId) {
        String sql = "SELECT * FROM pizza WHERE orderId = ?";
        int id = 0;
        try {
            PreparedStatement s = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ArrayList<Pizza> pizzaList = new ArrayList<>();
            s.setInt(1, orderId);
            ResultSet rs = s.executeQuery();
            rs.first();
            do {
                Pizza p = new Pizza();
                String sizeName;
                p.setPizzaId(rs.getInt("pizzaId"));
                switch (rs.getInt("sizeId")) {
                    case 3:
                        sizeName = "Medium";
                        break;
                    case 2:
                        sizeName = "Large";
                        break;
                    default:
                        sizeName = "Medium";
                        break;
                }
                p.setSize(sizeName);
                p.setIsFinished(rs.getBoolean("isFinished"));
                String crustName;
                switch (rs.getInt("crustTypeId")) {
                    case 2:
                        crustName = "Handmade Pan";
                        break;
                    case 3:
                        crustName = "Original";
                        break;
                    case 4:
                        crustName = "Gluten";
                        break;
                    case 5:
                        crustName = "Chicago Style";
                        break;
                    default:
                        crustName = "Thin Crust";
                        break;
                }
                p.setCrustType(crustName);
                p.setPrice(rs.getBigDecimal("price"));
                pizzaList.add(p);
            } while (rs.next());
            return pizzaList;

        } catch (SQLException ex) {
            return null;
        }
    }
}
