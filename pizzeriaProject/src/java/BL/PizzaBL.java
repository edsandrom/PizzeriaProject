/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import DAL.PizzaDAL;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.ejb.Stateless;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Stateless
public class PizzaBL {

    PizzaDAL pizzaDAL = PizzaDAL.getInstance();

    public BigDecimal CalculatePrice(Pizza p) {
        return pizzaDAL.calcPizzaPrice(p);
    }

    public ArrayList<Toppings> GetToppings(int pizzaId) {
        return pizzaDAL.listToppings(pizzaId);

    }

    public int[] createTemporaryEntries() {
        int[] tempEntries = pizzaDAL.createFake();
        if (tempEntries[0] != -1 && tempEntries[1] != -1) {
            return tempEntries;
        } else {
            return null;
        }
    }

    public boolean addPizza(Pizza p, int orderId, ArrayList<Toppings> toppingsList) {
        return pizzaDAL.insertPizza(p, orderId, toppingsList);
    }

    public boolean updatePizza(Pizza p) {
        return pizzaDAL.reviewPizza(p);
    }

    public int getCrustByName(String crustTypeName) {
        return pizzaDAL.fetchCrustByName(crustTypeName);
    }

    public int getSizeByName(String sizeName) {
        return pizzaDAL.fetchSizeByName(sizeName);
    }

    public ArrayList<Pizza> getAllPizzasByOrderId(int orderId) {
        return pizzaDAL.fetchAllPizzasByOrderId(orderId);
    }

    public Pizza getPizzaById(int pizzaId) {
        return pizzaDAL.fetchPizzaById(pizzaId);
    }
}
