/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Servlets;

import BL.Pizza;
import BL.PizzaBL;
import BL.Toppings;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@WebServlet(name = "OrderDetailsUpdateProcServlet", urlPatterns = {"/OrderDetailsUpdateProc"})
public class OrderDetailsUpdateProcServlet extends HttpServlet {

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
        response.setHeader("Location", "admin.xhtml");
        try (PrintWriter out = response.getWriter()) {

            int orderId = Integer.parseInt(request.getParameter("orderId"));
            out.print("<input type=hidden' name='orderIDforCus' id='orderIDforCus' value='" + orderId + "'");

        }
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
            PizzaBL pizzaBL = new PizzaBL();
            Pizza pizza = new Pizza();
            pizza.setPizzaId(Integer.parseInt(request.getParameter("pizzaId")));
            pizza = pizzaBL.getPizzaById(pizza.getPizzaId());
            if (request.getParameterValues("Chkbx" + pizza.getPizzaId()) == null) {
                pizza.setIsFinished(false);
            } else {
                pizza.setIsFinished(true);
            }
            pizzaBL.updatePizza(pizza);
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
