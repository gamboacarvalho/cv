<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.CommentFormModel>" %>
<h4>
    Deixe o seu comentário</h4>
<p>
    <b>
        <%=Html.DisplayTextFor(m => Model.Status) %></b>
</p>
Vote neste espectáculo:
<%Html.RenderPartial("UserStarRatingControl", Model.ShowId); %><br />
<%
    using (Ajax.BeginForm("Comment", "Shows", new { Model.ShowId }, new AjaxOptions { UpdateTargetId = "usercomment" }))
    {
%>
<fieldset>
    <p>
        Titulo:
        <%=Html.TextBoxFor(m => Model.Title) %>
        <%=Html.ValidationMessageFor(m => Model.Title) %>
    </p>
    <p>
        Comentário:<br />
        <%=Html.TextAreaFor(m => Model.Comment) %>
        <%=Html.ValidationMessageFor(m => Model.Comment) %>
    </p>
    <input type="submit" value="Comentar" />
</fieldset>
<%
    } %>