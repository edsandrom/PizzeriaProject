/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BL;

import Support_Servlets.JSFSupportClass;
import DAL.EmployeeBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@ManagedBean
@SessionScoped
public class Employee {

    private String username;
    private String password;
    private int empId;

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Employee() {

    }

    public String loginUser() {

        boolean valid = EmployeeBean.checkLogin(username, password);
        if (valid) {
            HttpSession session = JSFSupportClass.getSession();
            session.setAttribute("username", username);
            return "admin?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Input. Please enter correct Username and Password",
                            "Please enter correct Username and Password"));
            return "login?faces-redirect=true";
        }
    }

    public String logout() {
        HttpSession session = JSFSupportClass.getSession();
        session.invalidate();
        return "index.jsp";
    }

    public boolean addEmployee(String username, String password) {

        if (EmployeeBean.FetchEmployeeByUserName(username)!=null) {
            System.out.println("Username already exists.");
            return false;
        } else {
            if (EmployeeBean.InsertEmployee(username, password)) {
                return true;
            } else {
                System.out.println("Error inserting employee");
                return false;
            }
        }
    }

    public static Employee getEmployeeByUsername(String username) {
        Employee emp = new Employee();
        if (EmployeeBean.FetchEmployeeByUserName(username) != null) {
            emp = EmployeeBean.FetchEmployeeByUserName(username);
            return emp;
        }
        else{
            return null;
        }

    }

}
