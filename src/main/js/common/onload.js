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
    const listpickerLoeseSchluesselAuf = function (listpickerfield, indexSpalteSchluesselWert) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // cut previously resolved value from the input field
            // only keep the key that will be resolved
            listpickerfield.val(listpickerfield.val().substring(0,
                listpickerfield.val().indexOf(" - ")));
        }
        let $id;
        if (listpickerfield.attr('data-isymask-mask') !== undefined) {
            listpickerfield.mask();
            $id = listpickerfield.val();
            listpickerfield.unmask();
            if (listpickerfield.val() == "null" || listpickerfield.val() == "nul") {
                listpickerfield.val("");
            }
        }
        const $parent = listpickerfield.parent();
        const $tr = $parent.find("tr[id='" + $id + "']");
        const $td = $tr.find("td:nth-child(" + indexSpalteSchluesselWert + ")");
        const $value = $td.text();
        if ($value !== '') {
            listpickerfield.val($id + ' - ' + $value);
        } else {
            const $filter = $parent.find("input[id*='listpickerFilter']");
            const $listpickerContent = $parent.find(".listpicker-content");
            const $listpickerLastFilter = $listpickerContent.find("input[id*=lastfilter]");
            if ($parent.find(".listpicker-content").css("display") === 'none') {
                if (listpickerfield.val() !== $listpickerLastFilter.val()) {
                    if ($(".ajax-status span").text() !== 'begin') { // prevents collision with other ajax requests
                        $filter.val(listpickerfield.val());
                        $filter.change();
                    }
                }
            }
        }
    };

    /**
     * The function masks the listpickerfield, if a mask is defined
     * @param listpickerfield The Listpickerfield.
     */
    const listpickerMaskieren = function (listpickerfield) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // prevents that digits from the value remain in the input field
            listpickerfield.val(listpickerfield.val().substring(0,
                listpickerfield.val().indexOf(" - ")));
        }
        if (listpickerfield.attr('data-isymask-mask') !== undefined) {
            listpickerfield.mask();
        }
    };

    const $listpickerContainer = $(".listpicker-container").filter(':not(.rf-listpicker_ajaxtoken)');
    $listpickerContainer.each(function () {

        const $listpicker = $(this);
        const $listpickerField = $listpicker.find("[id$='listpickerField']");
        const $listpickerContent = $listpicker.find(".listpicker-content").first();
        const $listpickerFilter = $listpicker.find("[id$='listpickerFilter']");
        const $listpickerMinWidth = $listpicker.find("[id$='listpickerMinWidth']");
        const listpickerAjaxFormId = $listpicker.find("[id$='listpickerAjaxForm']").val();
        let $listpickerAjaxForm = null;
        //find the hidden input, that contains the column which should be used as a value
        const listpickerSchluesselwertSpalte = $listpicker.find("[id$='inputComplement']").val();
        if (typeof (listpickerAjaxFormId) != "undefined") {
            $listpickerAjaxForm = $("form[id$='" + listpickerAjaxFormId + "']");
        }

        // prevent default close event
        $listpicker.on('hide.bs.dropdown', function (e) {
            e.preventDefault();
        });

        // if it's an AJAX listbox, the correct values have to be loaded
        if ($listpickerAjaxForm !== null) {
            $listpickerContent.find("tbody").replaceWith($listpickerAjaxForm.find("tbody").clone());
        }

        // after the dropdown was opened
        $listpicker.on('shown.bs.dropdown', function (e) {
            // close all charpickers
            const $charpickers = $(".special-char-picker-widget");
            $charpickers.each(function () {
                $(this).hide();
            });

            // if a charpicker is open and new listpickers are opened, the old focus is removed
            const $active_charpickers_field = $(".charpicker-focused");
            $active_charpickers_field.each(function () {
                $(this).focusout();
                $(this).removeClass("charpicker-focused");
            });

            // close all listpickers but the current one
            $listpickerContainer.not($listpicker).removeClass('open');

            // set min size
            if (parseInt($listpickerMinWidth.val()) > $listpickerField.outerWidth()) {
                $listpickerContent.css("min-width", parseInt($listpickerMinWidth.val()) + "px");
            } else {
                $listpickerContent.css("min-width", $listpickerField.outerWidth() + "px");
            }

            // when the listpicker is opened, focus the filter input field
            $listpickerFilter.focus();

            // highlight current selection as active
            if ($listpickerField.val() !== '') {
                let id;
                // if the key of the input field was already resolved, we need to reisolate the key
                if ($listpickerField.val().indexOf(" - ") >= 0) {
                    id = $listpickerField.val().substring(0, $listpickerField.val().indexOf(" - "));
                } else {
                    id = $listpickerField.val();
                }
                $listpickerContent.find("tbody tr[id='" + id + "']").addClass("active");
                $listpickerContent.find("tbody tr").not("[id='" + id + "']").removeClass("active");

                // set cursor in Listpickerwidget to currently active row
                const $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");
                scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow);
            } else {
                // on an empty or deleted $listpickerField delete previously 'active' elements
                $listpickerContent.find("tbody tr").removeClass("active");
            }

        });


        // intercept clicks and close picker if needed
        $(document).click(function (e) {

            const $target = $(e.target);

            if ($listpickerContent.has($target).length <= 0) {
                // click is outside of the dropdown, close picker
                $listpicker.removeClass('open');
            } else {
                // click is on the dropdown, select current row
                if ($target.is("tr") || $target.is("td")) {
                    let $row = null;
                    if ($target.is("td")) {
                        $row = $target.parent();
                    } else {
                        $row = $target;
                    }
                    $listpickerField.val($row.attr('id'));
                    $listpickerField.change();
                    $listpicker.removeClass('open');
                    $listpickerField.focus();
                }
            }

        });

        // register input field
        $listpickerFilter.bind('keypress', function (e) {
            if (!e.ctrlKey && !e.altKey && !e.shiftKey && e.keyCode == 13) {
                // don't propagate up the DOM: this keypress only applies to the filter
                e.stopPropagation();

                // on pressing enter, select the currently active entry
                let $row = $listpickerContent.find("tbody tr:visible.active").first();
                // if there is no active entry, the first entry will be selected
                if ($row.length <= 0) {
                    $row = $listpickerContent.find("tbody tr:visible").first();
                }

                $listpickerField.val($row.attr('id'));
                $listpickerField.focus();
                $listpicker.removeClass('open');
                e.preventDefault();
            }
        });

        // ensure that the change-event is triggered whenever the listpickerfilter is changed
        $listpickerFilter.bind('keyup.ensureChange', function (event) {
            const keyCode = event.keyCode;
            const valid = inputChangingKeycode(keyCode); // does the key press change the filter content?
            if (valid) {
                $(this).change();
            }
        });

        // react to input into the input field (on AJAX-Widgets as well)
        // this is not needed on pickers, that filter via servlet
        if (!$listpickerFilter.parent().hasClass('servlet')) {
            $listpickerFilter
                .bind(
                    'keydown.ajaxFilter keypress.ajaxFilter',
                    function () {

                        setTimeout(
                            function () {

                                // Filter
                                const $rows = $listpickerContent
                                    .find(".rf-listpicker-table tbody tr");

                                const filterFunction = function ($row,
                                                               inverse) {

                                    const $tdsAfterFilter = $row
                                        .find('td')
                                        .filter(
                                            function () {
                                                const $td = $(this);
                                                const text = $td
                                                    .text();
                                                const listpickerVal = $listpickerFilter
                                                    .val();
                                                const compare = text
                                                    .toLowerCase()
                                                    .indexOf(
                                                        listpickerVal
                                                            .toLowerCase());
                                                return compare != -1;
                                            });

                                    if (inverse) {
                                        return $tdsAfterFilter.length > 0;
                                    } else {
                                        return $tdsAfterFilter.length <= 0;
                                    }
                                };

                                const $filteredRows = $rows
                                    .filter(function () {
                                        return filterFunction(
                                            $(this), false);
                                    });
                                const $unfilteredRows = $rows
                                    .filter(function () {
                                        return filterFunction(
                                            $(this), true);
                                    });

                                $filteredRows
                                    .css("display", "none");
                                $unfilteredRows.css("display",
                                    "table-row");
                            }, 0);

                    });
        }

        $listpickerFilter.bind('keydown', function (e) {

            const keyPressed = e.which;

            // currently active visible element
            const $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");

            // downwards arrow key
            if (keyPressed == 40) {
                // if no element is active, select first
                if ($activeVisibleRow.length <= 0 && $listpickerContent.find("tbody tr:visible").first().length > 0) {
                    $listpickerContent.find("tbody tr:visible").first().addClass("active");
                }
                // otherwise next element
                else if ($activeVisibleRow.nextAll('tr:visible:first[id]').length > 0) {
                    $activeVisibleRow.removeClass("active");
                    $activeVisibleRow.nextAll('tr:visible:first[id]').addClass("active");
                    scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow.nextAll('tr:visible:first[id]'));
                }

                // upwards arrow key
            } else if (keyPressed == 38) {
                // if there is a previous element, select it
                if ($activeVisibleRow.length > 0 && $activeVisibleRow.prevAll('tr:visible:first').length > 0) {
                    $activeVisibleRow.removeClass("active");
                    $activeVisibleRow.prevAll('tr:visible:first').addClass("active");
                    scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow.prevAll('tr:visible:first'));
                }
                // ESC
            } else if (keyPressed == 27) {
                $listpicker.removeClass('open');
                $listpickerField.focus();
                // Tab
            } else if (keyPressed == 9) {
                $listpicker.removeClass('open');
                $listpickerField.focus();
            }
        });

        // function that expands the listpicker menu, if
        // "downwards-arrow + alt" (Event "keydown", Key-Code: 40) was pressed
        $listpickerField.bind('keydown', 'alt+down', function (e) {
            // Open the listpicker
            // The listpicker is attached to the same parent as the input field
            $(this).parent().find('.listpicker-button').click();
        });

        // On Focusout the key should be resolved
        // The feature is only active if the lookup-column is greater 1
        if (listpickerSchluesselwertSpalte > 1) {
            $listpickerField.focusout(function () {
                listpickerLoeseSchluesselAuf($listpickerField, listpickerSchluesselwertSpalte);
            });

            //if the field is focussed, we need to mask it.
            //otherwise the resolved value would remain in the input
            //and the user would have to remove it manually
            $listpickerField.focus(function () {
                listpickerMaskieren($listpickerField);
            });

            //We initially resolve the key once.
            // This way the input field stays filled correctly,
            // after e.g. a request to the server.
            listpickerLoeseSchluesselAuf($listpickerField, listpickerSchluesselwertSpalte);
        }
    });

    $listpickerContainer.addClass('rf-listpicker_ajaxtoken');

    // scrolls within an element to a specified div
    function scroll_to(element, div) {
        $(element).animate({
            scrollTop: $(div).parent().scrollTop() + $(div).offset().top - $(div).parent().offset().top
        }, 0);
    }

    // close all listpickers, if a selectpicker is opened
    // TODO: the solution currently isn't generic. This should be improved upon.
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

