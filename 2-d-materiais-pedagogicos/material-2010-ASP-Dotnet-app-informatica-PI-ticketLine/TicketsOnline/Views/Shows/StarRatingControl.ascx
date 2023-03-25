<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<TicketsOnline.Models.StarRatingControlModel>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<span id="showRating" title="votos: <%=Model.TotalVotes %>">
    <%
        int i;
        for (i = 0; i < Model.Rating; i++)
        {
    %>
    <a>
        <img alt="<%=Model %>" height="12" width="12" src="<%=Url.Content("~/Content/Images/Red_Star_small.gif") %>" />
    </a>
    <%
        }
    for (; i < StarRatingControlModel.StarCount; i++)
    {
    %>
    <a>
        <img alt="<%=Model %>" height="12" width="12" src="<%=Url.Content("~/Content/Images/Grey_Star_small.gif") %>" />
    </a>
    <%
        }
    %>
    <%=Model.Text %>
</span>
