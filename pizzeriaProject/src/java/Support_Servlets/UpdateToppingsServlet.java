/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Servlets;

import BL.Toppings;
import DAL.ToppingsDAL;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@WebServlet(name = "UpdateToppings", urlPatterns = {"/UpdateToppings"})
public class UpdateToppingsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Toppings toppings = new Toppings();
        ArrayList<Toppings> toppingsList = new ArrayList<>();
        toppingsList = toppings.getAllToppings();
        displayPage(request, response, toppingsList);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param toppingsList
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void displayPage(HttpServletRequest request, HttpServletResponse response, ArrayList<Toppings> toppingsList)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Review Toppings</title>");
            out.println("<style>");
            out.println("table, th, td {margin:auto; border: 1px solid black;border-collapse: collapse;}");
            out.println("th, td {padding: 5px;}");
            out.println("#buttonDiv{display: flex; justify-content: center;}");
            out.println("th {text-align: left; width:20%; text-align:center;}");
            out.println("td {text-align: center;}");
            out.println("label {display: inline-block; width: 140px; text-align: left;}</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 style=\"text-align: center;\">Update Toppings Info</h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Topping Name</th> ");
            out.println("<th>Price ($)</th>");
            out.println("<th>Availability</th>");
            out.println("<th>Update</th>");
            out.println("<th>Remove</th>");
            out.println("</tr>");
            for (Toppings t : toppingsList) {
                out.println("<tr>");
                out.println("<form id='updateToppingsForm" + t.getId() + "' method='post' action='UpdateToppingsProcServlet'>");
                out.println("<td>" + t.getName() + "</td>");
                out.println("<input type='hidden' name='updateToppingsName" + t.getId() + "' id='updateToppingsName" + t.getId() + "' value='" + t.getName() + "') >");
                out.println("<td>");
                out.println("<input type='text' name='updateToppingPriceText" + t.getId() + "' id='updateToppingPriceText" + t.getId() + "' value='" + t.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN) + "' /> ");
                out.println("</td>");
                if (t.isIsActive()) {
                    out.println("<td><input type='checkbox' name='Chkbx" + t.getId() + "' id='Chkbx" + t.getId() + "' checked  /></td>");
                } else {
                    out.println("<td><input type='checkbox' name='Chkbx" + t.getId() + "' id='Chkbx" + t.getId() + "' /></td>");
                }
                out.println("<input type='hidden' name='toppingsId' id='toppingsId' value='" + t.getId() + "' />");
                out.println("<td>");
                out.println("<input type='submit' name='updateToppingButton' id='updateToppingButton' value='Update'> ");
                out.println("</td>");
                out.println("<td>");
                out.println("<input type='submit' name='deleteToppingButton' id='deleteToppingButton' value='Remove'> ");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("<h6 style=\"text-align: center;\">Don't forget to click on buttons in order to perform an update.</h6>");
            out.println("<BR><BR>");
            out.println("<div id='buttonDiv'>");
            out.println("<form method='post' action='" + request.getContextPath() + "/admin.xhtml'>");
            out.println("<input type='submit' name='backMainPageButton' id='backMainPageButton' value='Back to Admin'> ");
            out.println("</form>");
            out.println("</div>");
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
