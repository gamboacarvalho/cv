<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage<Minesweeper.Player>" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">	
<html>
<head id="Head1" runat="server">
    <title>Board Tests</title>
    <script type="text/javascript" src="~/Source/GameMVC.js"></script>
    <script type="text/javascript" src="~/Source/jquery-1.3.2.js"></script>	
    <link rel="Stylesheet" type="text/css" href="~/Source/mineSweeper.css" />	
</head>
<body>
  <h1>Consulta de Perfil</h1>
  
  <div>
    E-Mail: value="<%=Model.EMail%>"
  </div>
  <div>
    Name: <%=Model.Name%>
  </div>
  <div>
    Status: <%= (Model.Online ? "Online": "OffLine" )%>
  </div>
  <div>
    Photo: <img src="/Profile/GetPlayerPhoto?eMail=<%=Model.EMail%>" />    
  </div> 
</body>
</html>
