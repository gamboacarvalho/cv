<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<int>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<h3>
    Comentários ao espectáculo</h3>
<div id="allcomments">
</div>
<br />
<%=Ajax.ActionLink(
                "Actualizar comentários",
                "GetAllComments",
                "Shows",
                new RouteValueDictionary{{"id", Model}},
                new AjaxOptions { UpdateTargetId = "allcomments", HttpMethod = "Get"})%>
<div id="usercomment">
    <br />
    <%Html.RenderPartial("UserCommentControl", new CommentFormModel { ShowId = Model }); %>
</div>
