/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import DAL.CustomerDAL;
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
public class CustomerBL {

    CustomerDAL customerDAL = CustomerDAL.getInstance();

    public boolean updateCustomer(Customer c) {
        return customerDAL.reviewCustomer(c);
    }

    public Customer getCustomer(int id) {
        return customerDAL.fetchCustomerById(id);
    }

    public int getCustomerId(int orderId) {
        return customerDAL.fetchCustomerId(orderId);
    }
}
