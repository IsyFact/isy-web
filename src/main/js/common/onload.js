import { createDatepicker } from '../widgets/datepicker/datepicker';
import { createDatatable } from '../widgets/datatable/datatable';
import { refreshDatatableFilterRow } from '../widgets/datatable/datatable-utils';
import {
    initClickableArea,
    activateJSSorting,
    setSelectAllCheckboxState,
    showHideDetail
} from '../widgets/datatable/datatable-functionalities';
import { createTabGroup } from './tabs';
import { lazyLoad } from './common-utils';
import { registerListpickerHandlers, initialisierenListpickerServlet } from '../widgets/listpicker/listpicker';
import { bindReturnToDefaultButton } from '../widgets/buttons';
import { applyMask, deletePlaceholdersOnReturn } from '../widgets/inputmask';
import { initNavigation } from './tastatursteuerung-navigation';
import { initSelectlists } from '../widgets/selectlist';
import { focusOnload } from './focusOnload';

$(document).ready(function () {
    'use strict';

    // Handler for AJAX requests
    // jsf = JSF JavaScript Library
    if (typeof (jsf) != "undefined") {
        // --------------------------------------------------------
        // Error Handling
        // --------------------------------------------------------
        jsf.ajax
            .addOnError(function (data) {

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

            });

        // --------------------------------------------------------
        // Ajax-Callback
        // --------------------------------------------------------
        jsf.ajax.addOnEvent(function (callback) {

            if (callback.status === 'begin') {
                $(".ajax-status span").text("begin");
            }

            if (callback.status === 'complete') {
                $(".ajax-status span").text("complete");
            }

            if (callback.status === 'success') {
                refreshFunctions();
                initialisierenListpickerServlet();
            }

        });
    }

    // --------------------------------------------------------
    // Magnific Popup initialization
    // --------------------------------------------------------
    $(document).ready(function () {
        $('.image-link').magnificPopup({type: 'image'});
    });

    // --------------------------------------------------------
    // Lazy Loading
    // --------------------------------------------------------
    $(document).scrolled(0, function () {
        lazyLoad();
    });
    $(window).resize(function () {
        const tag = "resizeTimer";
        const self = $(this);
        let timer = self.data(tag);
        if (timer) {
            clearTimeout(timer);
        }
        timer = setTimeout(function () {
            self.removeData(tag);
            lazyLoad();
        }, 500);
        self.data(tag, timer);
    });


    // --------------------------------------------------------
    // initialize functions
    // --------------------------------------------------------
    refreshFunctions();

});

/**
 * Refreshes existing JS-listeners.
 * Every function has to ensure on ajax requests that the listeners are registered only once.
 * If the listeners already exists, they must not be registered again.
 */
