<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<int>" %>
<%@ Import Namespace="TicketsOnline.Models" %>
<span id="userRating">
    <%
        int rating = 1;
        for (int i = 0; i < StarRatingControlModel.StarCount; i++, rating++)
        {
    %>
    <a onmouseover="OnMouseOver(<%=rating %>)" onmouseout="OnMouseOut(<%=rating %>)"
        onclick="OnClick(<%=rating %>, <%=Model %>)">
        <img name="star<%=i %>" alt="<%=rating %>" height="12" width="12" src="<%=Url.Content("~/Content/Images/Grey_Star_small.gif") %>" />
    </a>
    <%
}
    %>
</span>
<span id="userRatingResponse"></span>