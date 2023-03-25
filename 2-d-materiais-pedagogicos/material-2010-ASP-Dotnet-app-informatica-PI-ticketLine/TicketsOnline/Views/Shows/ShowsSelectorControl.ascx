<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.ShowsSelectorControlModel>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<%=Model.Title %>
<select id="<%=Model.SelectorId %>" onchange="<%=Model.OnChangeEventExtraCode %>FormAjaxRequest('<%=Model.SelectorId %>', '<%=Url.Action(Model.Action, Model.Controller) %>', '<%=Model.DomIdToUpdate %>');">
    <option selected="selected" value="-1">[escolha uma opção]</option>
    <%
        foreach (SelectorOptionModel option in Model.SelectorOptions)
        {
    %>
    <option value="<%=option.Id %>">
        <%=option.Text %></option>
    <%
        } %>
</select>
<img id="loadImg<%=Model.SelectorId %>" style="display:none" src="<%=Url.Content("~/Content/Images/ajax-loader.gif") %>" alt="loading..."/>
