<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<IEnumerable<TicketsOnline.Models.CartIndexModel>>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<h3>
    Cesto de compras</h3>
<ul id="basketList">
    <%
        foreach (CartIndexModel cartIndexModel in Model)
        {
    %>
    <li id="session<%=cartIndexModel.SessionId %>">
        <%=cartIndexModel.ShowName %>,
        <%=cartIndexModel.SessionName %>,
        <%=cartIndexModel.PreReserveSeats.Length %>
        lugares; </li>
    <%
        }
    %>
</ul>
<%=Html.ActionLink("detalhes...", "Index", "Cart") %>
