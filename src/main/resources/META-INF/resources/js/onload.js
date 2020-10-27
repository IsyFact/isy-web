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
 * Aktualisiert existierende JS-Listener. Jede Funktion muss sicherstellen, das
 * bei einem AJAX-Request die spezifischen Listener nur einmal registriert
 * werden. Falls Listener bereits existieren, dürfen diese nicht erneut
 * registriert werden.
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
    // Helper function for datatables ( expand clickable area )
    // --------------------------------------------------------
    const $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) expand clickable area of header columns
    $rfDataTables.find('th.sortable').click(function (event) {
        const $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    // function to highlight a selected row
    const formatRowsFunction = function ($trs, $tr, selectionMode) {
        // moossenm: added row-selection class to differentiate row selection checkboxes from others
        const $input = $tr.find("td div.row-selection .checkbox label input");
        if ($input.is(":checked")) {
            $tr.addClass("active");
            // if selection mode is "single" other input checkboxes have to be disabled
            if (selectionMode === "single") {
                const $prevs = $tr.prevAll().find("td div.row-selection .checkbox label input:checked");
                const $nexts = $tr.nextAll().find("td div.row-selection .checkbox label input:checked");
                $prevs.attr("checked", false);
                $nexts.attr("checked", false);
                $prevs.each(function () {
                    $(this).parents("tr").first().removeClass("active");
                });
                $nexts.each(function () {
                    $(this).parents("tr").first().removeClass("active");
                });
            }
        } else {
            $tr.removeClass("active");
        }
    };
    // (2) expand clickable area for selection of rows / set double click / initialize selection mode
    $rfDataTables.each(function () {
        const $rfDataTable = $(this);
        const $rfDataTableSelectOption = $(this).find("[id$='rfDataTableSelectableOption']").first();
        const selectionMode = $(this).find("[id$='rfDataTableSelectionMode']").first().val();
        const selectActive = ($rfDataTableSelectOption.val() === 'true');
        const $rfDataTableDoubleClickActive = $(this).parent().find("[id$='rfDataTableDoubleClickActive']").first();
        const doubleClickActive = ($rfDataTableDoubleClickActive.text() === 'true');

        const $rows = $(this).find("tbody tr");
        $rows.each(function () {
            const $row = $(this);
            let clicks = 0;
            let timer = null;

            let functionSingleClick = null;
            if (selectActive) {
                functionSingleClick = function (a) {

                    if (!$(a.target).is("input") && !$(a.target).is("span") && !$(a.target).is("button")) {
                        const $input = $row.find("td div.row-selection .checkbox label input");
                        $input.click();
                    }
                    formatRowsFunction($rows, $row, selectionMode);
                };
            }

            let functionDoubleClick = null;
            if (doubleClickActive) {
                functionDoubleClick = function (e) {
                    if (!$(e.target).is("input") && !$(e.target).is("span")) {

                        // set the selection
                        $rfDataTable.find("[id$='rfDataTableDoubleClickSelectedRow']").val($row.attr('id'));

                        // click hidden button
                        $rfDataTable.parent().find("[id$='rfDataTableDoubleClickButton']").click();
                    }
                };
            }

            multiClick($row, functionSingleClick, functionDoubleClick, 200, clicks, timer);

        });

        // register click event on checkboxes
        $rows.find("td .checkbox label input").click(function () {
            formatRowsFunction($rows, $(this).parents("tr").first(), selectionMode);
        });

        // highlight initially selected rows
        if (selectActive) {
            $rows.each(function () {
                formatRowsFunction($rows, $(this), selectionMode);
            });
        }
    });
    // (3) register "select all" checkbox
    const selectAllFunction = function ($selectAllCheckbox, $rfDataTable) {
        //firstly, remove indeterminate state
        $selectAllCheckbox.prop("indeterminate", false);

        if ($selectAllCheckbox.is(":checked")) {
            // Transition to unchecked
            $rfDataTable.find("tbody").first().find(".checkbox input").prop("checked", false);
        } else {
            // Transition to checked
            $rfDataTable.find("tbody").first().find(".checkbox input").prop("checked", true);
        }
        // select rows if needed
        const selectionMode = $rfDataTable.find("[id$='rfDataTableSelectionMode']").first().val();
        const $rows = $rfDataTable.find("tbody tr");
        $rows.each(function () {
            // moossenm: DSD-509 - 16.06.2015
            // add missing parameters rows and selectionMode; now the selected rows are highlighted
            formatRowsFunction($rows, $(this), selectionMode);
        });
    };
    // (4) register show-/hide-detail logic
    const showDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        const $this = $(this);
        const $tr = $this.parents('tr');
        const $table = $this.parents("table.CLIENT.rf-data-table");
        const allowMultiple = $table.find("input[id$='rfDataTableDetailMode']").val() == 'multiple';
        if (!allowMultiple) {
            // hide all detail rows
            $table.find("tr[id*='detail-']").addClass('hidden');
            // change eventhandler, tooltip, id for hideDetail-Buttons
            const $hideDetailButtons = $table.find('div.detailview-actions button[id*=hideDetail]');
            $hideDetailButtons.find('span').removeClass('icon-minus').addClass('icon-plus');
            $hideDetailButtons.attr('title', $this.parents('div.detailview-actions').data('show-tooltip'));
            $hideDetailButtons.attr('id', $this.attr('id').replace("hideDetail", "showDetail"));
            $hideDetailButtons.off('click.hidedetail');
            $hideDetailButtons.on('click.showdetail', showDetail);
        }
        $tr.next().removeClass('hidden');
        $this.attr('title', $this.parents('div.detailview-actions').data('hide-tooltip'));
        $this.attr('id', $this.attr('id').replace("showDetail", "hideDetail"));
        $this.find('span').removeClass('icon-plus').addClass('icon-minus');
        // change eventhandler
        $this.off('click.showdetail');
        $this.on('click.hidedetail', hideDetail);
        // Lazy-Loading
        setTimeout(function () {
            // lazyloaded shouldn't be triggered directly
            // images of the detail-view are only visible AFTER click-function "visible"
            lazyLoad();
        }, 50);
    };
    const hideDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        const $this = $(this);
        const $tr = $this.parents('tr');
        $tr.next().addClass('hidden');
        $this.attr('title', $this.parents('div.detailview-actions').data('show-tooltip'));
        $this.attr('id', $this.attr('id').replace("hideDetail", "showDetail"));
        $this.find('span').removeClass('icon-minus').addClass('icon-plus');
        // Eventhandler wechseln
        $this.off('click.hidedetail');
        $this.on('click.showdetail', showDetail);
    };
    $("table.CLIENT.rf-data-table").each(function () {
        const $table = $(this);
        // =============== START DETAILVIEW ===================== //
        $table.find('tbody div.detailview-actions button').prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
        const $showDetail = $table.find('div.detailview-actions button[id*=showDetail]');
        $showDetail.on('click.showdetail', showDetail);
        const $hideDetail = $table.find('div.detailview-actions button[id*=hideDetail]');
        $hideDetail.on('click.hidedetail', hideDetail);
        // =============== END DETAILVIEW ===================== //
    });
    // (5) activate JS Sorting
    $('.rf-data-table').each(function () {
        const $rfDataTable = $(this);
        const $sortFunction = $rfDataTable.find("[id$='rfDataTableJsSortFunction']");

        if ($sortFunction.length > 0) {

            const $sortAttribute = $rfDataTable.find("[id$='rfDataTableSortProperty']");
            const $sortDirection = $rfDataTable.find("[id$='rfDataTableSortDirection']");
            const $jsSortedList = $rfDataTable.find("[id$='rfDataTableJsSortedList']");

            const $ths = $(this).find("th");
            $ths.each(function (index) {
                const $th = $(this);

                if ($th.hasClass("sortable")) {

                    const $thLink = $th.find("a");
                    $thLink.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"
                    $thLink.unbind("click");
                    $thLink.click(function (event) {
                        event.preventDefault();

                        // save details before sorting
                        const $details = $rfDataTable.find("tbody .details-preview");
                        $details.remove();

                        // determine new sort attribute and direction
                        $rfDataTable.find("thead th.sorted").removeClass("sorted");
                        let newSortDirection = "";

                        if ($sortDirection.val() == "ASCENDING") {
                            $th.removeClass("sort-up");
                            $th.addClass("sort-down");
                        }
                        if ($sortDirection.val() == "DESCENDING") {
                            $th.removeClass("sort-down");
                            $th.addClass("sort-up");
                        }

                        if ($th.hasClass("sort-up")) {
                            newSortDirection = "DESCENDING";
                            $th.removeClass("sort-up");
                            $th.addClass("sort-down");
                        } else {
                            newSortDirection = "ASCENDING";
                            $th.removeClass("sort-down");
                            $th.addClass("sort-up");
                        }
                        $th.addClass("sorted");
                        $sortDirection.val(newSortDirection);
                        $sortAttribute.val($th.attr("data-sortattribute"));


                        // sort
                        window[$sortFunction.val()]($rfDataTable, $th, index, newSortDirection);

                        // save sorting
                        const $trsNeu = $rfDataTable.find("tbody tr");
                        let sortedList = "";
                        $trsNeu.each(function () {
                            const id = $(this).attr("id");
                            if (sortedList.length > 0) {
                                sortedList = sortedList + ",";
                            }
                            sortedList = sortedList + id;
                        });
                        $jsSortedList.val(sortedList);

                        // reassign details after sorting
                        $details.each(function () {
                            const $detail = $(this);
                            const idDetail = $detail.attr("id");
                            const $afterTr = $rfDataTable.find("tbody tr[id='" + idDetail + "']");
                            $detail.insertAfter($afterTr);
                        });

                    });
                }
            });

        }

    });

    //(6) always set the correct state to the "select all" checkbox
    const tristateBerechnen = function ($checkboxes, $selectAllCheckbox, $rfDataTable) {
        $selectAllCheckbox.prop("indeterminate", false);

        let alleAusgewaehlt = true;
        let keineAusgewahlt = true;
        $checkboxes.each(function () {
            if ($(this).is(":checked")) {
                keineAusgewahlt = false;
            } else {
                alleAusgewaehlt = false;
            }
        });

        if (keineAusgewahlt) {
            $selectAllCheckbox.prop("checked", false);
        } else if (alleAusgewaehlt) {
            $selectAllCheckbox.prop("checked", true);
        } else {
            $selectAllCheckbox.prop("indeterminate", true);
            $selectAllCheckbox.prop("checked", false);
        }
    };


    $rfDataTables.each(function () {
        const $selectAllCheckbox = $(this).find("[id*='dataTableSelectAll']").first();
        const $rfDataTable = $(this);

        //register click on the tri-state-checkbox
        $selectAllCheckbox.parent().find("span").click(function () {
            selectAllFunction($selectAllCheckbox, $rfDataTable);
        });

        //register click on other checkboxes
        const $checkboxes = $rfDataTable.find("tbody").first().find(".checkbox input");
        $checkboxes.each(function () {
            $(this).click(function () {
                tristateBerechnen($checkboxes, $selectAllCheckbox, $rfDataTable);
            });
        });

        //calculate state initially. Otherwise the state might be lost on a request to the server
        tristateBerechnen($checkboxes, $selectAllCheckbox, $rfDataTable);

    });


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

    const $datepickers = $('.rf-datepicker').filter(':not(.rf-datepicker_ajaxtoken)').filter(':not(.rf-datepicker_readonly)');
    $datepickers.each(function () {
        $(this).datepicker({
            format: $(this).attr('dateformat'),
            weekStart: 1,
            todayBtn: "linked",
            language: $(this).attr('language'),
            autoclose: true,
            todayHighlight: true,
            // showOnFocus: If false, the datepicker will be prevented from showing when the input field associated with it receives focus.
            showOnFocus: false,
            // enableOnReadonly: If false the datepicker will not show on a readonly datepicker field.
            enableOnReadonly: false
        });

        $(this).children("a").click(
            function () {// open a datepicker
                const dateReg = /^\d{2}[.]\d{2}[.]\d{4}$/;
                const inputField = $(this).prev();
                let date = inputField.val().split('.');

                // delete underscore placeholders
                const placeholderReg = /\D/gi;
                date[0] = date[0].replace(placeholderReg, "");
                date[1] = date[1].replace(placeholderReg, "");
                date[2] = date[2].replace(placeholderReg, "");
                let dateString;
                if (date[0] === "99") {
                    // Secret-Code: 99 = set focus of the datepicker to current date
                    dateString = currentDateAsString();
                } else if (date[0] === "00" || date[1] === "00") {
                    dateString = setValidDateAsString(date);
                } else {
                    // copy the manuell entered date into the datepicker
                    // invalid date inputs will be fixed
                    dateString = fixDateOutOfRange(date);
                }
                $(this).parent().datepicker('setDate', dateString);
                $(this).parent().datepicker('update');
            });

        //read the limit for expanding two-digit years. Is used later on.
        const zweistelligeJahreszahlenErgaenzenGrenze = $('#formDateJahresZahlenErgaenzenGrenze').val();
        const $datumInputFeld = $(this).find('input');
        $datumInputFeld.focusout(function (event) {
            if (zweistelligeJahreszahlenErgaenzenGrenze !== "-1") {
                datumErgaenzen($datumInputFeld, zweistelligeJahreszahlenErgaenzenGrenze);
            }

            // Magic Number: set date to today
            const date = $datumInputFeld.val().split('.');
            if (date[0] === "99") {
                $datumInputFeld.val(currentDateAsString());
            } else {
                // wrong dates will be fixed
                $datumInputFeld.val(fixDateOutOfRange(date));
            }
        });
    });

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });

    function fixDateOutOfRange(date) {
        let year = date[2],
            month = date[1],
            day = date[0];
        // Assume not leap year by default (note zero index for Jan)
        const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

        // If evenly divisible by 4 and not evenly divisible by 100,
        // or is evenly divisible by 400, then a leap year
        if ((year % 4 === 0) && (year % 100 !== 0) || (year % 400 === 0)) {
            daysInMonth[1] = 29;
        }

        // Check if month is out of range and fix
        if (month > 12) {
            month = 12;
        }

        // Get max days for month and fix days if out of range
        const maxDays = daysInMonth[month - 1];
        if (day > maxDays) {
            day = maxDays;
        }

        return day + '.' + month + '.' + year;
    }

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
            // verhindere, dass Ziffern aus dem Wert im Feld verbleiben
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


        // intecept clicks and close picker if needed
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
            const valid = inputChangingKeycode(keyCode); // ändert der Tastendruck den Inhalt des Filters?
            if (valid) {
                $(this).change();
            }
        });

        // react to input into the input field (especially on AJAX-Widgets)
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
    $('.isy-tab').each(function () {
        const $isyTab = $(this);

        // TabAutoscroll: Is "tabHochScrollen" in a tabGroub active?
        let $tabHochScrollen = false;
        if ($isyTab.hasClass('tabHochScrollen')) {
            $tabHochScrollen = true;
        }

        $isyTab.children().each(function () {
            const $li = $(this);
            const $liLink = $(this).find("a");

            if ($li.hasClass('skipAction')) {
                $liLink.unbind("click");
                $liLink.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"
            }

            $liLink.click(function (event) {
                if ($li.hasClass('skipAction')) {
                    event.preventDefault();
                }

                // remove current tab
                const liIdAlt = $isyTab.find(".active").attr('id');
                $isyTab.find(".active").removeClass("active");
                $isyTab.next().find("#" + liIdAlt).removeClass("active");

                // activate tab
                const liIdNeu = $li.attr('id');
                $li.addClass("active");
                //$isyTab.next().find("#" + liIdNeu).addClass("active");

                const aktiverTab = $isyTab.next().find("#" + liIdNeu);
                aktiverTab.addClass("active");

                // support tab-autoscroll
                if ($tabHochScrollen) {
                    $('html, body').animate({
                        scrollTop: $(aktiverTab).offset().top - 50
                    }, 'slow');
                }

                // save state
                $isyTab.next().find("[id$='isyTabCurrentActiveTab']").first().val(liIdNeu.replace("tabId", ""));
                lazyLoad();
            });
        });
    });

    // --------------------------------------------------------
    // Button Inject POST
    // --------------------------------------------------------
    const $buttonInjectPostGroups = $("[id$='buttonInjectPostGroup']");
    $buttonInjectPostGroups.each(function () {
        const $group = $(this);

        // find clickable element int the ButtonInjectPostGroup
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
            $actualButton.prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht

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
        .each(function () {
            const $table = $(this);

            // =============== START FILTER-ROW ===================== //
            let timeId = 0;
            // replace buttons so that no server action is called
            const $filterRow = $table.find("thead tr.filter-row");
            $filterRow
                .find("button.btn:not(.selectpicker)")
                .replaceWith("<button type='button' class='btn hidden' />");

            // clear all filters, fix after replaceWith
            $filterRow.find("td.table-clear-all-filter button")
                .removeClass("hidden")
                .addClass("table-clear-all-filter icon btn-icon btn-icon-small icon-cancel");

            // event-trigger for filters
            $filterRow
                .on("click", "button.btn:not('.selectpicker,.table-clear-all-filter')", function (e) {
                    // use 100ms puffer to prevent events from queueing up too much
                    if (timeId) {
                        clearTimeout(timeId);
                    }
                    timeId = setTimeout(function () {
                        doItAll();
                    }, 100);
                });

            // reset events for all filters
            $filterRow.find("td.table-clear-all-filter button").click(function (e) {
                const $this = $(this);
                $filterRow.find('select.filter-dropdown').selectpicker('val', '');
                $filterRow.find('select.filter-dropdown').data('property', '');
                $filterRow.find('input.table-filter').val('');
                $filterRow.find('input.table-filter').data('property', '');
                $filterRow.find('a.table-clear-filter').hide();
                $this.hide();
                doItAll();
            });

            //list of items with details, is used by multiple functions
            let $itemsWithDetails = $table.find("tbody tr");
            const $allFilters = $filterRow.find('td');
            const filterSingle = function ($td) {
                // filter value
                let filter = '';
                if ($td.find('input.table-filter').length) {
                    filter = $td.find('input.table-filter').val();
                } else if ($td.find('select.filter-dropdown').length) {
                    filter = $td.find('select.filter-dropdown').val();
                } else {
                    // no filter column
                    return;
                }
                filter = filter.trim().toLowerCase();
                if (filter === undefined || filter === '') {
                    // no filter
                    return;
                }
                // column that should be filtered
                const filterTd = 'td:nth-child(' + ($td.index() + 1) + ')';
                let lastMatched = false; // helpervariable for showing details
                $.each($itemsWithDetails, function (i, item) {
                    const $item = $(this);
                    if (!$item.is(":visible")) {
                        // already filtered out
                        lastMatched = false;
                        return;
                    }
                    if ($item.hasClass('details-preview')) {
                        // detail
                        if (!lastMatched) {
                            $item.hide().addClass('filtered');
                        }
                        return;
                    }
                    // determine value
                    const $td = $item.find(filterTd);
                    let val = $td.data('filter');
                    if (val === undefined || val === '') {
                        val = $td.text();
                    }
                    val = val.trim().toLowerCase();
                    // filter
                    if (~val.indexOf(filter)) {
                        lastMatched = true;
                        return;
                    }
                    lastMatched = false;
                    $item.hide().addClass('filtered');
                });
            };
            const filterAll = function (init) {
                $itemsWithDetails.show().removeClass('filtered');
                $.each($allFilters, function (i, td) {
                    filterSingle($(td));
                });
                if (!init && !cumulative) {
                    setCurrentPage(1);
                }
            };
            // initialization
            $table.removeClass('datatable-filterrow-init');
            refreshDatatableFilterRow(); //
            // =============== END FILTER-ROW ===================== //

            // =============== START PAGINATION ===================== //
            const $pageControl = $table.find("tfoot tr").eq(0).find("td").eq(0);
            const getCurrentPage = function () {
                return $pageControl.data("currentpage") || 1;
            };
            const setCurrentPage = function (pageNumber) {
                $pageControl.data("currentpage", pageNumber);
                $table.find("input[type=hidden][id$=rfDataTableCurrentPage]").val(pageNumber);
            };
            const getPageSize = function () {
                return $pageControl.data("pagesize") || getItemCount();
            };
            const getItemCount = function () {
                return $table.find("tbody tr:not('.details-preview'):not('.filtered')").length;
            };
            const getPageCount = function () {
                const pageSize = getPageSize();
                return Math.floor((getItemCount() + pageSize - 1) / pageSize);
            };
            const isFirstPage = function () {
                return (getCurrentPage() === 1);
            };
            const isLastPage = function () {
                return (getCurrentPage() == getPageCount());
            };
            const getPageFrom = function () {
                return Math.max(getCurrentPage() - getPaginatorSize() + 1, 1);
            };
            const getPageTo = function () {
                return Math.min(getCurrentPage() + getPaginatorSize() - 1, getPageCount());
            };
            const getPaginatorSize = function () {
                return $pageControl.data("paginatorsize");
            };
            const rePageNumber = new RegExp('\\{0\\}', 'gi');
            const rePageCount = new RegExp('\\{1\\}', 'gi');
            const setupLi = function ($li, pageNumber, disabled) {
                setupButton($li.find('button'), pageNumber, disabled);
                $li.data('page', pageNumber);
            };
            const setupButton = function ($btn, pageNumber, disabled) {
                const text = $btn.parents('li').data('text');
                $btn.text(text.replace(rePageNumber, pageNumber).replace(rePageCount, getPageCount()));
                const tooltip = $btn.parents('li').data('tooltip');
                $btn.attr('title', tooltip.replace(rePageNumber, pageNumber).replace(rePageCount, getPageCount()));
                $btn.prop('disabled', disabled);
            };
            const doPagination = function () {
                const text = $pageControl.find('li.pagination-text').data('text');
                $pageControl.find('li.pagination-text').text(text.replace(rePageNumber, getCurrentPage()).replace(rePageCount, getPageCount()));

                setupLi($pageControl.find('li.page-first'), 1, isFirstPage());
                setupLi($pageControl.find('li.page-pre'), isFirstPage() ? 1 : getCurrentPage() - 1, isFirstPage());

                $pageControl.find("li.page-number.generated").remove();
                const $next = $pageControl.find('li.page-next');
                let pageNum = getPageFrom();
                for (pageNum = pageNum; pageNum <= getPageTo(); pageNum++) {
                    const $page = $pageControl.find("li.page-number.master").clone().removeClass("master hidden").addClass("generated");
                    setupLi($page, pageNum, false);
                    if (pageNum == getCurrentPage()) {
                        $page.addClass('active');
                    }
                    $page.insertBefore($next);
                }
                setupLi($pageControl.find('li.page-next'), isLastPage() ? getPageCount() : getCurrentPage() + 1, isLastPage());
                setupLi($pageControl.find('li.page-last'), getPageCount(), isLastPage());
                $pageControl.find('ul.pagination').show();
            };
            const cumulative = $pageControl.hasClass('SIMPLE');
            const renderPage = function () {
                if ($pageControl.hasClass('NORMAL')) {
                    doPagination();
                }
                const currentPage = getCurrentPage();
                const pageSize = getPageSize();
                // find all table entries with details-preview and hide them
                $itemsWithDetails.hide();
                const itemCount = getItemCount();
                // eingrenzen
                const itemFrom = cumulative ? 0 : (currentPage - 1) * pageSize;
                const itemTo = Math.min(currentPage * pageSize, itemCount);
                const isLastPage = (itemCount == itemTo);
                const items = $itemsWithDetails.filter(":not(.details-preview):not(.filtered)");
                const itemFromIndex = items.eq(itemFrom).index();
                let pageItems;
                if (isLastPage) {
                    pageItems = $itemsWithDetails.slice(itemFromIndex);
                } else {
                    const lastItemToShow = items.eq(itemTo - 1);
                    let itemToIndex = lastItemToShow.index();
                    if (lastItemToShow.next().hasClass('details-preview')) {
                        itemToIndex++;
                    }
                    pageItems = $itemsWithDetails.slice(itemFromIndex, itemToIndex + 1);
                }
                // fix body widths wenn using pagination with scrolling. do you really have to?
                if ($table.hasClass("tablescroll_body")) {
                    const header = $table.parent().prev().find("thead tr:first-of-type th");
                    const body = pageItems.filter(':not(.filtered)').eq(0).find("td");
                    for (let i = 0; i < header.length; i++) {
                        body.eq(i).attr("style", header.eq(i).attr("style"));
                    }
                }
                // show current page
                pageItems.filter(':not(.filtered)').show();
                // returns true if the last page is being shown
                return isLastPage;
            };
            // ...show more... variant
            $pageControl.filter('.SIMPLE').find('li.page-next').click(function (e) {
                setCurrentPage(getCurrentPage() + 1);
                if (renderPage()) {
                    $(this).hide();
                } else {
                    setupLi($(this), getCurrentPage() + 1, false);
                }
                // prevent execution of default action
                e.stopPropagation();
                e.preventDefault();
                return false;
            }).each(function () {
                // replace buttons
                $pageControl
                    .find('li.page-next')
                    .find('button')
                    .replaceWith("<button type='button' class='btn' />");

                // initialization
                setCurrentPage(getCurrentPage() - 1);
                $(this).click();
            });
            // normal paginator variant
            $pageControl.filter('.NORMAL').each(function () {
                // install EventHandler
                $pageControl.find('ul.pagination').on('click', 'li', function (e, skipDisabledTest) {
                    if (!skipDisabledTest && $(this).find('button').prop('disabled')) {
                        return false;
                    }
                    const page = $(this).data('page');
                    setCurrentPage(page);
                    doPagination();
                    renderPage();
                });
                // initialize page button master
                $pageControl.find('li.page-number:not(.master)').remove();

                // replace buttons
                $pageControl
                    .find('li.page-first,li.page-pre,li.page-next,li.page-last')
                    .find('button')
                    .replaceWith("<button type='button' class='btn' />");
            });

            // =============== END PAGINATION ===================== //

            // =============== START SORTING ===================== //
            $table.find("thead th.sortable").each(function () {
                const $th = $(this);
                $th.find('a').each(function () {
                    const $a = $(this);
                    $a.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"-events
                    $a.unbind("click");
                });
            });
            const getSortProperty = function () {
                return $table.find("input[type=hidden][id$=rfDataTableSortProperty]").val();
            };
            const setSortProperty = function (sortProperty) {
                return $table.find("input[type=hidden][id$=rfDataTableSortProperty]").val(sortProperty);
            };
            const getSortDirection = function () {
                return $table.find("input[type=hidden][id$=rfDataTableSortDirection]").val();
            };
            const setSortDirection = function (sortDirection) {
                return $table.find("input[type=hidden][id$=rfDataTableSortDirection]").val(sortDirection);
            };
            const sortValue = function (tr, index) {
                const filter = 'td:nth-child(' + index + ')';
                const $td = $(tr).find(filter);
                let val = $td.data('sort');
                if (val === undefined || val === '') {
                    val = $td.text().trim();
                }
                return val;
            };
            const sort = function ($th) {
                const thisSortProperty = $th.data("sortattribute");
                const index = $th.index() + 1;
                const items = $itemsWithDetails.filter(":not(.details-preview)");
                const isAsc = $th.hasClass('sort-up');
                const comp = function (v1, v2) {
                    if (v1 > v2) {
                        return isAsc ? 1 : -1;
                    }
                    if (v1 < v2) {
                        return isAsc ? -1 : 1;
                    }
                    return 0;
                };
                items.sort(function (tr1, tr2) {
                    const v1 = sortValue(tr1, index);
                    const v2 = sortValue(tr2, index);
                    if (+v1 === +v1 && +v2 === +v2) {
                        // compare as numbers
                        return comp(+v1, +v2);
                    }
                    // compare as strings
                    return comp(v1, v2);
                });
                // at this point the table entries are sorted without the details
                // we need to correctly assign the details now
                let newItems = [];
                $.each(items, function (i, item) {
                    const $item = $(item);
                    const index = $item.index(); // index in DOM before sorting
                    newItems.push($item);
                    // check if details are visible
                    const detail = $itemsWithDetails.eq(index).next();
                    if (detail.hasClass('details-preview')) {
                        // preserve detail-row
                        newItems.push(detail);
                    }
                });
                $itemsWithDetails.detach();
                let tbody = $table.find("tbody");
                tbody.append(newItems);
                $itemsWithDetails = $table.find("tbody tr");
            };
            $table.find('thead th.sortable a').bind('click', function (e) {
                e.preventDefault();
                const $th = $(this).parents('th');
                let sortClass = 'sort-up'; // standardmäßig aufsteigend sortiert
                const thisSortProperty = $th.data("sortattribute");
                if (thisSortProperty == getSortProperty()) {
                    // Richtung invertieren
                    if ($th.hasClass('sort-up')) {
                        sortClass = 'sort-down';
                    }
                }
                $th.parents("thead").find("th.sortable").removeClass("sorted sort-up sort-down");
                $th.addClass("sorted").addClass(sortClass);
                setSortProperty(thisSortProperty);
                setSortDirection(sortClass == 'sort-up' ? 'ASCENDING' : 'DESCENDING');
                // if there is an application specific sort function, use that
                if (typeof window.sortDataTable === 'function') {
                    window.sortDataTable($table, $th, $itemsWithDetails);
                    $itemsWithDetails = $table.find("tbody tr");
                } else {
                    sort($th);
                }

                if (!cumulative) {
                    setCurrentPage(1);
                }
                renderPage();
            });
            // =============== END SORTING ===================== //

            // main function
            const doItAll = function (init) {
                filterAll(init);
                // if there is an application specific sort function, use that
                if (typeof window.sortDataTable === 'function') {
                    window.sortDataTable($table, $.merge($table.find('thead th.sorted'), $table.parent().prev().find('thead th.sorted')).eq(0), $itemsWithDetails);
                    $itemsWithDetails = $table.find("tbody tr");
                } else {
                    sort($.merge($table.find('thead th.sorted'), $table.parent().prev().find('thead th.sorted')).eq(0));
                }
                renderPage();
            };
            // initialization
            doItAll(true);
        });

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
        // block ui of the listpicker
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

/**
 * Lazy loads elements if necessary.
 */
function lazyLoad() {
    'use strict';

    // Bilder
    $("[data-src].lazy").each(function () {
        const $lazyImage = $(this);
        if ($lazyImage.visible()) {
            const src = $lazyImage.attr("data-src");
            $lazyImage.attr("src", src);
            $lazyImage.removeAttr("data-src");
        }
    });
}

scriptLoadedOnload = function () {
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

function currentDateAsString() {
    "use strict";
    const currentDate = new Date();
    const dayOfMonth = currentDate.getDate();
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();
    const heute = dayOfMonth.toString() + '.' + month.toString() + '.' + year.toString();
    const heuteMitFuehrendenNullen = heute.replace(/(^|\D)(\d)(?!\d)/g, '$10$2');

    return heuteMitFuehrendenNullen;
}

function setValidDateAsString(date) {
    "use strict";
    date[0] = date[0].replace("00", "01");
    date[1] = date[1].replace("00", "01");
    return date[0] + '.' + date[1] + '.' + date[2];
}

function refreshDatatableFilterRow() {
    "use strict";
    $("table.rf-data-table:not('datatable-filterrow-init')")
        .addClass('datatable-filterrow-init') // mark as initialized
        .each(function () {
            const $table = $(this);
            // validate number of columns as a hint for the developer
            const spaltenImHeader = $table.find('thead tr').eq(0).find('th').length;
            const spaltenImFilter = $table.find('thead tr').eq(1).find('th').length || spaltenImHeader;
            const spaltenImBody = $table.find('tbody tr').eq(0).find('td').length || spaltenImHeader;
            if (spaltenImHeader != spaltenImFilter || spaltenImHeader != spaltenImBody) {
                $table.css({border: '5px solid #FF0000'});
                if (spaltenImHeader != spaltenImFilter) {
                    $table.before("Die Anzahl der Spalten im Header stimmen nicht mit der Anzahl der Spalten in der Filter-Zeile überein.");
                }
                if (spaltenImHeader != spaltenImBody) {
                    $table.before("Die Anzahl der Spalten im Header stimmen nicht mit der Anzahl der Spalten im Body überein.");
                }
            }
            // filter
            const $filterRow = $table.find("thead tr.filter-row");
            const $clearAllFilterIcon = $filterRow.find("td.table-clear-all-filter button");
            const resetFilter = function () {
                // reset all filters
                $filterRow.find('input.table-filter').val('').data('property', '');
                // reset all dropdown filters
                $filterRow.find('select.filter-dropdown').data('property', '').selectpicker('val', '');
                // hides all "clear filter" icons
                $filterRow.find('a.table-clear-filter').hide();
                // hide "clear-all-filters" icon
                $clearAllFilterIcon.hide();
            };
            const hasFilter = function () {
                // at least one filterfield has a value or one dropdown is selected
                return ($filterRow.find('input.table-filter, select.filter-dropdown').filter(function () {
                    return !!this.value;
                }).length);
            };
            const checkClearAllFilter = function () {
                if (hasFilter()) {
                    // show clear-all-filters icon
                    $clearAllFilterIcon.show();
                } else {
                    // hide clear-all-filters icon
                    $clearAllFilterIcon.hide();
                }
            };
            // patch for the clear-all-filters icon
            // is needed because of limitiations of the isy:buttonIcon implementation
            $clearAllFilterIcon
                .removeClass("btn-icon-round btn-icon-round-small") // remove wrong buttonIcon class
                .addClass("table-clear-all-filter btn-icon btn-icon-small") // add right icon class
                // click on clear-all-filter icon:
                // resets all filters, hides all clear-filter icons and the clear-all-filter icon
                // and executes the action
                .click(function () {
                    resetFilter();
                    // execute default action
                    return true;
                })
                .each(function () {
                    /// initialize
                    checkClearAllFilter();
                });
            $filterRow.find('select.filter-dropdown')
                .change(function (e) {
                    const $this = $(this);
                    checkClearAllFilter();
                    // click on hidden button to execute its action
                    $this.parent().next().click();
                });
            // click on clear-filter icon:
            // resets filter, adjusts view of the clear-filter/clear-all-filter icons and executes the action
            $filterRow.find('a.table-clear-filter')
                .click(function () {
                    const $this = $(this);
                    // resets filter
                    $this.prev().val('');
                    // hides clear-filter icon and if necessary clear-all-filter icon
                    $this.prev().trigger('blur');
                    // prevent default action
                    return false;
                });
            // Events of the filter input field
            // generally: execute action if enter was pressed, but only if the filter changed
            $filterRow.find('input.table-filter')
                .attr("autocomplete", "off")
                .keypress(function (event) {
                    const isClient = $table.hasClass('CLIENT');
                    // if enter was pressed
                    if (event.which == 13) {
                        // execute action
                        if (isClient) {
                            $(this).trigger('keyup');
                        } else {
                            $(this).trigger('blur');
                        }
                        // prevent default action
                        return false;
                    }
                    // if backspace was pressed
                    if (event.which == 8 && isClient) {
                        $(this).trigger('keyup');
                    }
                }).keyup(function (e) {
                // only in CLIENT mode
                if ($table.hasClass('CLIENT')) {
                    const $this = $(this);
                    $this.trigger('blur');
                    $this.focus();
                }
            // generally: execute action if the filter loses focus, but only if the filter has changed
            }).blur(function () {
                const $this = $(this);
                $this.trigger('change');
                if ($this.val() != $this.data('property')) {
                    // click on hidden button to execute action
                    $this.data('property', $this.val());
                    $this.next().next().click();
                }
                // adjust view of clear-filter and clear-all-filter icons
            }).change(function () {
                const $this = $(this);
                if ($this.val()) {
                    // if new value is not empty, show clear-filter icon
                    $this.next().show();
                } else {
                    // if new value is empty, hide clear-filter icon
                    $this.next().hide();
                }
                checkClearAllFilter();
            });
        });
}

// This function transforms two digit years into four digit years in date input fields.
datumErgaenzen = function (inputFeld, grenze) {
    "use strict";
    //The given "grenze" as a limit is added to the current year, so the resulting limit year will update over time
    const aktuellesJahr = parseInt(new Date().getFullYear().toString().substring(2, 4));
    grenze = (aktuellesJahr + parseInt(grenze)).toString();
    const aktuelleWerte = inputFeld.val().split('.');
    if (aktuelleWerte.length === 3 && aktuelleWerte[2].replace(/_/g, '').length === 2) {
        let praefix;
        const jahresZahl = aktuelleWerte[2].replace(/_/g, '');
        if (jahresZahl <= grenze) {
            praefix = "20";
        } else if (jahresZahl > grenze) {
            praefix = "19";
        }
        const ergebnis = praefix + jahresZahl;
        aktuelleWerte[2] = ergebnis;
        inputFeld.val(aktuelleWerte[0] + "." + aktuelleWerte[1] + "." + aktuelleWerte[2]);
    }
};

//Code that triggers initialization of a listpicker through the servlet
initialisierenListpickerServlet = function () {
    "use strict";
    const $listpicker = $(".servlet.listpicker-filter");
    $listpicker.each(function (i, listpicker) {
        registerListpickerfilter(listpicker);
    });
};


//register a listpicker
registerListpickerfilter = function (identifier) {
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
createListpickerTable = function (responseText, listfilter) {
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
