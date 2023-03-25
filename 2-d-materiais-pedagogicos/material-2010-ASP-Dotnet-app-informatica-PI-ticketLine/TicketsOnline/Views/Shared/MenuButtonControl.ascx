<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.MenuButtonControlModel>" %>

<%using (Html.BeginForm(Model.Action, Model.Controller, FormMethod.Get))
{
  %>
  <input type="submit" value="<%=Model.ButtonValue %>" />
  <%
} %>
