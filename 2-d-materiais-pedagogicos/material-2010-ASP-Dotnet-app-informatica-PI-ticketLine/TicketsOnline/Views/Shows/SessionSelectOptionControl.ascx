<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.SessionSelectOptionModel>" %>
<h2>
    <%=Model.ShowName %></h2>
Votação:
<%Html.RenderPartial("StarRatingControl", Model.StarRatingControlModel); %><br />
<br />
<%Html.RenderPartial("ShowsSelectorControl", Model.ShowsSelectorControlModel); %><br />
<br />
<div id='room'>
</div>
<div id="comments">
    <a href="<%=Url.Action("GetCommentsInterface", "Shows") %>" onclick="getCommentsInterface(<%=Model.ShowId %>); return false;">
        Ver/escrever comentários</a>
</div>
