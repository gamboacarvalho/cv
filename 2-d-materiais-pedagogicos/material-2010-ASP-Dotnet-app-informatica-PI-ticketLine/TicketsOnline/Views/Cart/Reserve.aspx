<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<System.Collections.Generic.IEnumerable<TicketsOnline.Models.CartReserveModel>>"
    MasterPageFile="~/Views/Shared/Site.Master" %>


<%@ Import Namespace="TicketsOnline.Models" %>
<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Resumo das reservas
</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
    <style type="text/css">
        .sucess
        {
            color: green;
        }
        .fail
        {
            color: red;
        }
    </style>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <h3>
        Resultado da reserva</h3>
    <table>
        <tr>
            <th>
                Estado
            </th>
            <th>
                Espect�culo
            </th>
            <th>
                Sess�o
            </th>
        </tr>
        <%
            bool allReserved = true;
            foreach (CartReserveModel cartReserveModel in Model)
            {
        %>
        <tr>
            <td>
                <%
                    allReserved &= cartReserveModel.Reserved; 
                %>
                <%=cartReserveModel.Reserved ? "<b class='sucess'>Sucesso</b>" : "<b class='fail'>Falhou</b>"%>
            </td>
            <td>
                <%=cartReserveModel.ShowName %>
            </td>
            <td>
                <%=cartReserveModel.SessionName %>
            </td>
        </tr>
        <%
} %>
    </table>
    <p>
        <%if (allReserved)
          {
        %>
        <b>Todas as reservas foram bem sucedidas.</b><br />
        Para mais detalhes e visualizar todas as suas reservas clique
        <%=Html.ActionLink("aqui", "Index", "Reserves") %>.
        <%
            }
          else
          {
        %>
        <b>Algumas reservas n�o ocorreram com sucesso.</b><br />
        Volte a verificar os lugares na sess�o e espect�culo correspondentes e tente novamente.<br />
        Para voltar ao carrinho de compras e ver a informa��o mais detalhada clique
        <%=Html.ActionLink("aqui", "Index", "Cart") %>.
        <%
            } %>
    </p>
</asp:Content>
