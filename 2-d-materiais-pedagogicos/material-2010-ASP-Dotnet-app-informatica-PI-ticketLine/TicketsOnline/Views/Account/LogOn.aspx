<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<TicketsOnline.Models.LogOnFormModel>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Entrar
</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
    <script src="<%=Url.Content("~/Scripts/MicrosoftMvcValidation.js") %>" type="text/javascript"></script>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <% Html.EnableClientValidation(); %>
    <%using (Html.BeginForm())
      {%>
    <fieldset>
        <p>
            Username:
            <%=Html.TextBoxFor(m => m.Username) %>
            <%=Html.ValidationMessageFor(m => m.Username) %>
        </p>
        <p>
            Password:
            <%=Html.PasswordFor(m => m.Password) %>
            <%=Html.ValidationMessageFor(m => m.Password) %>
        </p>
        <p>
            <input type="submit" value="Entrar" />
        </p>
        <p>
            Não se encontra registado?
            <%=Html.ActionLink("Registe-se", "Register")%>.
        </p>
    </fieldset>
    <% } %>
</asp:Content>
