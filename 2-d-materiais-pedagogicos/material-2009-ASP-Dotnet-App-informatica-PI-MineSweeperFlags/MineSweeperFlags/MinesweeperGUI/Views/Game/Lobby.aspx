<%@ Import Namespace="Minesweeper" %>
<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage<Player>" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">	
<html>
<head>
    <title>Lobby</title>
	<script type="text/javascript" src="/source/LobbyMVC.js"></script>
	<script type="text/javascript" src="/source/HttpRequest.js"></script>
	<script type="text/javascript" src="/source/Constants.js"></script>
	<script type="text/javascript" src="/source/jquery-1.3.2.js"></script>

	<link href="/Source/Lobby.css" type="text/css" rel="Stylesheet" />
</head>

<body onload="Lobby.init('<%=Model.Name%>','<%=Model.EMail%>');">
    <div class="divBackGround">
        <div class="divTabList">
            <div class="divTab"></div>
            <div class="divTab"></div>
            <div class="divTab"></div>
            <div class="divTab"></div>
        </div>
        <div class="divPlayerInfo">
            <div class="divTitles">My Information</div>
            <div class="divPhoto"></div>
            <div class="divPlayerName"></div>
            <div class="divPlayerOptions">
                <div class="divTitles">Menu</div>
            </div>
			<div class="divPlayerFriendsList">
			    <div class="divTitles">My Friends</div>
			    <dl id="frList"></dl>
			</div>
        </div>
        <div class="divCommunication">
            <div class="divTitles">Communications</div>
            <div class="divInviteBoard">
                <div class="divTitles">Incoming Invites</div>
                <dl id="invList"></dl>
            </div>
            <div class="divMessageBoard">
                <div class="divTitles">Message Box</div>
                <textarea cols="20" id="msgBoard" readonly="readonly" rows="20"></textarea>
            </div>
            <div class="divPlayerMessage">
                <div class="divTitles">Message</div>
                <textarea cols="20" rows="20" id="msgInput"></textarea>
                <input type="button" id="SendPrivate" value="Send To" />
                <select id="msgDestList"/>
                <input type="button" id="SendAll"value="Send To All" />
            </div>
        </div>
        <div class="divOnLists">
            <div class="divTitles">Online Board</div>
            <div class="divPlayersOnList">
                <div class="divTitles">Players Online</div>
                <dl id="plList"></dl>
            </div>           
            <div class="divGameOnList">
                <div class="divTitles">Games Available</div>
                <dl id="gList"></dl>
            </div>
        </div>        
    </div>
</body>
</html>
