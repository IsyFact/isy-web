/** Render an ajax error message in "messagesPlaceholder" */
export function renderAjaxErrorMessage( data ){
    // determine error message
    let errorMessage = $("[id$='ajaxErrorMessage']").val();
    const errorMessageTitle = $("[id$='ajaxErrorMessageTitle']").val();

    // write to console
    const error = data.description;
    console.log(error);

    // replace error message
    errorMessage = errorMessage.replace("%%FEHLER%%", error);

    // render as a message
    $("[id$='messagesPlaceholder']").replaceWith(
        "<div role='alert' class='alert alert-danger'>" +
        "<span><span class='icon icon-placeholder'></span>" +
        "<strong>" + errorMessageTitle + "</strong></span>" +
        "<span>" + errorMessage + "</span>" +
        "</div>");
}

/** Track ajax requests in hidden input element "ajax-status" */
export function trackAjaxRequests( callback ){
    if (callback.status === 'begin') {
        $(".ajax-status span").text("begin");
    }

    if (callback.status === 'complete') {
        $(".ajax-status span").text("complete");
    }
}