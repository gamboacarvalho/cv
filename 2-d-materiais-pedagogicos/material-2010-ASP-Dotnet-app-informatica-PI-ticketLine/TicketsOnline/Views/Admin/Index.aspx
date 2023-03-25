<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<TicketsOnline.Models.AdminModel>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<%@ Import Namespace="RepositoryInterfaces.DataObjects" %>
<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Administração</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <h3>
        Administradores</h3>
    <%
        if (Model.Admins.Count == 0)
        {	
    %>
    Não existem administradores registados!
    <%
        }
        foreach (IUser admin in Model.Admins)
        {
    %>
    <div id="admin<%=admin.UserName %>" class="item">
        <b>Nome: </b>
        <%=Html.Encode(admin.Name) %>;<br />
        <b>Email: </b>
        <%=Html.Encode(admin.Email)%>;<br />
    </div>
    <br />
    <%
        } %>
    <h3>
        Clientes</h3>
    <%
        if (Model.Clients.Count == 0)
        {	
    %>
    Não existem clientes registados!
    <%
        }
        foreach (IUser client in Model.Clients)
        {
            string clientId = "client" + client.UserName;
    %>
    <div id="<%=clientId %>" class="item">
        <%using (Ajax.BeginForm(
              "Delete",
              "Admin",
              new RouteValueDictionary { { "id", client.UserName } },
              new AjaxOptions
                  {
                      HttpMethod = "Post",
                      UpdateTargetId = clientId
                  }))
          {
        %>
        <b>Nome: </b>
        <%=Html.Encode(client.Name) %>;<br />
        <b>Email: </b>
        <%=Html.Encode(client.Email) %>;<br />
        <b>Username: </b>
        <%=Html.Encode(client.UserName) %>;
        <input type="submit" value="Apagar" />
        <%
            } %>
    </div>
    <br />
    <%
        } %>
</asp:Content>
