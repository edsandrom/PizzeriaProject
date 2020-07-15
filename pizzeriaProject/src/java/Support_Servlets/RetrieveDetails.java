/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support_Servlets;

import BL.Customer;
import BL.CustomerBL;
import BL.Pizza;
import BL.PizzaBL;
import BL.Toppings;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 *
 * @author Edsandro de Oliveira <edsandrom@gmail.com>
 */
@WebServlet(name = "RetrieveCustomer", urlPatterns = {"/RetrieveCustomer"})
public class RetrieveDetails extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            //Customer info
            CustomerBL cusBL = new CustomerBL();
            Customer cus = new Customer();
            int orderId = Integer.parseInt(request.getParameter("orderIDforCus"));
            int cusId = cusBL.getCustomerId(orderId);
            System.out.println(cusId);
            cus = cusBL.getCustomer(cusId);

            //Pizza Info
            PizzaBL pizzaBL = new PizzaBL();
            Pizza p = new Pizza();
            ArrayList<Pizza> pizzaList = new ArrayList<>();
            pizzaList = pizzaBL.getAllPizzasByOrderId(orderId);

            //Toppings Info
            Toppings toppings = new Toppings();
            ArrayList<Toppings> toppingsList = new ArrayList<>();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<style>");
            out.println(".table0 table, td, th {border-style: none;}");
            out.println("table, th, td {margin:auto; border: 1px solid black;border-collapse: collapse;}");
            out.println("th, td {padding: 5px;}");
            out.println("#buttonDiv{display: flex; justify-content: center;}");
            out.println("th {text-align: left; width:12%; text-align:center;}");
            out.println("td {text-align: center; }");
            out.println("label {display: inline-block; width: 140px; text-align: left;}</style>");
            out.println("<title>Order " + orderId + " Details</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form method='post' action='OrderDetailsUpdateProc'>");
            out.println("<h1 style='text-align:center';> Pizza Order # " + orderId + "</h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Order #</th>");
            out.println("<th>Customer Name</th>");
            out.println("<th>Phone Number</th>");
            out.println("<th>Address</th>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>" + orderId + "</td>");
            out.println("<td>" + cus.getFirstname() + " " + cus.getLastname() + "</td>");
            out.println("<td>" + cus.getPhoneNumber() + "</td>");
            out.println("<td>" + cus.getHouseNumber() + " " + cus.getStreet() + ", " + cus.getProvince() + ". " + cus.getPostalCode() + "</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<br />");
            out.println("<br />");
            if (pizzaList != null) {
                for (int i = 0; i < pizzaList.size(); i++) {
                    out.println("<h2 style='text-align:center;'> Pizza " + (i + 1) + "</h2>");
                    out.println("<table style='border-style: none;'>");
                    out.println("<tr>");
                    out.println("<th>Size</th>");
                    out.println("<th>Crust Type</th>");
                    out.println("<th>Toppings</th>");
                    out.println("<th>Ready</th>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td>");
                    out.println(pizzaList.get(i).getSize());
                    out.println("</td>");
                    out.println("<td>");
                    out.println(pizzaList.get(i).getCrustType());
                    out.println("</td>");
                    out.println("<td>");
                    toppingsList = toppings.getPizzaToppings(pizzaList.get(i).getPizzaId());
                    for (Toppings top : toppingsList) {
                        out.println(top.getName() + " / ");
                    }
                    out.println("</td>");
                    out.println("<td>");
                    if (pizzaList.get(i).isIsFinished()) {
                        out.println("<input type='checkbox' name='Chkbx" + pizzaList.get(i).getPizzaId() + "' id='Chkbx" + pizzaList.get(i).getPizzaId() + "' checked  ></input>");
                    } else {
                        out.println("<input type='checkbox' name='Chkbx" + pizzaList.get(i).getPizzaId() + "' id='Chkbx" + pizzaList.get(i).getPizzaId() + "' ></input>");
                    }
                    out.println("</td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("<input type='hidden' name='orderId' id='orderId' value='" + orderId + "' />");
                    out.println("<input type='hidden' name='pizzaId' id='pizzaId' value='" + pizzaList.get(i).getPizzaId() + "' />\n");
                    out.println("<br />");
                }
            }
            out.println("<h6 style='text-align:center;'>Don't forget to click on UPDATE in order to perform an update.</h6>");
            out.println("<br /> <br />");
            out.println("<div id='buttonDiv'>");
            out.println("<input type='submit' name='updateOrderButton' id='updateOrderButton' value='Update'> ");
            out.println("</div>");
            out.println("</form>");
            out.println("<br /> <br />");
            out.println("<form method='post' action='admin.xhtml'>\n"
                    + "                <div id='buttonDiv'>\n"
                    + "                    <input type='submit' name='backAdmin' id='backAdmin' value='Back'>\n"
                    + "                </div>\n"
                    + "            </form>");
            out.println("</body>");
            out.println("</html>");
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
