<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage<Player>" %>
<%@ Import Namespace="Minesweeper" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<head>
    <link type="text/css" href="http://jqueryui.com/latest/themes/base/ui.all.css" rel="stylesheet" />
    <link rel="Stylesheet" type="text/css" href="/Source/Forum.css" />
    <link rel="Stylesheet" type="text/css" href="/Source/tabs-ui.css" />
    <link rel="Stylesheet" type="text/css" href="/Source/Lobby.css" />
    <link rel="Stylesheet" type="text/css" href="/Source/mineSweeper.css" />
    
    <script type="text/javascript" src="/Source/jquery-1.3.2.js"></script>
    <script type="text/javascript" src="/Source/ui.core.js"></script>
    <script type="text/javascript" src="/Source/ui.tabs.js"></script>
    <script type="text/javascript" src="/Source/Constants.js"></script>
    <script type="text/javascript" src="/Source/HttpRequest.js"></script>
    <script type="text/javascript" src="/source/LobbyMVC.js"></script>
    <script type="text/javascript" src="/source/ForumMVC.js"></script>
    <script type="text/javascript" src="/Source/Cell.js"></script>
    <script type="text/javascript" src="/Source/Player.js"></script>
    <script type="text/javascript" src="/Source/BoardMVC.js"></script>    
    <script type="text/javascript" src="/Source/GameMVC.js"></script>        
    <script type="text/javascript">
        $(document).ready(function() {
            Lobby.init("<%=Html.Encode( Model.Name )%>", "<%=Html.Encode( Model.EMail )%>", "#tabs");
        });
    </script>

</head>
<body style="font-size: 62.5%;">
    <div id="tabs" class="divTabs">
        <ul id="tabStrip">
            <li><a href="#lobby"><span>Lobby</span></a></li>
        </ul>
        <div id="lobby" class="divLobbyBackGround">
            <div class="divPlayerInfo">
                <% Html.RenderPartial("MyInformation"); %>
                <div class="divPlayerOptions">
                    <div class="divTitles">Menu</div>
                </div>
                <div class="divPlayerFriendsList">
                    <div class="divTitles">My Friends</div>
                    <dl id="frList">
                    </dl>
                </div>
            </div>
            <div class="divCommunication">
                <div class="divTitles">Communications</div>
                <div class="divInviteBoard">
                    <div class="divTitles">Incoming Invites</div>
                    <dl id="invList">
                    </dl>
                </div>
                <div class="divMessageBoard">
                    <div class="divTitles">Message Box</div>
                    <textarea cols="20" rows="11" id="msgBoard" readonly="readonly"></textarea>
                </div>
                <div class="divPlayerMessage">
                    <div class="divTitles">Message</div>
                    <textarea cols="20" rows="11" id="msgInput"></textarea>
                    <input type="button" id="SendPrivate" value="Send To" />
                    <select id="msgDestList" />
                    <input type="button" id="SendAll" value="Send To All" />
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
    </div>
    <%--<a href="/debug/main.htm" target="_blank">Show Content</a>--%>
</body>
</html>
