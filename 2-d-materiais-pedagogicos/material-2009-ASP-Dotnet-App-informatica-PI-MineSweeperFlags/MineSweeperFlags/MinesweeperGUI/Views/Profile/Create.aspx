<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage<Minesweeper.Player>" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">	
<html>
<head>
    <title>.:MineSweeper 2000&Half:.</title>
    <script type="text/javascript" src="/Source/GameMVC.js"></script>
    <script type="text/javascript" src="/Source/jquery-1.3.2.js"></script>	
    <link rel="Stylesheet" type="text/css" href="/Source/Profile.css" />	
</head>
<body>
  <div class="divBackGround">
    <div class="divTittle">
        Registering in MineSweeper 2000&Half
        <br /><br />
    </div>
    <div class="divProfile">
        <% using (Html.BeginForm("Create", "Profile", FormMethod.Post, new { enctype = "multipart/form-data"}))
        { %>
            <div class="divProfileLine">
                <div class="divProfileText">Email:</div>
                <div class="divProfileControls">
                    <%= Html.TextBox("Email")%>
                </div>
            </div>
            <div class="divProfileLine">
                <div class="divProfileText">Password:</div>
                <div class="divProfileControls">
                    <%= Html.Password("Pwd")%>
                </div>
            </div>
            <div class="divProfileLine">
                <div class="divProfileText">Name:</div>
                <div class="divProfileControls">
                    <%= Html.TextBox("Name")%>
                </div>
            </div>
            <div class="divProfileLine">
                <div class="divProfileText">Online:</div>
                <div class="divProfileControls">
                    <%= Html.CheckBox("Online")%>
                </div>
            </div>
            <div class="divProfileLine">
                <div class="divProfileText">Photo:</div>
                <div class="divProfileControls">
                    <input type="file" name="photo" />
                </div>
            </div>
            <div class="divProfileLine" style="text-align: center">
                <input type="submit" value="Save Profile" /><br />
                <%=ViewData["message"] %>
            </div>
        <% } %>        
    </div>
  </div> 
</body>
</html>