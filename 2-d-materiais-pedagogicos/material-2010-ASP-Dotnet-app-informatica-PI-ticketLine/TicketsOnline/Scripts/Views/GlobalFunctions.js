
function removeNode(id) {
    $(id).remove();
}

function error(err) {
    $("p#error").text("Ocorreu um erro no seu pedido, refresque a página se o erro persistir");
}

function removeItemClass(id) {
    $(id).removeClass("item");
}

function popUpLogOnSubmit(form) {
    //    var pass = $("input#Password").val();
    //    var user = $("input#Username").val();

    var pass = $(form["Password"]).val();
    var user = $(form["Username"]).val();

    $.post(
        'Account/LogOn',
        { Password: pass, Username: user },
        function (data, textStatus, XMLHttpRequest) {
            if (data.Sucess) {
                $("span#popUpError").html("LogOn efectuado com sucesso! Repita o seu pedido!");
                $("div#popupForm").remove();
                $("div#login_frame").html("Seja bem vindo <b>" + data.Name + "</b>! [ <a href='/Account/Manage'>Gerir conta</a> ][ <a href='/Account/LogOff'>Log Off</a> ]");
            }
            else {
                $("span#popUpError").html(data.Error);
            }
        },
        'json'
    );
}