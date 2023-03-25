<%@ Import Namespace="MinesweeperForum" %>
<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<Thread>" %>

Thread Title
<%=Html.TextBox("thread.Title") %>

<input type="button" name="submit" value="Ok" onclick="ForumController.evtAddThread();" />