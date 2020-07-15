/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Servlets;

import BL.Toppings;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javafx.scene.control.CheckBox;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@WebServlet(name = "UpdateToppingsProcServlet", urlPatterns = {"/UpdateToppingsProcServlet"})
public class UpdateToppingsProcServlet extends HttpServlet {

    String url = "UpdateToppings";
//    String msg;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", url);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        try {
            Toppings topping = new Toppings();
            topping.setId(Integer.parseInt(request.getParameter("toppingsId")));
            topping.setName(String.valueOf(request.getParameter("updateToppingsName" + topping.getId())));
            topping.setPrice(new BigDecimal(request.getParameter("updateToppingPriceText" + topping.getId())));
            topping.setPrice(topping.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            if (request.getParameterValues("Chkbx" + topping.getId()) == null) {
                topping.setIsActive(false);
            } else {
                topping.setIsActive(true);
            }
            if (request.getParameter("updateToppingButton") != null){
                topping.updateTopping(topping);
            }
            else if (request.getParameter("deleteToppingButton") != null){
                topping.deleteTopping(topping);
            }
//                msg = "Update was successful.";

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
//            msg = "Please enter valid price.";
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Toppings Update/Delete Page";
    }// </editor-fold>

}
