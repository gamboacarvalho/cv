<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<System.Collections.Generic.IEnumerable<TicketsOnline.Models.CartIndexModel>>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<%@ Import Namespace="TicketsOnline.Models" %>
<%@ Import Namespace="TicketsOnline.Utils" %>
<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Carrinho de compras</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
    <script src="<%=Url.Content("~/Scripts/Views/Cart.js") %>" type="text/javascript"></script>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <h4>
        Tem os seguintes bilhetes seleccionados para compra</h4>
    <%
        int count = 0;
        double finalPrice = 0.0;
        foreach (CartIndexModel cartItem in Model)
        {
            ++count;
    %>
    <div id="preReserve<%=cartItem.SessionId %>" class="item">
        <%
            double reservePrice = cartItem.PreReserveSeats.Length*cartItem.TicketPrice;
            finalPrice += reservePrice;
            using (Ajax.BeginForm(
                "RemovePreReserve",
                "Cart",
                new RouteValueDictionary { { "session", cartItem.SessionId } },
                new AjaxOptions
                    {
                        HttpMethod = "Post",
                        UpdateTargetId = "preReserve" + cartItem.SessionId,
                        OnSuccess = "OnRemovePreReserveSucess"
                    }))
            {
%>
        Espectáculo:
        <%=cartItem.ShowName%><br />
        Sessão dia
        <%=cartItem.SessionName%><br />
        Os lugares marcados são
        <%=cartItem.PreReserveSeats.AllElementsToString()%>
        totalizando <b>
            <%=reservePrice%>€</b> (<%=cartItem.TicketPrice%>€ cada bilhete)
        <br />
        <input type="submit" value="Remover" onclick="OnRemovePreReserveClick(<%=reservePrice %>, <%=cartItem.SessionId %>)"/>
        <%
            }%>
    </div>
    <br />
    <%
        }
        if (count == 0)
        {	
    %>
    O seu carrinho de compras está vazio!
    <%
        }
        else
        {
    %>
    <%using (Html.BeginForm("Reserve", "Cart"))
      {
    %>
    <script type="text/javascript">
        var FINALPRICE = <%=finalPrice %>;
    </script>
    <h5>
        A sua despesa totaliza <span id="finalPrice">
            <%=finalPrice %></span>€</h5>
    <input type="submit" value="Reservar tudo" />
    <%
        } %>
    <%
        }%>
</asp:Content>
