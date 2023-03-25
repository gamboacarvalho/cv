function HttpRequest(handlerClass, handlerUrl, gName, playerId) {

    var xhr;
    var _args = arguments;

    this.Request = function() {
        xhr = new XMLHttpRequest();

        var data = "";
        
        for (var i = 4; i < _args.length; i += 2) {
            data += "&" + _args[i] + "=" + escape(_args[i + 1]);
        }
        var gChannel = "/" + handlerClass + "/" + handlerUrl
                                                + "?gName=" + escape(gName)
                                                + "&playerId=" + escape(playerId)
                                                + data;
        xhr.open("GET", gChannel, false);
        xhr.send();
        if (xhr.status != 200) throw (xhr.responseText);
    }

    this.isTrue = function() {
        return xhr.responseText == "True";
    }

    this.isFalse = function() {
        return !this.isTrue();
    }

    this.getJSonObject = function() {
        var jSon = xhr.responseText;
        if (jSon.length > 0)
            jSon = "(" + jSon + ")";
        return eval(jSon);
    }

    this.getResponseText = function() { return xhr.responseText; }
}