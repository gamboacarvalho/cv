<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<TicketsOnline.Models.ManageFormModel>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Gestão de conta</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
    <script src="<%=Url.Content("~/Scripts/MicrosoftMvcValidation.js") %>" type="text/javascript"></script>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <% Html.EnableClientValidation(); %>
    <%using (Html.BeginForm("Manage", "Account", FormMethod.Post, new { enctype = "multipart/form-data" }))
      {%>
    <fieldset>
        <p>
            Username:
            <%= Page.User.Identity.Name %>
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
            Imagem:
            <%if (Model.HasImage)
              {
            %>
            <img src="<%=Url.Action("GetImage", new{id=Page.User.Identity.Name}) %>" alt="Não é possivel mostrar a imagem" />
            <%
                }
              else
              {
            %>
            <img src="<%=Url.Content("~/Content/Images/Default_profile.jpg") %>" alt="Não é possivel mostrar a imagem" />
            <%
                } %>
            <br />
            <input type="file" name="image" />
        </p>
        <p>
            Password antiga:
            <%=Html.PasswordFor(m => m.Password) %>
            <%=Html.ValidationMessageFor(m => m.Password) %>
        </p>
        <p>
            Nova password:
            <%=Html.PasswordFor(m => m.NewPassword) %>
            <%=Html.ValidationMessageFor(m => m.NewPassword) %>
        </p>
        <p>
            Validar password:
            <%=Html.PasswordFor(m => m.ValidatePassword) %>
            <%=Html.ValidationMessageFor(m => m.ValidatePassword) %>
        </p>
        <p>
            <input type="submit" value="Actualizar" />
        </p>
    </fieldset>
    <% } %>
</asp:Content>
