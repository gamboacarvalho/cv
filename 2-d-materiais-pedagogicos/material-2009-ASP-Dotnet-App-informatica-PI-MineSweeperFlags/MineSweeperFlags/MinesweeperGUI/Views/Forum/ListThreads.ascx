<%@ Import Namespace="MinesweeperForum" %>
<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<IEnumerable<Thread>>" %>

<div id="threadListTitle">Current Threads</div>
<%
int count = 0;
string rowClass = "";

    foreach (Thread t in Model){%>
        <% rowClass = (count++ % 2 == 0) ? "Even" : "Odd"; %>
        <div id="thread">
            <div id="threadTitle" class="<%=rowClass%>">
                <a href="javascript:ForumController.evtListPosts('<%=t.Id%>');"><%=t.Title %></a>
            </div>
            <div id="threadFooter">
                Published by <%=t.Publisher %> on <%=t.AddDate %> (<%=t.Visits %> visits)
            </div>
        </div>
        <div id="threadSeparator"></div>
<%} %>
