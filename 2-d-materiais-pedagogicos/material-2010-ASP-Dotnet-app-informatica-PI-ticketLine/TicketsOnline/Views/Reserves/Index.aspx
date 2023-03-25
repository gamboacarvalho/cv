<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<System.Collections.Generic.IEnumerable<RepositoryInterfaces.DataObjects.IReserve>>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<%@ Import Namespace="RepositoryInterfaces.DataObjects" %>
<%@ Import Namespace="TicketsOnline.Utils" %>
<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Lista de Reservas</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <%
        int count = 0;
        foreach (IReserve reserve in Model)
        {
            ++count;
    %>
    <div id="reserve<%=reserve.Id %>" class="item">
        <%
            using (Ajax.BeginForm(
                "Delete",
                "Reserves",
                new RouteValueDictionary { { "id", reserve.Id } },
                new AjaxOptions
                    {
                        HttpMethod = "Post",
                        UpdateTargetId = "reserve" + reserve.Id
                    }))
            {
        %>
        Reserva global nº
        <%=reserve.Id%><br />
        Espectáculo:
        <%=reserve.Session.Show.Name%>
        (<%=reserve.Session.Show.Description%>)<br />
        Ocorrerá em
        <%=reserve.Session.Name%><br />
        Localização:
        <%=reserve.Session.Room.Id%>
        , cadeiras
        <%=reserve.Seats.AllElementsToString()%><br />
        <input type="submit" value="Cancelar reserva" />
        <%
            }
        %>
    </div>
    <br />
    <%
        } if (count == 0)
        { %>
    Não tem reservas para visualizar!
    <%
        }%>
</asp:Content>
