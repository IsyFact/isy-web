/**
 * Initializes selectlists and adds a refresh-handler.
 * Already initialized selectlists are marked with a token-class ("selectlist_ajaxtoken")
 * and will be skipped on subsequent calls.
 */
export function initSelectlists() {
    $("select.selectlist").filter(":not(.selectlist_ajaxtoken)")
        .addClass("selectlist_ajaxtoken")
        .selectlist({
            size: 10
        }).each(function() {
            // hack to make it work with modal dialogs
            // the problem is that while initializing the dialog is not shown yet
            // so all sizes are 0.
            // the hack: to wait until visible and to refresh then
            const $list = $(this);
            let timerId;

            function refreshVisibleSelectlist() {
                if ($list.next().is(':visible')) {
                    window.clearInterval(timerId);
                    $list.selectlist('refresh');
                }
            }

            timerId = window.setInterval(refreshVisibleSelectlist, 200);
    });
}


