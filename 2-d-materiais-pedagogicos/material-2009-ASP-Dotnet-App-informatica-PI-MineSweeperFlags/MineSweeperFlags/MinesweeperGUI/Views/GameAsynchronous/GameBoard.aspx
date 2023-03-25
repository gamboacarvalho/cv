<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>
<%@ Import Namespace="MinesweeperGUI.ExtensionMethods" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">	
<html>
<head>
    <title>Board Tests</title>
    <script type="text/javascript" src="/Source/Constants.js"></script>
    <script type="text/javascript" src="/Source/HttpRequest.js"></script>
    <script type="text/javascript" src="/Source/Cell.js"></script>
    <script type="text/javascript" src="/Source/Player.js"></script>
    <script type="text/javascript" src="/Source/BoardMVC.js"></script>
    <script type="text/javascript" src="/Source/GameMVC.js"></script>
    <script type="text/javascript" src="/Source/jquery-1.3.2.js"></script>
    <script type="text/javascript">
        var <%=ViewData["gKey"]%> = null;        
        $(document).ready(function() {
            <%=ViewData["gKey"]%> = new GameMVC(LINES, COLS, "<%=ViewData["gKey"]%>");
            <%=ViewData["gKey"]%>.init();
            <%=ViewData["gKey"]%>.gameController.startGame("<%=ViewData["gName"]%>", "<%=ViewData["pName"]%>", "<%=ViewData["pEMail"]%>", <%=ViewData["pId"]%>, <%=Html.JavaScriptBooleanValue((bool)ViewData["isOwner"])%> );
        });
    </script>
    <link rel="Stylesheet" type="text/css" href="/Source/mineSweeper.css" />	
</head>
<body onload="">
	<p/>
	<div class="divBackGround">
		<div id="divPlayerBoard" class="divPlayerBoard">

		</div>

		<div id="divArena" class="divArena"></div>
			<div id="divScoreBoard" class="divScoreBoard">
				<div class="divScoreLabel"></div>
				<div class="divScoreValue"></div>
			</div>		
		<div class="divOptions"></div>
		<div id="divMessageBoard" class="divMessageBoard">
			<div class="divMessage">.: GR9 - MineSweeper Two Thousand And a Half :.</div>
		</div>
	</div>
	<p/>
</body>
</html>