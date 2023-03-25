function Player(gCtrl, gKey) {

    var current = this;

    var getPlayerId = function(pNum) {        
        return gKey + "Player" + pNum;
    }

    var getPlayerKey = function(pNum) {        
        return "#" + getPlayerId(pNum);
    }

    var getElementId = function(id, elementName) {
        return getRealId(gKey, elementName) + id;
    }

    this.update = function(pNum, pName, pScore, myId) {
    if ($(getPlayerKey(pNum)).length == 0)
            renderNew(pNum, pName, myId);
        setScore(pNum, pScore);
    }

    var renderNew = function(pNum, pName, myId) {
        var playerDiv = $("<div/>").addClass(PL_BOARD_CLASS).attr("id", getPlayerId(pNum));
        var playerImage = $("<img/>").attr("src", PL_PIC_SRC + pNum + ".png").attr("alt", getPlayerId(pNum));
        var picDiv = $("<div/>").addClass(PL_PIC_CLASS).attr("id", getElementId(pNum, PL_PIC_CLASS));
        playerImage.appendTo(picDiv);

        var playerFlag = $("<img/>").attr("src", PL_FLAG_SRC + pNum + ".png").attr("alt", getPlayerId(pNum));
        var flagDiv = $("<div/>").addClass(PL_FLAG_CLASS).attr("id", getElementId(pNum, PL_FLAG_CLASS)); ;
        playerFlag.appendTo(flagDiv);

        var scoreDiv = $("<div/>").addClass(PL_SCORE_CLASS).attr("id", getElementId(pNum, PL_SCORE_CLASS)).text("0");
        var nameDiv = $("<div/>").addClass(PL_NAME_CLASS).attr("id", getElementId(pNum, PL_NAME_CLASS)).text(pName);

        var quitButton = $("<button/>").addClass(BTN_QUIT_CLASS).text("Quit");
        var quitDiv = $("<div/>").addClass(PL_QUIT_CLASS);
        if (pNum == myId) {
            quitButton.appendTo(quitDiv);
            quitButton.click(function() { gCtrl.evtRemovePlayer(); });
        }

        ((playerDiv.append(picDiv)).append(flagDiv).append(scoreDiv));
        (playerDiv.append(nameDiv)).append(quitDiv);

        playerDiv.appendTo($("#" + getRealId(gKey, PL_CLASS)));
    }

    this.incScore = function(pNum) { setScore(pNum, this.getScore(pNum) * 1 + 1); }

    this.getScore = function(pNum) { return $("#" + getElementId(pNum, PL_SCORE_CLASS) ).text(); }

    var setScore = function(pNum, val) { $("#" + getElementId(pNum, PL_SCORE_CLASS) ).text(val); }

    this.getName = function(pNum) { return $("#" + getElementId(pNum, PL_NAME_CLASS) ).text(); }

    this.activatePlayer = function(idPlayer) {
        var pCount = parseInt(gCtrl.getPlayerCount());
        idPlayer   = parseInt(idPlayer);
        for (var i = 0; i < pCount; i++) {
            $("#" + getElementId(( i + 1 ), PL_PIC_CLASS) ).toggleClass(PL_INACTIVE, i != idPlayer).toggleClass(PL_ACTIVE, i == idPlayer);
            $("#" + getElementId(( i + 1 ), PL_FLAG_CLASS)).toggleClass(PL_INACTIVE, i != idPlayer).toggleClass(PL_ACTIVE, i == idPlayer);
            $("#" + getElementId(( i + 1 ), PL_NAME_CLASS)).toggleClass(PL_INACTIVE, i != idPlayer).toggleClass(PL_ACTIVE, i == idPlayer);
            $("#" + getElementId(( i + 1 ), PL_SCORE_CLASS)).toggleClass(PL_INACTIVE, i != idPlayer).toggleClass(PL_ACTIVE, i == idPlayer);
        }
    }

    this.removePlayer = function(pNum) {
        $(getPlayerKey(pNum)).css("display", "none");
    }
}