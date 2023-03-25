<%@ Import Namespace="MinesweeperForum" %>
<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>

<script type="text/javascript">
    $(document).ready(function() {
        Forum.init(<%="'"+ViewData["pName"]+"'"%>,<%="'"+ViewData["eMail"]+"'"%>);
    });
</script>

<div id="forumBackground" class="forumBackground">
    <div id="forumTitle" class="forumTitle">
        <h1>MineSweeper Two Thousand And a Half Forum</h1>
    </div>
    <div class="forumLeftBar">
        <div id="forumNav" class="forumNav"></div>
        <div id="forumThreadEntry" class="forumThreadEntry"></div>
        <div id="forumPostEntry" class="forumPostEntry"></div>
    </div>
    <div id="forumContent" class="forumContent">
    </div>
</div>
