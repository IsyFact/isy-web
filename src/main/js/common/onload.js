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
import {
    registerListpickerHandlers,
    initialisierenListpickerServlet} from '../widgets/listpicker/listpicker';

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
    initialisierenListpickerServlet();

});

/**
 * Refreshes existing JS-listeners.
 * Every function has to ensure on ajax requests that the listeners are registered only once.
 * If the listeners already exists, they must not be registered again.
 */
function refreshFunctions() {
    'use strict';

    lazyLoad();

    // --------------------------------------------------------
    // refresh selectpickers (so bootstrap-select renders them correctly)
    // --------------------------------------------------------
    $('.selectpicker').selectpicker('refresh');

    // --------------------------------------------------------
    // activate multipart forms if needed
    // --------------------------------------------------------
    if ($("[id$='multipartFormEnabled']").val() === 'true') {
        $("form").attr("enctype", "multipart/form-data");
    }

    // --------------------------------------------------------
    // navigation
    // --------------------------------------------------------
    $('#main-nav').mainNavigation();

    // keyboard control for navigation
    $(document).keydown(function (e) {
        if (e.altKey && !e.shiftKey && !e.ctrlKey && e.keyCode == 77) {
            $('.menustart').addClass('open');
            $(".search-option:first").focus();
        }
    });

    $(".search-option").keydown(function (e) {


        // current column
        const $divCol = $(this).parents(".col-lg-4").first();

        // current row
        const $divRow = $divCol.parents(".row").first();

        // next elements
        let $liMenu;
        let $liMenuNext;
        let $divRowNext;
        let divColNext;

        // remember current column index
        let $divColNeighbour = $divCol;
        let colIndex = 1;
        while ($divColNeighbour.prev().length !== 0) {
            $divColNeighbour = $divColNeighbour.prev();
            colIndex = colIndex + 1;
        }

        if (e.which == 37) {

            // left arrow
            // jump one column to the left or to the previous menu heading
            if (colIndex != 1) {
                // another menu item exists
                $($divRow.children().get(colIndex - 2)).find(".search-option").focus();
            } else {
                // jump to the menu in the left, if it exists
                $liMenu = $divRow.parents("li").first();
                $liMenuNext = $liMenu.prev();

                if ($liMenu.length >= 1) {
                    // another menu exists
                    $liMenu.removeClass("open").removeClass("active");
                    $liMenuNext.addClass("open").addClass("active");
                    $liMenuNext.find(".search-option:first").focus();
                }

            }

            // prevent scrolling
            return false;
        }

        if (e.which == 39) {

            // right arrow
            // jump one column to the right or to the next menu heading
            if ($divRow.children().length >= colIndex + 1) {
                // another menu item exists
                $($divRow.children().get(colIndex)).find(".search-option").focus();
            } else {
                // jump to the menu on the next page, if there is one
                $liMenu = $divRow.parents("li").first();
                $liMenuNext = $liMenu.next();

                if ($liMenu.length >= 1) {
                    // if there is another menu
                    $liMenu.removeClass("open").removeClass("active");
                    $liMenuNext.addClass("open").addClass("active");
                    $liMenuNext.find(".search-option:first").focus();
                }

            }

            // prevent scrolling
            return false;
        }

        if (e.which == 40) {

            // downwards arrow
            // jump to the next row on the same col index
            $divRowNext = $(this).parents(".row").first().next();
            if ($divRowNext.length > 0) {

                if ($divRowNext.children().length >= colIndex) {
                    // there are enough children to select
                    divColNext = $divRowNext.children().get(colIndex - 1);
                } else {
                    // select last element
                    divColNext = $divRowNext.children().last();
                }

                $(divColNext).find(".search-option").focus();
            }

            // prevent scrolling
            return false;
        }
        if (e.which == 38) {

            // upwards arrow
            // jump to the previous row on the same col index
            $divRowNext = $(this).parents(".row").first().prev();

            if ($divRowNext.length > 0) {

                if ($divRowNext.children().length >= colIndex) {
                    // there are enough children to select
                    divColNext = $divRowNext.children().get(colIndex - 1);
                } else {
                    // select last element
                    divColNext = $divRowNext.children().last();
                }

                $(divColNext).find(".search-option").focus();
            }

            // prevent scrolling
            return false;
        }
    });

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

    const $focusOnloadActive = $("[id$='focusOnloadActive']").last();
    let $focusOnloadDeactivated;
    let $focusOnloadForce;
    const focusOnloadElement = "[id$='focusOnloadElement']";

    if ($('#modal-add-personal').val() === undefined) {

        $focusOnloadDeactivated = $('#inhaltsbereichForm').find("[id$='focusOnloadDeactivated']");
        $focusOnloadForce = $('#inhaltsbereichForm').find("[id$='focusOnloadForce']");

        if ($focusOnloadDeactivated.val() != 'true') {
            if ($focusOnloadActive.val() == 'true' || $focusOnloadForce.val() == 'true') {
                $focusOnloadActive.val('false');

                if ($('#inhaltsbereichForm').find(focusOnloadElement).val() === undefined) {
                    // use default value
                    $('#inhaltsbereichForm').find('input:not([type=hidden]), a:not([tabindex=-1]), button').first().focus();
                } else {
                    $('#inhaltsbereichForm').find($('#inhaltsbereichForm').find(focusOnloadElement).val()).first().focus();
                }
            }
        }
    } else {
        $focusOnloadDeactivated = $('#modalDialogPlaceholderForm').find("[id$='focusOnloadDeactivated']");
        $focusOnloadForce = $('#modalDialogPlaceholderForm').find("[id$='focusOnloadForce']");

        if ($focusOnloadDeactivated.val() != 'true') {
            if ($focusOnloadActive.val() == 'true' || $focusOnloadForce.val() == 'true') {
                $focusOnloadActive.val('false');

                if ($('#modalDialogPlaceholderForm').find(focusOnloadElement).val() === undefined) {
                    // use default value
                    $('#modalDialogPlaceholderForm').find('input:not([type=hidden]), a:not([tabindex=-1]), button').first().focus();
                } else {
                    $('#modalDialogPlaceholderForm').find($('#modalDialogPlaceholderForm').find(focusOnloadElement).val()).first().focus();
                }
            }
        }
    }

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
    $inputMasks.each(function () {
        const $inputMask = $(this);
        if ($inputMask.attr('name').indexOf('listpickerField') > -1) {
            if ($inputMask.val().indexOf(" - ") >= 0) {
                //verhindere, dass Ziffern aus dem Wert im Feld verbleiben
                $inputMask.val($inputMask.val().substring(0, $inputMask.val().indexOf(" - ")));
            }
        }

        $inputMask.mask();

        // max length is set via mask, otherwise ctrl+v wouldn't work
        $inputMask.removeAttr('maxlength');
    });

    $inputMasks.bind('keydown keypress', function (e) {
        const $inputMask = $(this);

        if (e.key === 'Enter') {
            // delete all placeholder characters
            const existentVal = $inputMask.val();
            const newVal = existentVal.replace(/_/g, '');
            $inputMask.val(newVal);
        }
    });

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
    $forms.each(function () {
        const $form = $(this);
        $form.unbind("keypress");
        const $defaultButton = $form.find("[id*='" + $form.find("[id$='defaultButtonID']").val() + "']");
        if ($defaultButton.length > 0) {
            // remove original bind
            $form.unbind("keypress");
            // the form contains a default button
            $form.bind("keypress", function (event) {
                if (event.keyCode == 13) {
                    const $source = $(document.activeElement);
                    // No link, button, charpicker or other submit-element is focused,
                    // that has their own action on enter
                    if (!$source.is("[type='submit']") &&
                        !$source.is("a") &&
                        !$source.is("button") &&
                        !$source.hasClass("charpicker") &&
                        !$defaultButton.first().is("[disabled]")) {
                        // trigger click on default button
                        $defaultButton.first().click();
                        // prevent original default-action,
                        // which would be clicking the first button found having type="submit"
                        event.preventDefault();
                    }
                }
            });
        }
    });

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
    // Selectlist
    // --------------------------------------------------------
    $("select.selectlist").filter(":not(.selectlist_ajaxtoken)")
        .addClass("selectlist_ajaxtoken")
        .selectlist({
            size: 10
        }).each(function () {
        // hack to make it work with modal dialogs
        // the problem is that while initializing the dialog is not shown yet
        // so all sizes are 0.
        // the hack: to wait until visible and to refresh then
        const $list = $(this);
        let timerId;

        function checkForVisibility() {
            if ($list.next().is(':visible')) {
                window.clearInterval(timerId);
                $list.selectlist('refresh');
            }
        }

        timerId = window.setInterval(checkForVisibility, 200);
    });

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

/**
 * Blocks a single button on click. Prevents doubleclicks.
 */
function blockSingleButton(data) {
    'use strict';
    if (data.source.type != "submit") {
        return;
    }

    switch (data.status) {
        case "begin":
            data.source.disabled = true;
            break;
        case "complete":
            data.source.disabled = false;
            break;
    }
}


const scriptLoadedOnload = function () {
    'use strict';

    return true;
};

/**
 * Formats currency input to use the correct amount of decimal places.
 *
 * parseFloat is used, as the component formCurrencyInput already prevents input of letters.
 */
function formatAmountOfMoney(ref) {
    'use strict';
    const inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        const dezimalstellen = $(inputField).data("decimalplaces");
        let result = formatiereInput(ref.value, dezimalstellen);
        result = kuerzeInput(result, ref.maxLength);
        result = setzeTausenderPunkte(result);
        inputField.value = result;
    }
}

