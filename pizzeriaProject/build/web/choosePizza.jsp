<%-- 
    Document   : choosePizza
    Created on : Dec 8, 2019, 5:25:37 PM
    Author     : Edsandro de Oliveira <edsandrom@gmail.com>
--%>

<%@page import="BL.Toppings"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
        <title>Pizza Assembler</title>
        <style>
            #mainDiv {margin:auto; width: 80%;height: 100%; margin-left: 15%; margin-right: 15%; margin-top: 3%; }
            #leftDiv{border:3px; width:30%; border-style:solid; border-color: blue; padding: 2%; margin:auto;float:left;}
            #centerDiv{border:3px; width:10%; border-style:solid; border-color: red; padding: 1%; margin:auto}
            #centerDiv2{border:3px; width:15%; border-style:solid; border-color: green; padding: 1%; margin:auto; margin-top: 2%;}
            #rightDiv{border:3px; width:20%; border-style:solid; border-color: blue; padding: 2%; margin:auto; float: right;}
            table, th, td {margin:auto; border: 1px solid black;border-collapse: collapse;}
            th, td {padding: 5px;}
            #buttonDiv{display: flex; justify-content: center; padding-top: 3%;}
            th {text-align: left; width:20%; text-align:center;}
            td {text-align: center;}
            label {display: inline-block; width: 140px; text-align: left;}
        </style>
    </head>
    <body>
        <h1 style="text-align: center;">Choose your pizza(s)</h1>
        <br /> 
        <div id="mainDiv">
            <form id='addPizzaForm' method='post' action='AddPizzaProc'>
                <div id="leftDiv">
                    <h2 style="text-align: center;">Choose the Topping(s)</h2>
                    <table>
                        <tr>
                            <th>Topping</th> 
                            <th>Price ($)</th>
                            <th>Add Topping</th>
                        </tr>
                        <%  int orderId = 0;
                            session = request.getSession(false);
                            if (request.getAttribute("orderId") == null) {
                                orderId = -1;
                            } else {
                                try {
                                    orderId = (Integer) (request.getAttribute("orderId"));
                                } catch (Exception ex) {
                                    System.out.println("ERROR ON OrderId Session Passing");
                                }
                            }

                            Toppings top = new Toppings();
                            ArrayList<Toppings> toppingsList = top.getAllAvailableToppings();
                            int i = 0;
                            if (toppingsList != null) {
                                int[] ids = new int[toppingsList.size()];
                                while (i < toppingsList.size()) {
                                    if (toppingsList.get(i).getName().equalsIgnoreCase("cheese")) {
                                        out.println("<tr>");
                                        out.println("<td>" + toppingsList.get(i).getName() + "</td>");
                                        out.println("<td>" + toppingsList.get(i).getPrice() + "</td>");
                                        out.println("<td> <input type ='checkbox' checked name ='activeChkbx" + toppingsList.get(i).getId() + "' id ='activeChkbx" + toppingsList.get(i).getId() + "' disabled='true'/> </td >");
                                        out.println("</tr>");
                                        i++;
                                    } else {
                                        out.println("<tr>");
                                        out.println("<td>" + toppingsList.get(i).getName() + "</td>");
                                        out.println("<td>" + toppingsList.get(i).getPrice() + "</td>");
                                        if (toppingsList.get(i).isIsActive()) {
                                            out.println("<td> <input type ='checkbox' name ='activeChkbx" + toppingsList.get(i).getId() + "' id ='activeChkbx" + toppingsList.get(i).getId() + "' /> </td >");
                                        }
                                        out.println("</tr>");
                                        ids[i] = toppingsList.get(i).getId();
                                        out.println("<input type='hidden' name='chkId" + i + "' id='chkId" + i + "' value='" + ids[i] + "' />");

                                        i++;
                                    }
                                }
                            }
                        %>
                    </table>
                </div>
                <div id="rightDiv">
                    <h2 style="text-align: center;"> Cart</h2>
                    <table>
                        <th>Total Price</th>
                        <th>Pizza #</th>
                        <th>Remove</th>
                    </table>

                </div>
                <input type='hidden' name='totalCount' id='totalCount' value="<%=i%>" />
                <div id="centerDiv">
                    <h2 style="text-align: center;"> Choose the Size </h2>
                    <input style="display: inline-block;" type="radio" name="sizeRadio" id="sizeRadio" checked value="small"/> Small &nbsp;&emsp;&nbsp;&nbsp; $12.00<br />
                    <input style="display: inline-block;" type="radio" name="sizeRadio" id="sizeRadio" value="medium"/> Medium &nbsp; &nbsp; $15.00 <br />
                    <input style="display: inline-block;" type="radio" name="sizeRadio" id="sizeRadio" value="large"/> Large &nbsp;&emsp;&nbsp;&nbsp; $16.50 <br />
                </div>
                <div id="centerDiv2">
                    <h2 style="text-align: center;"> Choose the Crust Type</h2>
                    <input style="display: inline-block;" type="radio" name="crustRadio" id="crustRadio" checked value="Thin Crust"/> Thin Crust &emsp; &emsp; &nbsp; &emsp; $0.00 <br />
                    <input style="display: inline-block;" type="radio" name="crustRadio" id="crustRadio" value="Handmade Pan"/> Handmade Pan  &emsp; &nbsp; &ensp; $5.00 <br />
                    <input style="display: inline-block;" type="radio" name="crustRadio" id="crustRadio" value="Original"/> Original &nbsp; &ensp; &nbsp; &nbsp; &nbsp; &emsp; &emsp; $3.25 <br />
                    <input style="display: inline-block;" type="radio" name="crustRadio" id="crustRadio" value="Gluten"/> Gluten &ensp; &nbsp; &ensp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &ensp; &ensp; $1.50 <br />
                    <input style="display: inline-block;" type="radio" name="crustRadio" id="crustRadio" value="Chicago Style"/> Chicago Style &emsp; &nbsp; &nbsp; &ensp; $6.00<br />
                </div>
                <br />
                <div id='buttonDiv'>
                    <input type='submit' name="addPizzaButton" id="addPizzaButton" value="Add Pizza" />
                    <input type='hidden' name="checkOrderId" id="checkOrderId" value="<%=orderId%>" />
                </div>
            </form>
            <form method='post' action='index.jsp'>
                <div id='buttonDiv'>
                    <input type='submit' name='submitOrder' id='submitOrder' value='Submit Order'>
                </div>
            </form>
            <form method='post' action='index.jsp'>
                <div id='buttonDiv'>
                    <input type='submit' name='backIndex' id='backIndex' value='Back'>
                </div>
            </form>
    </body>
</html>


