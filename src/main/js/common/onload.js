import { initDatepickers } from "../widgets/datepicker/datepicker";
import { initDatatablesClientmode, initDatatables } from "../widgets/datatable/datatable";
import { refreshDatatableFilterRow } from "../widgets/datatable/datatable-filterrow";
import { createTabGroup } from "../widgets/tabs";
import { enableMultipartFormIfNeeded, lazyLoad } from "./common-utils";
import { initialisierenListpickerServlet, initListpickers } from '../widgets/listpicker';
import { bindReturnToDefaultButton } from "../widgets/buttons";
import { initInputMasks } from "../widgets/inputmask";
import { initNavigation } from "./tastatursteuerung-navigation";
import { initSelectlists } from "../widgets/selectlist";
import { focusOnload } from "./focusOnload";
import { initToggleFilters } from "../widgets/togglefilter";
import { enableTooltips } from "../widgets/tooltip";
import { initPanels } from "../widgets/panels";
import { initModalDialogs } from "../widgets/modaldialog";
import { initSelectpickers } from "../widgets/selectpicker";
import { initImagePopups } from "../widgets/imagepopup";
import { initBrowseCollect } from "../widgets/browsecollect";

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
    // Initialize all custom Handlers
    initBrowseCollect();
    initNavigation();
    initSelectlists();
    initPanels();
    initialisierenListpickerServlet();
    initToggleFilters();
    initModalDialogs();
    initDatepickers();
    initSelectpickers();
    initImagePopups();
    initListpickers();
    initInputMasks();

    //data tables
    initDatatables();
    initDatatablesClientmode();
    refreshDatatableFilterRow();

    focusOnload();
    enableTooltips();
    enableMultipartFormIfNeeded();

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
        const $postButton = $("[id$='" + $group.find("[id$='postButton']").val() + "']");

        // find field for posted
        const $posted = $group.find("[id$='posted']");

        // fiend field for continue
        const $continue = $group.find("[id$='continue']");


        if ($posted.attr("value") === 'true') {
            // the POST action was successfully completed. Reset the flag.
            $posted.attr("value", "false");

            $actualButton.unbind("click.postInject");
            // If <a>-Tag: return onclick
            $actualButton.attr("onclick", $actualButton.attr("onclickStandby"));

            // Only trigger the click, if continue-flag was set
            if ($continue.attr("value") === 'true') {
                $actualButton.click();
            }
        }

        // bind events, if they haven't already
        if (!$group.hasClass(".isyfact-buttonInjectPostGroup_ajaxtoken")) {
            // <a>-tag: remove onclick
            $actualButton.attr("onclickStandby", $actualButton.attr("onclick"));
            $actualButton.prop("onclick", null); // IE11 does not support .removeAttr() for "onclick"

            // <input type=submit ...> prevent transmission
            // overwrite button action
            $actualButton.bind("click.postInject", function (event) {
                event.preventDefault();
                $posted.attr("value", "true");
                $postButton.click();
            });

            $group.addClass('isyfact-buttonInjectPostGroup_ajaxtoken');
        }

    });

    // Bind enter to Defaultbutton
    $("form").each(bindReturnToDefaultButton);
}
