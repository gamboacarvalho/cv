<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<int>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<h3>
    Coment�rios ao espect�culo</h3>
<div id="allcomments">
</div>
<br />
<%=Ajax.ActionLink(
                "Actualizar coment�rios",
                "GetAllComments",
                "Shows",
                new RouteValueDictionary{{"id", Model}},
                new AjaxOptions { UpdateTargetId = "allcomments", HttpMethod = "Get"})%>
<div id="usercomment">
    <br />
    <%Html.RenderPartial("UserCommentControl", new CommentFormModel { ShowId = Model }); %>
</div>