/**
 * Formats numeric values and floating point numbers.
 *
 * parseFloat is used, as the component formNumericInput already prevents input of letters
 * in the "onkeyup"-event.
 */
function formatNumericValue(ref) {
    'use strict';
    const inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        const dezimalstellen = $(inputField).data("decimalplaces");
        const tausenderpunktGewuenscht = $(inputField).data("tausenderpunkt");
        let result = formatiereInput(ref.value, dezimalstellen);
        result = kuerzeInput(result, ref.maxLength);
        if (tausenderpunktGewuenscht) {
            result = setzeNumerischeTausenderPunkte(result);
        }
        inputField.value = result;
    }
}

/**
 * formats input value: remove thousands separator and show the given number of decimal places
 * (e.g. xxxxx,xx)
 *
 * Used by formCurrencyInput and formNumericInput components
 */
function formatiereInput(input, dezimalstellen) {
    'use strict';
    let value = input.split(".").join("");
    value = value.replace(',', '.');
    let tmp = parseFloat(value).toFixed(dezimalstellen);
    return tmp.replace('.', ',');
}

/**
 * shortens input to the given length
 */
function kuerzeInput(value, length) {
    'use strict';
    let kommaPosition = value.indexOf(",");
    let anzahlTausenderPunkte = parseInt(((kommaPosition - 1) / 3));
    while (value.length > (length - anzahlTausenderPunkte)) {
        value = value.substring(0, kommaPosition - 1) + value.substring(kommaPosition);
        kommaPosition = value.indexOf(",");
        anzahlTausenderPunkte = parseInt(((kommaPosition - 1) / 3));
    }
    return value;
}

