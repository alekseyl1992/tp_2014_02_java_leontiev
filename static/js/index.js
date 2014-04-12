
$(document).ready(function() {
    var loginForm = $("#login-form");

    $('#login-button').click(function() {
        $.post("/login", loginForm.serialize())
            .done(function(result) {
                console.log(result);
                poll();
            })
            .fail(function() {
                alert("error0");
            });

        return false;
    });

    var poll = function() {
        $.get("/poll")
            .done(function(result) {
                console.log(result);
                if (result == "wait")
                    setTimeout(poll, 100);
                else if (result == "ok")
                    window.location.href = "/timer";
                else
                    alert("error1");
            })
            .fail(function() {
                alert("error2");
            });
    }
});