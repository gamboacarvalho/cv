function GameMVC(lines, cols, gKey) {
    var current = this;

    // Game Model ---------------------------------------------------------------------------------------
    this.gameModel = function() {
        if (this.gameModel.setGameName != undefined) return;

        var _gStatus = -1;
        var _gName = null;
        var _pName = null;
        var _pEMail = null;
        var _pId = 0;
        var _activePlayer = 0;
        var _isOwner = false;
        var _playerCount = 0;

        this.gameModel.setGameName = function(gName) { _gName = gName; }
        this.gameModel.getGameName = function() { return _gName; }

        this.gameModel.setGameStatus = function(gStatus) { _gStatus = gStatus; }
        this.gameModel.getGameStatus = function() { return _gStatus; }

        this.gameModel.setPlayerName = function(pName) { _pName = pName; }
        this.gameModel.getPlayerName = function() { return _pName; }

        this.gameModel.setPlayerEMail = function(pEMail) { _pEMail = pEMail; }
        this.gameModel.getPlayerEMail = function() { return _pEMail; }

        this.gameModel.setPlayerId = function(pId) { _pId = pId; }
        this.gameModel.getPlayerId = function() { return _pId; }

        this.gameModel.setActivePlayer = function(pId) { _activePlayer = pId; }
        this.gameModel.getActivePlayer = function() { return _activePlayer; }

        this.gameModel.setIsOwner = function(isOwner) { _isOwner = isOwner; }
        this.gameModel.getIsOwner = function() { return (_isOwner == true); }

        this.gameModel.setPlayerCount = function(playerCount) { _playerCount = playerCount; }
        this.gameModel.getPlayerCount = function() { return _playerCount; }        
    }

    // Game Controller ----------------------------------------------------------------------------------

    this.gameController = function() {
        if (this.gameController.doWork != undefined) return;

        var cellObj = new Cell(current.gameController, gKey);
        var board = new BoardMVC(lines, cols, cellObj, gKey);
        var playerObj = new Player(current.gameController, gKey);

        this.gameController.startGame = function(gName, pName, pEMail, pId, isOwner) {
            current.gameModel.setGameName(gName);
            current.gameModel.setPlayerName(pName);
            current.gameModel.setPlayerEMail(pEMail);
            current.gameModel.setPlayerId(pId);
            current.gameModel.setIsOwner(isOwner);

            current.gameView.renderOptions();

            if (current.gameModel.getIsOwner()) {
                current.gameView.showStartButton();
            } else {
                current.gameView.showMsgButton("Waiting to Start");
            }

            current.gameController.startPooling();
        }


        // --------------------------------
        // Pooling

        var poolingActive = false;

        var pooling = function() {
            if (!poolingActive) return;
            try {
                poolPlayerRefresh();

                if (current.gameModel.getGameStatus() == -1) {
                    var game = null;
                    var req = new HttpRequest("GameAsynchronous", "RefreshGameInfo", current.gameModel.getGameName(), current.gameModel.getPlayerId());
                    req.Request();
                    if (req != "") game = req.getJSonObject();

                    current.gameModel.setPlayerCount(game.PlayersCount);

                    if (current.gameModel.getIsOwner()) { // Verifica se há mais do que 1 Jogador
                        if (game.PlayersCount > 1) {
                            current.gameView.enableStartGameButton();
                        }
                    } else { // Verifica que o jogo já iniciou
                        current.gameModel.setGameStatus(game.gStatus);
                    }
                } else {
                    poolPlayerRefresh();
                    poolCellRefresh();
                    poolGameRefresh();
                }
            }
            finally { if (poolingActive) setTimeout(gKey + ".gameController.doWork()", 1000); }
        }

        this.gameController.doWork = function() {
            pooling();
        }

        this.gameController.startPooling = function() {
            poolingActive = true;
            pooling();
        }

        this.gameController.stopPooling = function() {
            poolingActive = false;
        }

        var poolPlayerRefresh = function() {
            var req = new HttpRequest("GameAsynchronous", "RefreshPlayerBoard", current.gameModel.getGameName(), current.gameModel.getPlayerId());
            req.Request();
            if (req != "") {
                var p = req.getJSonObject();
                for (var i = 0; i < p.length; i++) {
                    if (p[i].active == 0)
                        playerObj.removePlayer(p[i].id);
                    playerObj.update(p[i].id, p[i].name, p[i].points, current.gameModel.getPlayerId());
                }
            }
        }

        var poolGameRefresh = function() {
            var req = new HttpRequest("GameAsynchronous", "RefreshGameInfo", current.gameModel.getGameName(), current.gameModel.getPlayerId());
            req.Request();
            if (req != "") {
                var game = req.getJSonObject();
                current.gameModel.setActivePlayer(game.activePlayer);
                playerObj.activatePlayer(game.activePlayer);
                current.gameView.renderMinesLeft(game.minesLeft);

                if (current.gameModel.getGameStatus() == WAITING_FOR_PLAYERS && game.gStatus == STARTED) {
                    current.gameView.hideOptions();
                    current.gameModel.setGameStatus(game.gStatus);
                    board.init();
                    board.boardController.start();
                }
                else if (game.gStatus == GAME_OVER) {
                    current.gameController.stopPooling();
                    current.gameView.renderGameOver("Game over! Player '" + game.activePlayer + "' won!");
                }
            }
        }

        var poolCellRefresh = function() {
            var req = new HttpRequest("GameAsynchronous", "RefreshCell", current.gameModel.getGameName(), current.gameModel.getPlayerId());
            req.Request();
            if (req != "") {
                var cell = req.getJSonObject();
                for (var i = 0; i < cell.length; i++) {
                    cellObj.update(board.boardView.getCellByPos(cell[i].posX, cell[i].posY), cell[i].type, cell[i].owner, cell[i].value);
                }
            }
        }

        this.gameController.getPlayerCount = function() {
            return current.gameModel.getPlayerCount();
        }

        // --------------------------------    
        // Events


        this.gameController.evtStartGame = function() {
            current.gameController.stopPooling();
            try {
                var req = new HttpRequest("GameAsynchronous", "StartGame", current.gameModel.getGameName(), 1);
                req.Request();
                var game = req.getJSonObject();
            } catch (e) { alert(e); }

            if (game.gStatus == STARTED) {
                current.gameModel.setGameStatus(STARTED);

                board.init();
                board.boardController.start();
                playerObj.activatePlayer(game.activePlayer);
                current.gameView.hideOptions();
            }
            current.gameController.startPooling();
        }

        this.gameController.evtRemovePlayer = function() {
            try {
                var req = new HttpRequest("GameAsynchronous", "RemovePlayer", current.gameModel.getGameName(), current.gameModel.getPlayerId());
                req.Request();
            } catch (e) { alert(e); }
            current.gameController.stopPooling();
            current.gameView.renderPlayerQuit("Game over! You suck!");
            //location.reload(true);
        }

        this.gameController.evtCellClicked = function(cell) {
            if ((parseInt(current.gameModel.getActivePlayer()) + 1) == parseInt(current.gameModel.getPlayerId())) {
                var pos = cellObj.getPos(jQuery(cell));
                try {
                    var req = new HttpRequest("GameAsynchronous", "Play", current.gameModel.getGameName(), current.gameModel.getPlayerId()
                    , "posX", pos[0], "posY", pos[1]);
                    req.Request();
                } catch (e) { alert(e); }
            }
            else
                this.sendMessage("Wait for your turn to play!");
        }

        this.gameController.evtRevealBoard = function() {
            try {
                var req = new HttpRequest("GameAsynchronous", "RevealBoard", current.gameModel.getGameName(), current.gameModel.getPlayerId());
                req.Request();
                if (req != "") {
                    var cell = req.getJSonObject();
                    for (var i = 0; i < cell.length; i++) {
                        cellObj.update(board.boardView.getCellByPos(cell[i].posX, cell[i].posY), cell[i].type, cell[i].owner, cell[i].value);
                    }
                }
            } catch (e) { alert(e); }

            current.gameView.hideOptions();
            //board.boardController.revealBoard();
        }

        // --------------------------------
        // Messages

        this.gameController.sendMessage = function(msg) {
            current.gameView.renderMessage(msg);
        }

    }

    this.gameView = function() {

        // --------------------------------
        // Game Board

        if (current.gameView.renderBoard != undefined) return;

        var poolingActive = false;

        this.gameView.renderBoard = function() {
            $("." + getRealId(gKey, BOARD_CLASS)).empty();
            BoardView.render();
        }

        this.gameView.enableStartGameButton = function() {
            $("#" + getRealId(gKey, "StartButton")).attr("disabled", "");
        }

        this.gameView.hideStartGameButton = function() {
            $("#" + getRealId(gKey, "StartButton")).attr("disabled", "");
        }


        // --------------------------------
        // Options Menu

        this.gameView.renderOptions = function() {
            var optionsDiv = $("#" + getRealId(gKey, "divOptions"));
            $("<button/>").attr("id", getRealId(gKey, "MsgButton")).appendTo(optionsDiv).css("display", "none");
            $("<button/>").click(function() { current.gameController.evtStartGame(); }).attr("disabled", "disabled").attr("id", getRealId(gKey, "StartButton")).text("Start Game").appendTo(optionsDiv).css("display", "none");
            $("<button/>").click(function() { current.gameController.evtRevealBoard() }).attr("id", getRealId(gKey, "RevealButton")).text("Reveal game board").appendTo(optionsDiv).css("display", "none");
            optionsDiv.css("display", "block");
        }

        this.gameView.renderPlayerQuit = function(msg) {
            current.gameView.showMsgButton(msg);
        }
        this.gameView.renderGameOver = function(msg) {
            current.gameView.renderOptions();
            current.gameView.showRevealButton();
            current.gameView.showMsgButton(msg);
        }

        this.gameView.hideOptions = function() {
            $("#" + getRealId(gKey, "divOptions")).hide("slow", current.gameView.clearOptions);
        }

        this.gameView.clearOptions = function() {
            $("#" + getRealId(gKey, "divOptions")).empty();
        }

        this.gameView.showMainOptions = function() {
            current.gameView.hideOptions();
            current.gameView.startPooling();
        }

        var pooling = function() {
            if (!poolingActive) return;
            try {
                current.gameView.renderOptions();
                current.gameView.showListButton()
                current.gameView.showCreateButton();
            }
            finally { if (poolingActive) setTimeout(gKey + ".gameView.doWork()", 1000); }
        }

        this.gameView.doWork = function() {
            pooling();
        }

        this.gameView.startPooling = function() {
            poolingActive = true;
            pooling();
        }

        this.gameView.stopPooling = function() {
            poolingActive = false;
        }

        this.gameView.hideStartButton = function() { $("#" + getRealId(gKey, "StartButton")).hide("slow"); }
        this.gameView.showStartButton = function() { $("#" + getRealId(gKey, "StartButton")).show("slow"); }

        this.gameView.hideMsgButton = function() { $("#" + getRealId(gKey, "WaitButton")).hide("slow"); }
        this.gameView.showMsgButton = function(msg) { $("#" + getRealId(gKey, "MsgButton")).text(msg).show("slow"); }

        this.gameView.hideRevealButton = function() { $("#" + getRealId(gKey, "RevealButton")).hide("slow"); }
        this.gameView.showRevealButton = function() { $("#" + getRealId(gKey, "RevealButton")).show("slow"); }


        // --------------------------------
        // Messages

        this.gameView.renderMessage = function(msg) {
            $("#" + getRealId(gKey, MSGBOARD_CLASS) + " #" + getRealId(gKey, MSG_CLASS)).text(msg);
        }


        // --------------------------------
        // Mines Left

        this.gameView.renderMinesLeft = function(minesLeft) {
            $("#" + getRealId(gKey, SCORE_LABEL)).text("Mines Left");
            $("#" + getRealId(gKey, SCORE_VALUE)).text(minesLeft);
        }

    }

    this.init = function() {
        current.gameModel();
        current.gameView();
        current.gameController();
    }
}