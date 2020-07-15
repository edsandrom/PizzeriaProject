/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import java.math.BigDecimal;
import javax.ejb.Stateless;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@Stateless
public class Pizza {

    private String crustType, size;
    private boolean isFinished;
    private BigDecimal price;
    private int pizzaId;

    public Pizza() {
    }

    public Pizza(String crustType, String size, boolean isFinished, BigDecimal price, int pizzaId) {
        this.crustType = crustType;
        this.size = size;
        this.isFinished = isFinished;
        this.price = price;
        this.pizzaId = pizzaId;
    }

    
    public String getCrustType() {
        return crustType;
    }

    public void setCrustType(String crustType) {
        this.crustType = crustType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

}
