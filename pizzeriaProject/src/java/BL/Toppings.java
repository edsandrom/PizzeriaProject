/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import Support_Servlets.JSFSupportClass;
import DAL.ToppingsDAL;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@ManagedBean(name = "toppings")
@SessionScoped
public class Toppings {

    @EJB
    private ToppingsDAL toppingsDAL;

    String name;
    Date dateCreated;
    BigDecimal price;
    boolean isActive;

    public void setToppingsList(ArrayList<Toppings> toppingsList) {
        this.toppingsList = toppingsDAL.fetchAllToppings();
    }

    public Toppings(int id, String name, BigDecimal price, boolean isActive) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isActive = isActive;
    }

    public Toppings(String name, Date dateCreated, BigDecimal price, boolean isActive, ArrayList<Toppings> toppingsList) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.price = price;
        this.isActive = isActive;
    }

    public Toppings() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override //In order to use contains method of ArrayList Class
    public boolean equals(Object obj) {

        if (!(obj instanceof Toppings)) {
            return false; //objects cant be equal
        }

        Toppings secondTopping = (Toppings) obj;
        return this.name.equalsIgnoreCase(secondTopping.name);
    }

    public String addTopping() {
        String username = JSFSupportClass.getUserName(); //Getting username in order to find the Id
        Employee emp = Employee.getEmployeeByUsername(username);
        Toppings top = new Toppings();

        if (!name.isEmpty() && price != null) {

            try {
                top.setName(name.trim());
                top.setPrice(price);
                top.setIsActive(isActive);
                ArrayList<Toppings> allToppings = toppingsDAL.fetchAllToppings();
                if (allToppings.contains(top)) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "This topping already exists. Please enter another topping or update it.",
                                    "Please enter another topping or update it"));
                } else if (toppingsDAL.insertTopping(top, emp.getEmpId())) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Topping added successfuly.",
                                    "Success"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Please enter valid values.",
                                    "It was not possible to insert the data into table."));
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                "Please enter valid values.",
                                "It was not possible to insert the data into table."));
                System.out.println(ex.getMessage());
            }
        }
        name = "";
        price = null;
        isActive = false;
        return "admin";

    }

    ArrayList<Toppings> toppingsList;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Toppings> getToppingsList() {
        return toppingsList;
    }

    public boolean deleteTopping(Toppings top) {
        boolean successFlag = false;
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        ArrayList<Toppings> allToppings = toppingsDAL.fetchAllToppings();
        if (allToppings.contains(top)) {
            if (toppingsDAL.removeTopping(top)) {
                successFlag = true;
            } else {
                successFlag = false;
            }
        } else {
            successFlag = false;
        }
        return successFlag;
    }

    public boolean updateTopping(Toppings top) {
        boolean successFlag = false;
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        ArrayList<Toppings> allToppings = toppingsDAL.fetchAllToppings();
        try {
            if (allToppings.contains(top)) {
                Toppings oldTop = new Toppings();
                oldTop.getToppingById(top.getId());
                if ((oldTop.isIsActive() == top.isIsActive()) && (oldTop.getPrice() == top.getPrice())) {
                    successFlag = false;
                } else {
                    successFlag = toppingsDAL.reviseTopping(top);
                }
            }
        } catch (NullPointerException nex) {
            nex.printStackTrace();
            System.out.println(nex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
        return successFlag;
    }

    public ArrayList<Toppings> getAllToppings() {
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        ArrayList<Toppings> tList = toppingsDAL.fetchAllToppings();
        return tList;
    }

    public Toppings getToppingById(int toppingId) {
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        Toppings topping = toppingsDAL.fetchToppingById(toppingId);
        return topping;
    }

    public ArrayList<Toppings> getAllAvailableToppings() {
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        ArrayList<Toppings> tList = toppingsDAL.fetchAllAvailableToppings();
        return tList;
    }

    public ArrayList<Toppings> getPizzaToppings(int pizzaId) {
        ToppingsDAL toppingsDAL = new ToppingsDAL();
        return toppingsDAL.fetchPizzaToppings(pizzaId);
    }
}