function listpickerAjaxReload(callback, keyCode) {
    'use strict';

    // the listpicker filter that sends the event
    const $listpickerFilter = $(callback.source).first();
    const $listpickerContent = $listpickerFilter.parents(".listpicker-container").first().find(".listpicker-content");
    const $ajaxSpinner = $listpickerFilter.parent().parent().parent().parent().find('.listpicker-ajax-spinner');


    if (callback.status === 'begin' && $listpickerFilter.is($(document.activeElement))) {
        // ui-block on the listpicker
        $ajaxSpinner.css("position", $listpickerContent.css("position"));
        $ajaxSpinner.css("top", $listpickerContent.css("top"));
        $ajaxSpinner.css("left", $listpickerContent.css("left"));
        $ajaxSpinner.css("height", $listpickerContent.css("height"));
        $ajaxSpinner.css("width", $listpickerContent.css("width"));
        $ajaxSpinner.css("margin-top", $listpickerContent.css("margin-top"));
        $ajaxSpinner.css("display", "block");

        // block keyboard input
        $listpickerFilter.bind("keydown.prevent keypress.prevent", function (event) {
            event.preventDefault();
        });

    }

    if (callback.status === 'complete') {
        // remove ui-block
        $ajaxSpinner.css("display", "none");

        // remove keyboard input block
        $listpickerFilter.unbind("keydown.prevent keypress.prevent");

    }

    if (callback.status === 'success') {

        const listpickerAjaxFormId = $listpickerContent.find("[id$='listpickerAjaxForm']").val();
        const $listpickerAjaxForm = $("form[id$='" + listpickerAjaxFormId + "']");

        $listpickerContent.find("tbody").replaceWith($listpickerAjaxForm.find("tbody").clone());
        const $listpickerLastFilter = $listpickerContent.find("input[id*=lastfilter]");
        $listpickerLastFilter.val($listpickerFilter.val());
        $listpickerFilter.parents(".listpicker-container").find("input[id*='listpickerField']").focusout();
    }
}

