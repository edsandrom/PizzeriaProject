/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import DAL.OrderDAL;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Stateless
@ManagedBean
@RequestScoped
public class OrderBL {

    OrderDAL orderDAL = OrderDAL.getInstance();

    public boolean updateOrder(Order o) {
        if (orderDAL.reviewOrder(o)) {
            return true;
        } else {
            return false;
        }
    }

    public Order getOrder(int id) {
        return orderDAL.fetchOrderById(id);
    }

    public BigDecimal getTotal(Order o) {
        return orderDAL.totalOrder(o);
    }

    public ArrayList<Order> getAllOrders() {
        return orderDAL.fetchAllValidOrders();
    }

    public boolean finishOrder() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params
                = fc.getExternalContext().getRequestParameterMap();
        int orderId = Integer.parseInt(params.get("orderId"));
        if (orderDAL.endOrder(orderId)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean checkOrderStatus (String orderStatus){
       return orderDAL.orderStatusCompare(orderStatus);
    }
   
}
