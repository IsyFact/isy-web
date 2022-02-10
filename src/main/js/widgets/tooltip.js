/**
 * Initializes and enables tooltips.
 */
export function enableTooltips () {
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').popover({animation: false});
    // Popovers, that would overflow to the right, will be moved further to the left
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').on('shown.bs.popover', function (e) {
        const $popover = $(e.target).next();
        if ($(document).width() < ($popover.offset().left + $popover.width() + 30)) {
            const positionLeft = -($(document).width() - $popover.offset().left) + 20;
            $popover.css("left", positionLeft + "px");
        }
    });
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').addClass('rf-popover_ajaxtoken');

    $('.rf-tooltip').filter(':not(.rf-tooltip_ajaxtoken)').popover();
    $('.rf-tooltip').filter(':not(.rf-tooltip_ajaxtoken)').addClass('rf-tooltip_ajaxtoken');
}