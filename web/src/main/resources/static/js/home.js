const MESSAGE_TYPE_SUCCESS = "success";
const MESSAGE_TYPE_ERROR   = "error";
const MESSAGE_TYPE_WARNING = "warning";

$(document).ready(function () {
    $("#cancel-url-button").click(function () {
        $("#completed-task").addClass("hidden");
    });
    $("#copy-url-button").click(function () {
        copyToClipboard($("#short-url").text());
        let button = $(this);
        button.attr("disabled", "disabled");
        button.text("Copied!");
        setTimeout(function () {
            button.removeAttr("disabled");
            button.text("Copy URL");
        }, 2000);
    });
    $("#close-notification-button").click(function () {
        let notification = $("#notification-bar");
        notification.stop(true, true);
        notification.addClass("hidden");
    });
});

function createURL() {
    if (grecaptcha.getResponse().length === 0) {
        notify("Please complete the captcha", MESSAGE_TYPE_WARNING);
        return;
    }
    $.post("/", {
        "url": document.getElementById("form").url.value,
        "g-recaptcha-response": grecaptcha.getResponse()
    }).done(function (data) {
        try {
            $("#completed-task").removeClass("hidden").get(0).scrollIntoView();
            $("#short-url").prop("href", window.location.href + data.url).text(window.location.href + data.url);
        } finally {
            grecaptcha.reset();
        }
    }).fail(function (_) {
        grecaptcha.reset();
        notify("Failed to create URL, please try again.", MESSAGE_TYPE_ERROR);
    });
}

function notify(message, type) {
    let notification = $("#notification-bar");
    notification.stop(true, true);
    switch (type) {
        case "success":
            notification.removeClass(["hidden", "bg-red-500", "bg-yellow-500"]);
            notification.addClass("bg-green-500");
            notification.get(0).scrollIntoView();
            break;

        case "warning":
            notification.removeClass(["hidden", "bg-red-500", "bg-green-500"]);
            notification.addClass("bg-yellow-500");
            notification.get(0).scrollIntoView();
            break;

        case "error":
            notification.removeClass(["hidden", "bg-green-500", "bg-yellow-500"]);
            notification.addClass("bg-red-500");
            notification.get(0).scrollIntoView();
            break;

        default:
            console.error("Invalid notification type: " + type);
            return;
    }
    $("#notification-bar-text").text(message);
    notification.slideDown(300, function() {
        notification.delay(2000);
        notification.slideUp(300, function() {
            notification.addClass("hidden");
        });
    });
}

function copyToClipboard(text) {
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(function() {
            console.log("Copied to clipboard: " + text)
        });
    } else {
        console.warn("navigator.clipboard not supported");
        return prompt("Copy to clipboard: Ctrl+C", text);
    }
}