/**
 * format currency value input to use dot as thousands separator ( e.g. xx.xxx,xx )
 */
function setzeTausenderPunkte(value) {
    'use strict';
    const kommaPosition = value.indexOf(",");
    for (let i = 1; i < kommaPosition; i++) {
        if (i % 3 === 0) {
            value = value.substring(0, kommaPosition - i) + "." + value.substring(kommaPosition - i);
        }
    }
    return value;
}

/**
 * Format input of numeric values or floating point number to use dot as thousands separator
 * (e.g. xx.xxx or xx.xxx,xx)
 */
function setzeNumerischeTausenderPunkte(value) {
    'use strict';
    let kommaPosition = value.indexOf(",");
    if (kommaPosition === -1) {
        kommaPosition = value.length;
    }
    for (let i = 1; i < kommaPosition; i++) {
        if (i % 3 === 0) {
            value = value.substring(0, kommaPosition - i) + "." + value.substring(kommaPosition - i);
        }
    }
    return value;
}

/**
 * Delete all characters apart from numbers and comma in an input field.
 * Is used by formCurrencyInput and formNumericInput Components as a onkeyup-event.
 *
 * @param ref -
 *            reference to the input field whose contents are replaced
 */
function deleteNonDigitCharacters(ref) {
    'use strict';
    if (ref.value !== "") {
        // saves the current cursor position as a variable
        // needed for browser compatibility with IE and Chrome
        let start = ref.selectionStart;
        let end = ref.selectionEnd;

        // length of text is saved:
        // is used later on to determine how many characters were removed
        // to calculate the shift in position of the cursor
        const length = ref.value.length;

        // removes all characters but numbers and comma as a decimal separator
        // Attention: dots as thousands separators are also removed
        ref.value = ref.value.replace(/[^\d,.]/g, '');

        // checks whether characters were removed and shifts the cursor
        // accordingly - is needed for IE and Chrome; for FF replacing the text is sufficient
        const lengthAfterReplace = ref.value.length;
        if (length > lengthAfterReplace) {
            start = start - (length - lengthAfterReplace);
            end = end - (length - lengthAfterReplace);
        }

        // sets cursor position
        ref.setSelectionRange(start, end);
    }
}

/**
 * JavaScript type conversion of String = "true" to Boolean = true
 */
function stringToBoolean(str) {
    "use strict";
    return ((str == "true") ? true : false);
}


