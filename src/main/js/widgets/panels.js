// --------------------------------------------------------
// Panels
// --------------------------------------------------------
/**
 * Initializes panels and adds event listeners.
 * Already initialized panels are marked with a token-class ("panel_ajaxtoken")
 * and will be skipped on subsequent calls.
 */
export function initPanels(){
    const $panels = $(".panel-collapse[id$='PanelCollapse']").filter(':not(.panel_ajaxtoken)');

    //event handlers to sync current collapse-state of the panel to the server
    $panels.on('hidden.bs.collapse', {val: 'false'}, setPanelCollapseAttribute);
    $panels.on('shown.bs.collapse', {val: 'true'}, setPanelCollapseAttribute);

    $panels.addClass('panel_ajaxtoken');
}

/**
 * Handler for setting hidden input element of a panel.
 * The hidden input element is used to persist the collapse-state of a panel in a form to the server.
 * @param event
 */
function setPanelCollapseAttribute( event ){
    const val = event.data.val;
    //find panel itself
    const $panel = $(this).parents('.panel').first();
    //set hidden input element
    const $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
    $serverProperty.val(val);
    event.stopPropagation();
}