/**
 * Checks whether the pressed key is a valid input character to change a text fields contents.
 * Common control characters are considered invalid.
 *
 */
function inputChangingKeycode(keyCode) {
    'use strict';
    // keyCodes, that don't change input. Only valid for IE and Firefox!
    // Common control characters will be ignored.
    // invalid Keycodes:
    // 0 OR 9-13 OR 16-20 OR 27 OR 33-45 OR 91-93 OR 112-123 OR 144 OR 145 OR 181-183
    const invalid = (keyCode > 8 && keyCode < 14) ||
        (keyCode > 15 && keyCode < 21) ||
        keyCode === 0 || keyCode === 27 ||
        (keyCode > 32 && keyCode < 46) ||
        (keyCode > 90 && keyCode < 94) ||
        (keyCode > 111 && keyCode < 124) ||
        keyCode === 144 || keyCode === 145 ||
        (keyCode > 180 && keyCode < 184);
    return !invalid;
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

//Code that triggers initialization of a listpicker through the servlet
const initialisierenListpickerServlet = function () {
    "use strict";
    const $listpicker = $(".servlet.listpicker-filter");
    $listpicker.each(function (i, listpicker) {
        registerListpickerfilter(listpicker);
    });
};


//register a listpicker
const registerListpickerfilter = function (identifier) {
    "use strict";
    const $listpickerFilter = $(identifier);
    const listpickerFilterInput = $listpickerFilter.children()[0];
    const url = $listpickerFilter.siblings("div.rf-listpicker-table-container").find(".servletTable")[0].getAttribute("data-servleturl");

    //Hereafter the url paramaters will be encoded
    //only the paramater value will be encoded, not the parameter name itself
    const urlsplit = url.split("?");

    //the first part of the URL (the part before the parameters) remains unchanged
    let urlEncoded = urlsplit[0] + '?';

    //split the second part
    if (urlsplit.length > 1) {
        const attributeGesetzt = urlsplit[1].split("&");
        attributeGesetzt.forEach(function (attribut) {
            const attributSplit = attribut.split("=");
            urlEncoded = urlEncoded + attributSplit[0] + '=' + encodeURIComponent(attributSplit[1]) + "&";
        });
    }

    //Initialize contents of listpicker
    //This is where the actual request is sent!
    $.get(urlEncoded + "filter=" + encodeURIComponent(listpickerFilterInput.value)).done(function (data) {
        createListpickerTable(data, $listpickerFilter);
    });
    listpickerFilterInput.dataset.oldvalue = listpickerFilterInput.value;

    const $listpickerContent = $listpickerFilter.parent().parent();
    const $listpickerContainer = $listpickerContent.parent();
    const $listpickerField = $listpickerContainer.find('*[id*=listpickerField]');

    //if a filter dropdown menu was focused and another area is clicked, the fields will be updated
    $(listpickerFilterInput).focusout(function () {
        $listpickerFilter.parent().parent().siblings('.form-control').focusout();
    });

    //Filtermethod that updates the list.
    //First the handler will be deactivated, in case it already exists and needs to be updated
    //(This is the case, if e.g. the url was manipulated via JavaScript without the whole page being rerendered)
    //If we don't deactiviate this handler, the servlet-url will remain effectively unchanged and
    //the filter wouldn't work
    $(listpickerFilterInput).off('change keyup', servletListpickerFilterChanged);
    //The needed data (the URL and the filter itself) are set as data-attributes of the event
    $(listpickerFilterInput).on('change keyup', {
        url: urlEncoded,
        listpickerfilter: $listpickerFilter,
        listpickerFilterInput: $(listpickerFilterInput)
    }, servletListpickerFilterChanged);
};

/**
 * The function handels change and keyup events for listpickers that filter via servlet.
 * @param event The change/keyup Event.
 */
function servletListpickerFilterChanged(event) {
    "use strict";
    event.stopImmediatePropagation();
    //fetch the needed values from the data-attributes of the event (were set when called)
    const servletUrl = event.data.url;
    const listpickerFilter = event.data.listpickerfilter;
    const listpickerFilterInput = event.data.listpickerfilter;
    const delay = 500;
    window.setTimeout(function (filter) {
        const input = listpickerFilter.children()[0];
        if (input.dataset.oldvalue == "undefined" || input.value != input.dataset.oldvalue) {
            $.get(servletUrl + "filter=" + encodeURIComponent(input.value)).done(function (data) {
                createListpickerTable(data, listpickerFilter);
            });
            input.dataset.oldvalue = input.value;
        }
    }, delay, listpickerFilterInput);
}

//Creates a ListpickerTable based on the responseText.
const createListpickerTable = function (responseText, listfilter) {
    "use strict";
    const $tablecontainer = $(listfilter).siblings("div.rf-listpicker-table-container");
    let $table = $tablecontainer.find(".servletTable");
    $table.empty();
    const tableJson = JSON.parse(responseText);
    for (let j in tableJson.items) {
        const item = tableJson.items[j];
        let tr = $('<tr>').attr('id', item.id);
        for (let i = 0; i < item.attrs.length; i++) {
            const td = $('<td>').text(item.attrs[i].trim());
            tr.append(td);
        }
        $table.append(tr);
    }
    if (tableJson.weiterFiltern === true) {
        let trWeiterFiltern = $('<tr>');
        const tdWeiterFiltern = $("<td>").text(tableJson.messageItem).attr('colspan', 2);
        trWeiterFiltern.append(tdWeiterFiltern);
        $table.append(trWeiterFiltern);
    }
    $(listfilter).parent().parent().siblings('.form-control').focusout();
};

//On click inside the document, if a listpicker was open, it will be closed
//and additionally the focusout-method will be triggered, to cause the key to be resolved
$(document).click(function (e) {
    "use strict";
    const $target = $(e.target);
    const $listpickerContainer = $('.listpicker-container.open');
    const $listpickerContent = $listpickerContainer.find('.listpicker-content');
    if ($listpickerContent.has($target).length <= 0 && $listpickerContainer.hasClass('open')) {
        $listpickerContainer.removeClass('open');
        $listpickerContent.siblings('.form-control').focusout();
        $target.focus();

    }

});
