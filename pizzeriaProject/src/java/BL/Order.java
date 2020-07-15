/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.ejb.Stateless;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Stateless
public class Order {

    private BigDecimal totalPrice;
    private String orderStatus;
    private LocalDateTime placedDateTime, deliveryDateTime;

    public Order(BigDecimal totalPrice, String orderStatus, LocalDateTime placeDateTime, LocalDateTime deliveryDateTime) {
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.placedDateTime = placeDateTime;
        this.deliveryDateTime = deliveryDateTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPlacedDateTime() {
        return placedDateTime;
    }

    public void setPlacedDateTime(LocalDateTime placedDateTime) {
        this.placedDateTime = placedDateTime;
    }

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public LocalDateTime getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(LocalDateTime deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public Order() {
    }

}
