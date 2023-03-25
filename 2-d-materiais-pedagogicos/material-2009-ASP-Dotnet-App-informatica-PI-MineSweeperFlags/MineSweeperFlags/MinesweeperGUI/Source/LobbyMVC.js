// Lobby MVC initializer


var Lobby = new Object();

Lobby.init = function(pName, eMail, tabId) {

    LobbyModel.init(pName, eMail);
    LobbyView.init(tabId);
    LobbyController.init();
    LobbyController.startPooling();
}


// Lobby Model ---------------------------------------------------------------------------------------

var LobbyModel = new Object();
LobbyModel.init = function(pName, eMail) {

    var _pName = pName;
    var _eMail = eMail;

    this.setPlayerName = function(pName) { _pName = pName; }
    this.getPlayerName = function() { return _pName; }

    this.setPlayerEMail = function(eMail) { _eMail = eMail; }
    this.getPlayerEMail = function() { return _eMail; }
}


// Lobby Controller ----------------------------------------------------------------------------------

var LobbyController = new Object();
LobbyController.init = function() {

    var handlerClass = "Lobby";
    LobbyView.renderProfile("/Profile/GetPlayerPhoto?eMail=" + LobbyModel.getPlayerEMail(), LobbyModel.getPlayerName());
    LobbyView.renderOptions();
    LobbyView.renderFriendList();
    LobbyView.renderPlayerList();
    LobbyView.renderGameList();
    LobbyView.renderMsgInput();


    // --------------------------------
    // Pooling

    var poolingActive = false;

    var pooling = function() {
        if (!poolingActive) return;
        try {
            poolPlayersRefresh();
            poolGamesRefresh();
            poolFriendsRefresh();
            poolMessagesRefresh();
            poolInvitesRefresh();
            //poolProfileRefresh();
        }
        finally { if (poolingActive) setTimeout("LobbyController.doWork()", 1000); }
    }

    this.doWork = function() {
        pooling();
    }

    this.startPooling = function() {
        poolingActive = true;
        pooling();
    }

    this.stopPooling = function() {
        poolingActive = false;
    }

    var poolGamesRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshGames", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            jSon = req.getJSonObject();
            for (var i = 0; i < jSon.length; i++) {
                if (jSon[i].status == GAME_OVER || jSon[i].status == STARTED) {
                    LobbyView.removeGame(jSon[i]);
                } else {
                    LobbyView.addGame(jSon[i]);
                }
            }
        }
    }

    var poolPlayersRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshPlayers", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            jSon = req.getJSonObject();
            for (var i = 0; i < jSon.length; i++) {
                if (jSon[i].status == PLAYER_STATUS_OFF) {
                    LobbyView.removePlayer(jSon[i]);
                } else {
                    LobbyView.addPlayer(jSon[i]);
                }
            }
        }
    }

    var poolFriendsRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshFriends", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            jSon = req.getJSonObject();
            if (jSon.length > 0) {
                for (var i = 0; i < jSon.length; i++) {
                    LobbyView.addFriend(jSon[i]);
                }
            }
        }
    }

    var poolMessagesRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshMessages", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            messages = req.getJSonObject();
            for (var i = 0; i < messages.length; i++)
                LobbyView.addMessage(messages[i]);
        }
    }

    var poolInvitesRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshInvites", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            invites = req.getJSonObject();
            for (var i = 0; i < invites.length; i++)
                LobbyView.addInvite(invites[i]);
        }
    }

    var poolProfileRefresh = function() {
        var req = new HttpRequest(handlerClass, "RefreshProfile", undefined, 0, "eMail", LobbyModel.getPlayerEMail());
        req.Request();
        if (req != "") {
            //Update lobby view with profile updates (name, img,...)
        }

    }

    // --------------------------------    
    // Events

    this.evtShowForum = function() {
        LobbyView.showForum(LobbyModel.getPlayerName(), LobbyModel.getPlayerEMail());
    }
    
    this.evtProceedToGame = function() {
        var isPublicGame = LobbyView.isPublicGame();
        var gName = LobbyView.getGameName();
        if (gName == "") return;

        if (!isPublicGame) {
            selFriendCount = LobbyView.getSelFriendCount();
            if (selFriendCount < 1 || selFriendCount > 4) {
                this.sendMessage("Minimum number of invites for private game is 1, maximum is 3!");
                return;
            }
        }

        try {
            var req = new HttpRequest("Game", "ReserveGameName", gName, 0, "eMail", LobbyModel.getPlayerEMail());
            req.Request();
            if (req.getResponseText() != gName) {
                LobbyView.hideGameForm();
                return;
            }
        }
        catch (e) { alert(e); }

        if (!isPublicGame) {
            selectedFriends = LobbyView.getSelectedFriends();
            for (var i = 0; i < selectedFriends.length; i++) {
                this.evtSendInvite(gName, selectedFriends[i]);
            }
        }

        LobbyView.startGame(gName, LobbyModel.getPlayerName(), LobbyModel.getPlayerEMail(), (isPublicGame ? 1 : 0));
        LobbyView.hideGameForm();
    }

    this.evtStartPublicGame = function() {

        if (!LobbyView.isGameFormVisible()) {
            LobbyView.showGameForm();
            LobbyView.hidePrivateButton();
            return;
        }
    }

    this.evtStartPrivateGame = function() {

        if (!LobbyView.isGameFormVisible()) {
            LobbyView.showGameForm();
            LobbyView.hidePublicButton();
            return;
        }
    }

    this.evtJoinGame = function() {
        var gName = LobbyView.getSelectedGame();

        if (gName == "") return;
        LobbyView.joinGame(gName, LobbyModel.getPlayerName(), LobbyModel.getPlayerEMail());
    }

    this.evtSendInvite = function(gName, pName) {
        try {
            var req = new HttpRequest(handlerClass, "SendInvite", gName, 0, "eMail"
                , LobbyModel.getPlayerEMail(), "friend", pName);
            req.Request();
        }
        catch (e) { alert(e); }
    }

    this.evtAcceptInvite = function(gName) {
        LobbyView.joinGame(gName, LobbyModel.getPlayerName(), LobbyModel.getPlayerEMail());
        LobbyView.removeInvite(gName);
    }

    this.evtRefuseInvite = function(pName) {
        try {
            var req = new HttpRequest(handlerClass, "RefuseInvite", undefined, 0, "eMail"
				, LobbyModel.getPlayerEMail(), "friend", pName);
            req.Request();
        } catch (e) { alert(e); }
        LobbyView.removeinvite(gName);
    }

    this.evtAddFriend = function() {
        try {
            var req = new HttpRequest(handlerClass, "AddFriend", undefined, 0, "eMail"
				, LobbyModel.getPlayerEMail(), "friend", LobbyView.getSelectedPlayer());
            req.Request();
        } catch (e) { alert(e); }
    }

    this.evtRemoveFriend = function() {
        if (LobbyView.getSelFriendCount() == 0) {
            this.sendMessage("At least one friend should be selected in order to proceed with removal!");
            return false;
        }

        for (var i = 0; i < selFriend.length; i++) {
            try {
                var req = new HttpRequest(handlerClass, "RemoveFriend", undefined, 0, "eMail"
				, LobbyModel.getPlayerEMail(), "friend", selFriend[i]);
                req.Request();
            } catch (e) { alert(e); }
        }
        LobbyView.removeFriend(selFriend[i]);

    }

    this.evtEditProfile = function() {
        window.location.href = "/Profile/Edit?eMail=" + escape(LobbyModel.getPlayerEMail());
    }

    this.evtSendMessage = function() {
        if (LobbyView.getMsgInput() == "")
            return;
        try {
            var req = new HttpRequest(handlerClass, "SendMessage", undefined, 0, "eMail"
				, LobbyModel.getPlayerEMail(), "msg", LobbyView.getMsgInput());
            req.Request();
        } catch (e) { alert(e); }
        LobbyView.clearMsgInput();
    }

    this.evtSendPrivateMessage = function() {
        if (LobbyView.getMsgInput() == "")
            return;
        try {
            var req = new HttpRequest(handlerClass, "SendPrivateMessage", undefined, 0, "eMail"
				, LobbyModel.getPlayerEMail(), "eMailTo", LobbyView.getSelectedPlayerTo()
				, "msg", "*" + LobbyView.getMsgInput());
            req.Request();
        } catch (e) { alert(e); }
        LobbyView.clearMsgInput();
    }

    this.sendMessage = function(msg) {
        var message = new Object();
        message.msg = msg;
        message.sender = "MS2500";
        LobbyView.addMessage(message);
    }
}



