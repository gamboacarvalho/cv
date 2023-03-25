function chairClicked(offset, session) {
    var chair = $("span#" + offset);

    if (chair.hasClass("selected")) {
        $.post(
            'Cart/RemoveSeat',
            { session: session, seat: offset },
            function (data, textStatus, XMLHttpRequest) {
                if (data.MustUpdate) {
                    var item = $("li#session" + session);
                    if (data.RemoveNode) {
                        item.remove();
                    }
                    else {
                        item.text(data.NodeText);
                    }
                    chair.removeClass("selected");
                }
            },
            'json'
        );
    }
    else {
        $.post(
            'Cart/AddSeat',
            { session: session, seat: offset },
            function (data, textStatus, XMLHttpRequest) {
                if (data.MustUpdate) {
                    var item;
                    if (data.AddNode) {
                        item = $("<li id='session" + session + "'/>");
                        item.appendTo("ul#basketList");
                    }
                    else {
                        item = $("li#session" + session);
                    }
                    item.text(data.NodeText);
                    chair.addClass("selected");
                }
            },
            'json'
        );
    }
}

function cleanSessionAndRoom() {
    $("div#session").empty();
    $("div#room").empty();
}

function cleanRoom() {
    $("div#room").empty();
}

RATE = 0;

function OnClick(rate, showId) {
    RATE = rate;

    $.post(
        'Shows/RateMovie',
        { showId: showId, rating: rate },
        function (data, textStatus, XMLHttpRequest) {
            //            $("span#userRating").replaceWith(data);
            var respDom = $("span#userRatingResponse");
            respDom.html(data);
        },
        'text/html'
    );
}

function OnMouseOver(rate) {
    if (document.images) {
        if (RATE != 0)
            TurnOffStars(RATE);

        TurnOnStars(rate);
    }
}

function OnMouseOut(rate) {
    if (document.images) {
        TurnOffStars(rate);

        if (RATE != 0)
            TurnOnStars(RATE);
    }
}

function TurnOffStars(count) {
    for (var i = 0; i < count; ++i) {
        var tmp = eval("starOff.src");
        document["star" + i].src = tmp;
    }
}

function TurnOnStars(count) {
    for (var i = 0; i < count; ++i) {
        var tmp = eval("starOn.src");
        document["star" + i].src = tmp;
    }
}

function FormAjaxRequest(selectId, url, domIdToUpdate) {
    var value = $("select#" + selectId).val();
    if (value < 0)
        return;

    var image = $("img#loadImg" + selectId);
    image.show();
    $.get(
        url,
        { id: value },
        function (data, textStatus, XMLHttpRequest) {
            var div = $("div#" + domIdToUpdate);
            div.empty();
            div.html(data);
            image.hide();
        },
        'text/html'
    );
}

function getCommentsInterface(id) {
    $.get(
        'Shows/GetCommentsInterface',
        { showId: id },
        function (data, textStatus, XMLHttpRequest) {
            $("div#comments").html(data);
        },
        'text/html'
    );
}