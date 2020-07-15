/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Servlets;

import BL.Order;
import BL.OrderBL;
import BL.Pizza;
import BL.PizzaBL;
import BL.Toppings;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@WebServlet(name = "AddPizzaProc", urlPatterns = {"/AddPizzaProc"})
public class AddPizzaProc extends HttpServlet {

    Order order = new Order();
    OrderBL orderBL = new OrderBL();
    PizzaBL pizzaBL = new PizzaBL();

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
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/choosePizza.jsp");
        dispatcher.forward(request, response);
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "choosePizza.jsp");
        response.setContentType("text/html");

//            out.println("<h1>You tried to add a row and you " + toppingsList.size() + "</h1>");
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Result</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>You tried to add a row and you " + toppingsList.get(0).getName() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
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
        try (PrintWriter out = response.getWriter()) {
            int orderId;
            int[] tempEntries = pizzaBL.createTemporaryEntries();
            int customerId = tempEntries[0];
            orderId = tempEntries[1];

            order.setId(orderId);
            order = orderBL.getOrder(order.getId());

            Pizza pizza = new Pizza();
            pizza.setSize(request.getParameter("sizeRadio"));
            pizza.setCrustType(request.getParameter("crustRadio"));
            pizza.setIsFinished(false);
            pizza.setPrice(pizzaBL.CalculatePrice(pizza));

            ArrayList<Toppings> toppingsList = new ArrayList<>();
            int totalCount = Integer.parseInt(request.getParameter("totalCount"));
            for (int i = 1; i < totalCount; i++) {
                System.out.println("chkId" + i + ":");
                int chkBoxId = (Integer.parseInt(request.getParameter("chkId" + i)));
//
                if (request.getParameter("activeChkbx" + chkBoxId) != null) {
                    Toppings top = new Toppings();
                    top.setId(chkBoxId);
                    top = top.getToppingById(top.getId());
                    toppingsList.add(top);
                }
            }
            Toppings topp = new Toppings();
            topp.setId(1);
            topp.setName("chees");
            topp.setPrice(new BigDecimal("0.00"));
            topp.setIsActive(false);
            toppingsList.add(topp);
               
            if (pizzaBL.addPizza(pizza, order.getId(), toppingsList)) {
                System.out.println(pizza.getSize());
                pizza.setPrice(pizzaBL.CalculatePrice(pizza));
            }
            
            System.out.println(pizza.getPrice());
            System.out.println(pizzaBL.CalculatePrice(pizza));
            if (pizzaBL.updatePizza(pizza)) {
                System.out.println(pizza.getPrice());
                order.setTotalPrice(orderBL.getTotal(order));
                orderBL.updateOrder(order);
            }
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
