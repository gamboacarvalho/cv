    <%@ Import Namespace="MinesweeperForum" %>
<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<IEnumerable<Post>>" %>

<div id="postListTitle"><%=ViewData["title"] %></div>

<% foreach (Post p in Model){ %>
    <div id="post">
        <div id="postTitle"><% =p.Publisher %></div>
        <div id="postBody"><% =Server.HtmlDecode(p.Body) %></div>
        <div id="postFooter">Added on <%=p.AddDate %></div>
    </div>
    <div id="postSeparator"></div>
<% } %>