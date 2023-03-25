<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>MineSweeper 2000&Half</title>
    <link type="text/css" rel="Stylesheet" href="../../Source/Profile.css" />
</head>
<body> 
    <div class="divBackGround">
        <h1>Welcome to MineSweeper 2000&Half</h1>          
        <div class="divEmailLogin">
            <% using(Html.BeginForm()) { %>
                    Please enter your e-mail<br />
                    <%= Html.TextBox("Email")%>&nbsp;<%= Html.Password("Pwd")%>
                    <input type="submit" value="OK" /><br />            
                    <%= ViewData["message"] %>
            <% } %>   
            <br /> 
            <a href="../../Profile/Create">Register in MineSweeper 2000&Half</a>
        </div>
    </div>
</body>
</html>
