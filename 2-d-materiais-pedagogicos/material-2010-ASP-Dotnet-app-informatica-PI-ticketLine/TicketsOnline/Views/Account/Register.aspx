<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<TicketsOnline.Models.RegisterFormModel>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Registar
</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="MainContent">
    <% Html.EnableClientValidation(); %>
    <%using (Html.BeginForm("Register", "Account", FormMethod.Post, new { enctype = "multipart/form-data" }))
      {%>
    <fieldset>
        <p>
            Username:
            <%=Html.TextBoxFor(m => m.Username) %>
            <%=Html.ValidationMessageFor(m => m.Username) %>
        </p>
        <p>
            Email:
            <%=Html.TextBoxFor(m => m.Email) %>
            <%=Html.ValidationMessageFor(m => m.Email) %>
        </p>
        <p>
            Nome:
            <%=Html.TextBoxFor(m => m.Name) %>
            <%=Html.ValidationMessageFor(m => m.Name) %>
        </p>
        <p>
            Password:
            <%=Html.PasswordFor(m => m.Password) %>
            <%=Html.ValidationMessageFor(m => m.Password) %>
        </p>
        <p>
            Validar password:
            <%=Html.PasswordFor(m => m.ValidatePassword) %>
            <%=Html.ValidationMessageFor(m => m.ValidatePassword) %>
        </p>
        <p>
            Imagem:
            <input type="file" name="image" />
        </p>
        <p>
            <input type="submit" value="Registar" />
        </p>
    </fieldset>
    <% } %>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="HeaderContent">
    <script src="<%=Url.Content("~/Scripts/MicrosoftMvcValidation.js") %>" type="text/javascript"></script>
</asp:Content>