function refreshFunctions() {
    'use strict';

    lazyLoad();
    initNavigation();
    initialisierenListpickerServlet();
    initSelectlists();

    // refresh selectpickers
    $('.selectpicker').selectpicker('refresh');

    // --------------------------------------------------------
    // activate multipart forms if needed
    // --------------------------------------------------------
    if ($("[id$='multipartFormEnabled']").val() === 'true') {
        $("form").attr("enctype", "multipart/form-data");
    }


    // --------------------------------------------------------
    // Panels
    // --------------------------------------------------------
    const $panels = $(".panel-collapse[id$='PanelCollapse']").filter(':not(.panel_ajaxtoken)');
    $panels.on('hidden.bs.collapse', function (e) {
        const $panel = $(this).parents('.panel').first();

        // set value of hidden input element
        const $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('false');
        e.stopPropagation();
    });
    $panels.on('shown.bs.collapse', function (e) {
        const $panel = $(this).parents('.panel').first();

        // set value of hidden input element
        const $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('true');
        e.stopPropagation();
    });
    $panels.addClass('panel_ajaxtoken');

    // --------------------------------------------------------
    // Modal dialogs
    // --------------------------------------------------------
    // show modal dialog, if any exist
    const $modalDialogs = $('#modal-add-personal').filter(':not(.modal_ajaxtoken)');
    $modalDialogs.modal('show');
    $modalDialogs.addClass('modal_ajaxtoken');

    // brandest: DSD-1467 modal dialog backgrounds become darker when triggering actions
    // If actions are executed in a modal dialog, which cause the form to reload via AJAX,
    // the modal dialog is rerendered as well.
    // This causes another "modal-backdrop" to be added, which overlaps with the previous one.
    // The prior removal ensures that at most one backdrop exists.
    const $modalVisible = $('.modal-dialog').is(':visible');
    $($(".modal-backdrop").get().reverse()).each(function (index, element) {
        // Removal if a .modal-backdrop exists, even though there is no modal dialog (happens in edge-cases).
        // also:  "get().reverse()" removes the older .modal-backdrops,
        // keep the newest (index=0) as only the newest is linked to the button-event
        if (!$modalVisible || index > 0) {
            $(element).remove();
        }
    });

    // --------------------------------------------------------
    // FocusOnload
    // Focus the element on the upper left of the content area on load.
    // The element to be focussed can be overwritten or deactivated using the isy:focusOnload tag.
    // Additionally the focusOnloadForce id can be used to force a specific focus element.
    // --------------------------------------------------------
    focusOnload();

    // --------------------------------------------------------
    // Activate functionalities for a datatable
    // --------------------------------------------------------
    const $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) expand clickable area of header columns
    $rfDataTables.find('th.sortable').click(function (event) {
        const $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    $rfDataTables.each(initClickableArea);
    // (3) register show-/hide-detail logic
    $("table.CLIENT.rf-data-table").each(showHideDetail);
    // (4) activate JS Sorting
    $('.rf-data-table').each(activateJSSorting);
    // (5) always set the correct state to the "select all" checkbox
    $rfDataTables.each(setSelectAllCheckboxState);


    $rfDataTables.addClass('rf-data-table_ajaxtoken');

    // --------------------------------------------------------
    // Popovers and Tooltips
    // --------------------------------------------------------
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

    // --------------------------------------------------------
    // Maginific Popup (Plugin for image-popups)
    // --------------------------------------------------------
    $('.rf-image-popup').filter(':not(.rf-imagepopup_ajaxtoken)').magnificPopup({
        type: 'image'
    });
    $('.rf-image-popup').filter(':not(.rf-imagepopup_ajaxtoken)').addClass('rf-imagepopup_ajaxtoken');


    // --------------------------------------------------------
    // Datepicker
    // --------------------------------------------------------

    const $datepickers = $('.rf-datepicker').filter(':not(.rf-datepicker_ajaxtoken)');

    $datepickers.each(createDatepicker);

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });


    // --------------------------------------------------------
    // Input Masks
    // --------------------------------------------------------
    // all input elements that have the attribut "inputmask"
    const $inputMasks = $('input[data-isymask-mask][data-isymask-mask!=""]').filter(':not(.isyfact-inputmask_ajaxtoken)');
    $inputMasks.each(applyMask);
    $inputMasks.bind('keydown keypress', deletePlaceholdersOnReturn);

    $inputMasks.addClass('isyfact-inputmask_ajaxtoken');


    // --------------------------------------------------------
    // Listpicker
    // --------------------------------------------------------
    /**
     * The function ensures that the key in a listpicker field is resolved.
     * The value is extracted from the underlying table using the given column index and added to the listpicker-field.
     * @param listpickerfield The Listpickerfield.
     * @param indexSpalteSchluesselWert index of the header column whose contents should be extracted
     */
    const $listpickerContainer = $(".listpicker-container").filter(':not(.rf-listpicker_ajaxtoken)');
    $listpickerContainer.each(registerListpickerHandlers);
    $listpickerContainer.addClass('rf-listpicker_ajaxtoken');

    // close all listpickers, if a selectpicker is opened
    const $buttonSelectpicker = $('button.selectpicker');
    $buttonSelectpicker.click(function (event) {
        $listpickerContainer.removeClass('open');
    });

    // --------------------------------------------------------
    // Tabs
    // --------------------------------------------------------
    // control preloaded tabs
    $('.isy-tab').each(createTabGroup);

    // --------------------------------------------------------
    // Button Inject POST
    // --------------------------------------------------------
    const $buttonInjectPostGroups = $("[id$='buttonInjectPostGroup']");
    $buttonInjectPostGroups.each(function () {
        const $group = $(this);

        // find clickable element in the ButtonInjectPostGroup
        const $actualButton = $group.find(":nth-child(4)");

        // find button for POST-action
        const $postButtonId = $group.find("[id$='postButton']").val();
        let $postButton = $("[id$='" + $postButtonId + "']");
        //jsf ids might have other suffixes delimited with ':', e.g. ':ajax_button'
        //prefer no-suffix version, but if none is found, look for suffixed buttons as well
        if ($postButton.length === 0){
            $postButton = $("[id*='" + $postButtonId + ":']");
        }

        // find field for posted
        const $posted = $group.find("[id$='posted']");

        // fiend field for continue
        const $continue = $group.find("[id$='continue']");

        if ($posted.attr("value") === 'true') {
            // the POST action was successfully completed. Reset the flag.
            $posted.attr("value", "false");

            // only click if continue-flag was set
            if ($continue.attr("value") === 'true') {
                $actualButton.trigger("onclickStandby");
            }
        }

        // bind events, if they haven't already
        if (!$group.hasClass("isyfact-buttonInjectPostGroup_ajaxtoken")) {
            // <a>-tag: remove onclick
            $actualButton.on("onclickStandby", $actualButton.prop("onclick"));
            $actualButton.prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht

            // <input type=submit ...> prevent transmission
            // overwrite button action
            $actualButton.on("click.postInject", function (event) {
                event.preventDefault();
                $posted.attr("value", "true");
                $postButton.click();
            });
            $group.addClass('isyfact-buttonInjectPostGroup_ajaxtoken');
        }

    });

    // --------------------------------------------------------
    // Default Buttons
    // --------------------------------------------------------
    const $forms = $("form");
    $forms.each(bindReturnToDefaultButton);

    // --------------------------------------------------------
    // Browse and Collect
    // --------------------------------------------------------
    $("select.browsecollect").filter(":not(.browsecollect_ajaxtoken)")
        .addClass("browsecollect_ajaxtoken")
        .browsecollect({
            size: 10
        }).each(function () {
        // hack to make it work with modal dialogs
        // the problem is that while initializing the dialog is not shown yet
        // so all sizes are 0.
        // the hack: to wait until visible and to refresh then
        const $bc = $(this);
        let timerId;

        function checkForVisibility() {
            if ($bc.next().is(':visible')) {
                window.clearInterval(timerId);
                $bc.browsecollect('refresh');
            }
        }

        timerId = window.setInterval(checkForVisibility, 200);
    });

    // --------------------------------------------------------
    // Datatable Client
    // --------------------------------------------------------
    $("table.CLIENT.rf-data-table:not('datatable-client-init')")
        .addClass('datatable-client-init') // mark as initialized
        .each(createDatatable);
    // --------------------------------------------------------
    // Datatable Filter
    // --------------------------------------------------------
    refreshDatatableFilterRow();

    // --------------------------------------------------------
    // Toggle Filter
    // --------------------------------------------------------
    $("div.toggle-filter:not('.toggle-filter-ajax')")
        .addClass('toggle-filter-ajax') // mark as already initialized
        .removeClass('hidden')
        .each(function () {
            const $this = $(this);
            const $sel = $this.find('select');
            $sel.find('option').each(function () {
                if (!this.value) return; // überspringe platzhalter
                $this.append('<button type="button" class="btn btn-default' + (this.selected ? ' active' : '') + (this.disabled ? ' disabled' : '') + '" value="' + this.value + '">' + this.text + '</button>');
            });
        }).on('click', 'button', function () {
        if ($(this).hasClass('active') || $(this).hasClass('disabled')) return;
        const $sel = $(this).parent().find('select');
        $sel.val(this.value);
        $(this).parent().find('input[type=submit]').click();
    });
}



