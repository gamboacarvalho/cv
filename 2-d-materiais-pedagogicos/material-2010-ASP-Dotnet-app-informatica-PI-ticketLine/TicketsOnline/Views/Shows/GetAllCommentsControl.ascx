<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<System.Collections.Generic.IEnumerable<RepositoryInterfaces.DataObjects.IComment>>" %>
<%@ Import Namespace="RepositoryInterfaces.DataObjects" %>
<br />
<%
    int count = 0;
    foreach (IComment comment in Model)
    {
        ++count;
%>
<div class="commentBox">
    <div>
        <b>Nome: </b>
        <%=comment.UserName %></div>
    <div>
        <b>Data de inserção: </b>
        <%=comment.CommentDate %></div>
    <div>
        <b>Titulo: </b>
        <%=Html.Encode(comment.Title) %></div>
    <div class="commentUserText">
        <b>Comentário: </b>
        <br />
        <%=Html.Encode(comment.UserComment) %>
    </div>
</div>
<br />
<%
    }
    if (count == 0)
    {
%>
<b>Não existem comentários para mostrar</b>
<%
    }
%>
<br />
