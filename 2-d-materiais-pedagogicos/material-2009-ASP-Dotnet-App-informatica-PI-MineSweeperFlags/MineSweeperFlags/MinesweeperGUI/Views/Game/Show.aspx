<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>
<%@ Import Namespace="MinesweeperGUI.ExtensionMethods"    %>

<script type="text/javascript">
        var <%=ViewData["gKey"]%> = null;        
        $(document).ready(function() {
            <%=ViewData["gKey"]%> = new GameMVC(LINES, COLS, "<%=ViewData["gKey"]%>");
            <%=ViewData["gKey"]%>.init();
            <%=ViewData["gKey"]%>.gameController.startGame(
                    "<%= Html.Encode(ViewData["gName"])%>",
                    "<%= Html.Encode(ViewData["pName"])%>",
                    "<%= Html.Encode(ViewData["pEMail"])%>",
                    <%= Html.Encode(ViewData["pId"])%>,
                    <%=Html.Encode(Html.JavaScriptBooleanValue((bool)ViewData["isOwner"]))%>
                    
                    );
        });
</script>
  
<div class="divBackGround">
    <div id="<%=ViewData["gKey"]%>divPlayerBoard" class="divPlayerBoard"/>
    <div id="<%=ViewData["gKey"]%>divArena" class="divArena"/>    
    <div id="<%=ViewData["gKey"]%>divScoreBoard" class="divScoreBoard">
        <div id="<%=ViewData["gKey"]%>divScoreLabel" class="divScoreLabel"/>
        <div id="<%=ViewData["gKey"]%>divScoreValue" class="divScoreValue"/>
    </div>
    <div id="<%=ViewData["gKey"]%>divOptions" class="divOptions"/>
    <div id="<%=ViewData["gKey"]%>divMessageBoard" class="divMessageBoard">
        <div id="divMessage" class="divMessage">
            .: GR9 - MineSweeper Two Thousand And a Half :.</div>
    </div>
</div>
