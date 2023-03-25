<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl" %>
<%@ Import Namespace="Repository" %>
<%
    if (Request.IsAuthenticated)
    {
%>
Seja bem vindo <b>
    <%= DataRepository.UserMapperFactory.GetMapper().Get(Page.User.Identity.Name).Name%></b>!
[
<%: Html.ActionLink("Gerir conta", "Manage", "Account") %>
][
<%: Html.ActionLink("Log Off", "LogOff", "Account") %>
]
<%
    }
    else
    {
%>
Para iniciar sessão clique aqui [
<%: Html.ActionLink("Log On", "LogOn", "Account") %>
]
<%
    }
%>