// Lobby View ----------------------------------------------------------------------------------------

var LobbyView = new Object();
LobbyView.init = function(tabId) {

    var tabElementsCount;
    $(tabId).tabs();
    tabElementsCount = $(tabId).tabs('length');

    this.removeTab = function (tabIdx) { $('#tabs').tabs('remove',tabIdx); }

    var addTab = function(url, label) {
        //$(tabId).tabs('add', url, label + '<a href="javascript:LobbyView.removeTab(' + $(tabId).tabs('length') + ')">X</a>');
        $(tabId).tabs('add', url, label);
        $(tabId).tabs({ cache: true });
        $(tabId).bind('tabsselect', function(event, ui) {
            if (ui.index > (tabElementsCount - 1) && ui.panel.innerHTML.length > 0) {
                $(tabId).tabs('url', ui.index, "");
            }
        });
    }

    this.joinGame = function(gName, playerName, playerEMail) {
        var url = "/Game/Join"
                  + "?gName=" + escape(gName)
                  + "&pName=" + escape(playerName)
                  + "&eMail=" + escape(playerEMail)
        addTab(url, escape(gName));
    }

    this.showForum = function(playerName, playerEMail) {
        var url = "/Forum/Main"
                  + "?pName=" + escape(playerName)
                  + "&eMail=" + escape(playerEMail)
        addTab(url, "Forum");
        LobbyView.hideForumButton();
    }

    this.startGame = function(gName, playerName, playerEMail, type) {
        var url = "/Game/Create"
                  + "?gName=" + escape(gName)
                  + "&pName=" + escape(playerName)
                  + "&eMail=" + escape(playerEMail)
                  + "&type=" + escape(type)
        addTab(url, escape(gName));
    }

    var encodeEmail = function(email) { return email.replace(".", "DOT_SYMBOL").replace("@", "AT_SYMBOL"); }
    var decodeEmail = function(email) { return email.replace("DOT_SYMBOL", ".").replace("AT_SYMBOL", "@"); }

    // --------------------------------
    // Profile Information
    this.renderProfile = function(photoUrl, pName) {
        var playerPhoto = $(".divPhoto");
        var playerName = $(".divPlayerName");
        $("<img/>").addClass("photo").attr("src", photoUrl).appendTo(playerPhoto);
        playerName.text(pName);
    }


    // --------------------------------
    // Options Menu

    this.renderOptions = function() {
        var optionsDiv = $(".divPlayerOptions");
        $("<button/>").click(function() { LobbyController.evtEditProfile(); }).attr("id", "ProfileButton").text("Edit Profile").appendTo(optionsDiv);
        $("<button/>").click(function() { LobbyController.evtShowForum(); }).attr("id", "ForumButton").text("Forum").appendTo(optionsDiv);

        var formDiv = $("<div/>").addClass("divGameForm").attr("id", "gameForm").css("display", "none").attr("valign", "middle");
        $("<input/>").attr("id", "gameNameInput").attr("maxLength", "20").appendTo(formDiv);
        $("<button/>").click(function() { LobbyController.evtProceedToGame(); }).text("Ok").attr({ align: "center", id: "btnProceed" }).appendTo(formDiv);
        $("<button/>").click(function() { LobbyView.hideGameForm(); }).text("Back").attr({ align: "center", id: "btnBack" }).appendTo(formDiv);
        formDiv.appendTo(optionsDiv);

        $("<button/>").click(function() { LobbyController.evtStartPublicGame(); }).attr("id", "StartPublicButton").text("Start Public Game").appendTo(optionsDiv);
        $("<button/>").click(function() { LobbyController.evtStartPrivateGame(); }).attr("id", "StartPrivateButton").text("Start Private Game").appendTo(optionsDiv);
    }

    this.showGameForm = function() {
        $(".divGameForm").show("slow");
        setTimeout('$("#gameForm").focus();', 1000);
    }

    this.hideGameForm = function() {
        $(".divGameForm").hide("slow");
        this.showPublicButton();
        this.showPrivateButton();
        setTimeout('$("#gameNameInput").val("");');
    }

    this.hideForumButton = function() { $("#ForumButton").hide("slow"); }

    this.isGameFormVisible = function() { return $(".divGameForm").is(":visible"); }
    this.isPublicGame = function() { return $("#StartPublicButton").is(":visible"); }

    this.showPublicButton = function() { $("#StartPublicButton").show("slow"); }
    this.hidePublicButton = function() { $("#StartPublicButton").hide("slow"); }

    this.showPrivateButton = function() { $("#StartPrivateButton").show("slow"); }
    this.hidePrivateButton = function() { $("#StartPrivateButton").hide("slow"); }

    this.getGameName = function() { return $("#gameNameInput").val(); }
    this.setGameName = function(name) { $("#gameNameInput").val(name); }


    // --------------------------------
    // Friends List

    this.renderFriendList = function() {
        var listDiv = $(".divPlayerFriendsList");
        $("<button/>").click(function() { LobbyController.evtRemoveFriend(); }).attr("id", "RemoveFriendButton").text("Remove Friend").css("display", "none").appendTo(listDiv);
    }

    this.addFriend = function(player) {
        var listDiv = $("#frList");
        var val = encodeEmail(player.email);

        if ($("#fr_" + val + "").length == 0) {
            var friendItem = $('<input type="checkbox" id="' + val + '" name="friendListItem"</input>&nbsp;');
            $("<dt/>").attr("id", "fr_" + val).toggleClass(player.status).append(friendItem).append($("<span/>").text(player.email)).appendTo(listDiv);
        }
        $("#fr_" + val + "").removeClass().toggleClass(player.status);
        if (getFriendCount() > 0) showRemoveFriendButton();
    }

    this.removeFriend = function(eMail) {
        $("#fr_" + encodeEmail(eMail) + "").remove();
        if (getFriendCount() == 0) hideRemoveFriendButton();
    }

    var getFriendCount = function() { return ($("input[name='friendListItem']").length); }
    this.getSelFriendCount = function() {
        return $("input[name='friendListItem']:checked").length;
    }

    this.getSelectedFriends = function() {

        var selectedFriends;
        var outValues = new Array();
        selectedFriends = $("input[name='friendListItem']:checked");
        for (var i = 0; i < selectedFriends.length; i++) {
            outValues[i] = decodeEmail($(selectedFriends[i]).attr("id"));
        }
        return outValues;
    }

    var showRemoveFriendButton = function() { $("#RemoveFriendButton").show("slow"); }
    var hideRemoveFriendButton = function() { $("#RemoveFriendButton").hide("slow"); }


    // --------------------------------
    // Players List

    this.renderPlayerList = function() {
        var listDiv = $(".divPlayersOnList");
        $("<button/>").click(function() { LobbyController.evtAddFriend(); }).attr("id", "AddPlayerButton").text("Add To Friends").css("display", "none").appendTo(listDiv);
    }

    this.addPlayer = function(player) {
        var listDiv = $("#plList");
        var val = encodeEmail(player.email);

        if ($("#pl_" + val + "").length == 0) {
            var playerItem = $('<input type="radio" id="playerListItem" name="playerListItem" value="' + player.email + '">' + player.email + '</input><br>');
            $("<dt/>").attr("id", "pl_" + val + "").append(playerItem).appendTo(listDiv);
            this.addPlayerTo(player);
        }
        if (getPlayerCount() > 0) showAddPlayerButton();
    }

    this.removePlayer = function(player) {
        var listDiv = $("#plList");
        var val = encodeEmail(player.email);

        $("#pl_" + val + "").remove();
        if (getPlayerCount() == 0) hideAddPlayerButton();
    }

    this.getSelectedPlayer = function() { return ($("input[name='playerListItem']:checked").val()); }
    var getPlayerCount = function() { return ($("#plList <dt/>").length); }
    var showAddPlayerButton = function() { $("#AddPlayerButton").show("slow"); }
    var hideAddPlayerButton = function() { $("#AddPlayerButton").hide("slow"); }


    // --------------------------------
    // Games List

    this.renderGameList = function() {
        var listDiv = $(".divGameOnList");
        $("<button/>").click(function() { LobbyController.evtJoinGame(); }).attr("id", "JoinGameButton").text("Join Game").css("display", "none").appendTo(listDiv);
        showJoinGameButton();
    }

    this.addGame = function(game) {
        var listDiv = $("#gList");
        if ($("#g_" + game.name + "").length == 0) {
            var gameItem = $('<input type="radio" id="gameListItem" name="gameListItem" value="' + game.name + '">' + game.name + '</input><br>');
            $("<dt/>").attr("id", "g_" + game.name).append(gameItem).appendTo(listDiv);
        }
        if (getGameCount() > 0) showJoinGameButton();
    }

    this.removeGame = function(game) {
        $("#g_" + game.name + "").remove();
        if (getGameCount() == 0) hideJoinGameButton();
    }

    this.getSelectedGame = function() { return ($("input[name='gameListItem']:checked").val()); }
    var getGameCount = function() { return ($("#gList <dt/>").length); }
    var showJoinGameButton = function() { $("#JoinGameButton").show("slow"); }
    var hideJoinGameButton = function() { $("#JoinGameButton").hide("slow"); }


    // --------------------------------
    // Invites

    this.addInvite = function(invite) {
        var listDiv = $("#invList");
        if ($("#inv_" + invite.sender + "").length == 0) {
            var invItem = $("<dt/>").attr("id", "inv_" + invite.gName).append(invite.msg);
            var yesOption = $("<button/>").text(" Yes ").click(function() {
                //alert(""+invite.gName);
                LobbyController.evtAcceptInvite(invite.gName);
            }).appendTo(invItem);
            var noOption = $("<button/>").text(" No ").click(function() {
                LobbyController.evtRefuseInvite(invite.sender);
            }).appendTo(invItem);
            invItem.appendTo(listDiv);
        }
    }

    this.removeInvite = function(gName) {
        $("#inv_" + gName + "").remove();
    }


    // --------------------------------
    // Messages Box

    this.addMessage = function(message) {
        var listDiv = $("#msgBoard");
        listDiv.val(listDiv.val() + "<" + message.sender + ">" + (message.msg) + "\n");
    }

    this.clearMsgList = function() { $("#msgBoard").val(""); }


    // --------------------------------
    // Message Input

    this.renderMsgInput = function() {
        var listDiv = $(".divPlayerMessage").attr("id", "playerMsg");
        $("#SendAll").click(function() { LobbyController.evtSendMessage(); });
        $("#SendPrivate").click(function() { LobbyController.evtSendPrivateMessage(); });
    }

    this.clearMsgInput = function() { $("#msgInput").val(""); }
    this.getMsgInput = function() { return $("#msgInput").val(); }


    // --------------------------------
    // Message To Select Box

    this.addPlayerTo = function(player) {
        var selBox = $("#msgDestList");
        if ($("#plTo_" + player.email + "").length == 0) {
            $('<option id="plTo_' + player.email + '">').val(player.email).text(player.email).appendTo(selBox)
        }
    }

    this.removePlayerTo = function(player) {
        $("#plTo_" + player.email + "").remove();
    }

    this.getSelectedPlayerTo = function() { return ($("#msgDestList").val()); }

}
