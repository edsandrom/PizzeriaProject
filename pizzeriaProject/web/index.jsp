<%-- 
    Document   : index
    Created on : Nov 25, 2019, 2:17:30 PM
    Author     : Edsandro de Oliveira <edsandrom@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Ed's Pizza</title>
        <style>
            #mainDiv{border:3px; width:350px; border-style:solid; border-color: blue; padding: 1em; margin:auto}
            label {padding:2%; display: inline-block; width: 140px; text-align: left;}​
        </style>
    </head>
    <body>
        <BR><BR>
        <div id="mainDiv">
            <h1 style="margin: 0px; text-align: center;">Employees Only</h1>
            <form method="post" action="login.xhtml">
                <BR>
                <div style="text-align: center;">
                    <input type="submit" name="loginButton" name="loginButton" value="Login">
                </div>
            </form>
            <BR>
            <h1 style="margin: 0px; text-align: center;">proceed to order pizza</h1>
            <BR>
            <form method="post" action="choosePizza.jsp">
                <BR>
                <div style="text-align: center;">
                    <input type="submit" name="orderPizzaButton" name="orderPizzaButton" value="Order Pizza">
                </div>
            </form>
        </div>
    </body>   
</html>
