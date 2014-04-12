
$(document).ready(function() {
    var $loginForm = $("#login-form");
    var $wrongBlock = $('#wrong');
    var $spin = $('#spin');
    var spinner = new Spinner().spin();
    $spin.append(spinner.el);

    $('#login-button').click(function() {
        $wrongBlock.hide(100);

        $.post("/login", $loginForm.serialize())
            .done(function(result) {
                console.log(result);
                $spin.show(100);
                poll();
            })
            .fail(function() {
                alert("Unable to reach server");
                $spin.hide(100);
            });

        return false;
    });

    var poll = function() {
        $.get("/poll")
            .done(function(result) {
                console.log(result);

                switch(result) {
                    case "wait":
                        setTimeout(poll, 100);
                        break;
                    case "ok":
                        $spin.hide(100);
                        window.location.href = "/timer";
                        break;
                    case "wrong":
                        $spin.hide(100);
                        $wrongBlock.show(100);
                        break;
                    case "error":
                        $spin.hide(100);
                        alert("Unexpected error");
                        break;
                }
            })
            .fail(function() {
                setTimeout(poll, 100);
            });
    }
});