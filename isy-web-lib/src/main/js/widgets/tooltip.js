/**
 * Initializes and enables tooltpips.
 */
export function enableTooltips() {
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').popover({animation: false});
    // Popovers, that would overflow to the right, will be moved further to the left
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').on('shown.bs.popover', function (e) {
        const $popover = $(e.target).next();
        if ($(document).width() < ($popover.offset().left + $popover.width() + 30)) {
            const $popoverInitiator = e.target;

            const differenceAbsRelPos = $popoverInitiator.getBoundingClientRect().left - $popoverInitiator.offsetLeft;
            let positionLeft = $popover.offset().left - differenceAbsRelPos;
            const differencePopoverInitiatorPos =
                $popover.offset().left+differenceAbsRelPos - $popoverInitiator.getBoundingClientRect().left;

            // If popover too far right, move back towards its initiator
            if (differencePopoverInitiatorPos > -($popover.width()/2)){
                positionLeft -= differencePopoverInitiatorPos - ($popover.width()/2) - 48;
            }

            // If popover clips beyond right border of window, adjust to the left
            if ((positionLeft + $popover.width() + differenceAbsRelPos) > $(document).width()){
                positionLeft -= (positionLeft + $popover.width() + differenceAbsRelPos + 23) - $(document).width();
            }

            $popover.css("left", positionLeft + "px");
        }
    });
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').addClass('rf-popover_ajaxtoken');

    $('.rf-tooltip').filter(':not(.rf-tooltip_ajaxtoken)').popover();
    $('.rf-tooltip').filter(':not(.rf-tooltip_ajaxtoken)').addClass('rf-tooltip_ajaxtoken');
}
