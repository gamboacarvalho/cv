<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.LogOnFormModel>" %>
<div class="popup">
    <a href="" onclick="$('.popup').remove(); return false;">fechar</a><br />
    <br />
    <div id="popupForm">
        <b>Entre na sua conta! Ou <%=Html.ActionLink("Registe-se", "Register", "Account") %></b>
        <form action="" id="logonPopupForm" onsubmit="popUpLogOnSubmit(this); return false;">
        <fieldset>
            <p>
                Username:
                <%=Html.TextBoxFor(m => m.Username) %>
            </p>
            <p>
                Password:
                <%=Html.PasswordFor(m => m.Password) %>
            </p>
            <p>
                <input type="submit" value="Entrar"/>
            </p>
        </fieldset>
        </form>
    </div>
    <span id="popUpError"></span>
</div>
