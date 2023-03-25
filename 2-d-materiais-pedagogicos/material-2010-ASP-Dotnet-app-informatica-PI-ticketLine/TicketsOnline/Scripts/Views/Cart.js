var LASTPRICETOSUBTRACT = 0;
var LASTPRERESERVESESSIONREMOVED = 0;

function OnRemovePreReserveClick(priceToSubtract, sessionId) {
    LASTPRICETOSUBTRACT = priceToSubtract;
    LASTPRERESERVESESSIONREMOVED = sessionId;
}

function OnRemovePreReserveSucess() {
    FINALPRICE -= LASTPRICETOSUBTRACT;
    $("span#finalPrice").text(FINALPRICE);
    $("li#session" + LASTPRERESERVESESSIONREMOVED).remove();
    removeItemClass("div#preReserve" + LASTPRERESERVESESSIONREMOVED);
}