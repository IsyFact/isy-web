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
                var errorMessage = $("[id$='ajaxErrorMessage']").val();
                var errorMessageTitle = $("[id$='ajaxErrorMessageTitle']").val();

                // write to console
                var error = data.description;
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
        var tag = "resizeTimer";
        var self = $(this);
        var timer = self.data(tag);
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
        var $divCol = $(this).parents(".col-lg-4").first();

        // current row
        var $divRow = $divCol.parents(".row").first();

        // next elements
        var $liMenu;
        var $liMenuNext;
        var $divRowNext;
        var divColNext;

        // remember current column index
        var $divColNeighbour = $divCol;
        var colIndex = 1;
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
    var $panels = $(".panel-collapse[id$='PanelCollapse']").filter(':not(.panel_ajaxtoken)');
    $panels.on('hidden.bs.collapse', function (e) {
        var $panel = $(this).parents('.panel').first();

        // set value of hidden input element
        var $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('false');
        e.stopPropagation();
    });
    $panels.on('shown.bs.collapse', function (e) {
        var $panel = $(this).parents('.panel').first();

        // set value of hidden input element
        var $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('true');
        e.stopPropagation();
    });
    $panels.addClass('panel_ajaxtoken');

    // --------------------------------------------------------
    // Modal dialogs
    // --------------------------------------------------------
    // show modal dialog, if any exist
    var $modalDialogs = $('#modal-add-personal').filter(':not(.modal_ajaxtoken)');
    $modalDialogs.modal('show');
    $modalDialogs.addClass('modal_ajaxtoken');

    // brandest: DSD-1467 modal dialog backgrounds become darker when triggering actions
    // If actions are executed in a modal dialog, which cause the form to reload via AJAX,
    // the modal dialog is rerendered as well.
    // This causes another "modal-backdrop" to be added, which overlaps with the previous one.
    // The prior removal ensures that at most one backdrop exists.
    var $modalVisible = $('.modal-dialog').is(':visible');
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

    var $focusOnloadActive = $("[id$='focusOnloadActive']").last();
    var $focusOnloadDeactivated;
    var $focusOnloadForce;
    var focusOnloadElement = "[id$='focusOnloadElement']";

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
    var $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) expand clickable area of header columns
    $rfDataTables.find('th.sortable').click(function (event) {
        var $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    // function to highlight a selected row
    var formatRowsFunction = function ($trs, $tr, selectionMode) {
        // moossenm: added row-selection class to differentiate row selection checkboxes from others
        var $input = $tr.find("td div.row-selection .checkbox label input");
        if ($input.is(":checked")) {
            $tr.addClass("active");
            // if selection mode is "single" other input checkboxes have to be disabled
            if (selectionMode === "single") {
                var $prevs = $tr.prevAll().find("td div.row-selection .checkbox label input:checked");
                var $nexts = $tr.nextAll().find("td div.row-selection .checkbox label input:checked");
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
        var $rfDataTable = $(this);
        var $rfDataTableSelectOption = $(this).find("[id$='rfDataTableSelectableOption']").first();
        var selectionMode = $(this).find("[id$='rfDataTableSelectionMode']").first().val();
        var selectActive = ($rfDataTableSelectOption.val() === 'true');
        var $rfDataTableDoubleClickActive = $(this).parent().find("[id$='rfDataTableDoubleClickActive']").first();
        var doubleClickActive = ($rfDataTableDoubleClickActive.text() === 'true');

        var $rows = $(this).find("tbody tr");
        $rows.each(function () {
            var $row = $(this);
            var clicks = 0;
            var timer = null;

            var functionSingleClick = null;
            if (selectActive) {
                functionSingleClick = function (a) {

                    if (!$(a.target).is("input") && !$(a.target).is("span") && !$(a.target).is("button")) {
                        var $input = $row.find("td div.row-selection .checkbox label input");
                        $input.click();
                    }
                    formatRowsFunction($rows, $row, selectionMode);
                };
            }

            var functionDoubleClick = null;
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
    var selectAllFunction = function ($selectAllCheckbox, $rfDataTable) {
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
        var selectionMode = $rfDataTable.find("[id$='rfDataTableSelectionMode']").first().val();
        var $rows = $rfDataTable.find("tbody tr");
        $rows.each(function () {
            // moossenm: DSD-509 - 16.06.2015
            // add missing parameters rows and selectionMode; now the selected rows are highlighted
            formatRowsFunction($rows, $(this), selectionMode);
        });
    };
    // (4) register show-/hide-detail logic
    var showDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        var $this = $(this);
        var $tr = $this.parents('tr');
        var $table = $this.parents("table.CLIENT.rf-data-table");
        var allowMultiple = $table.find("input[id$='rfDataTableDetailMode']").val() == 'multiple';
        if (!allowMultiple) {
            // hide all detail rows
            $table.find("tr[id*='detail-']").addClass('hidden');
            // change eventhandler, tooltip, id for hideDetail-Buttons
            var $hideDetailButtons = $table.find('div.detailview-actions button[id*=hideDetail]');
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
    var hideDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        var $this = $(this);
        var $tr = $this.parents('tr');
        $tr.next().addClass('hidden');
        $this.attr('title', $this.parents('div.detailview-actions').data('show-tooltip'));
        $this.attr('id', $this.attr('id').replace("hideDetail", "showDetail"));
        $this.find('span').removeClass('icon-minus').addClass('icon-plus');
        // Eventhandler wechseln
        $this.off('click.hidedetail');
        $this.on('click.showdetail', showDetail);
    };
    $("table.CLIENT.rf-data-table").each(function () {
        var $table = $(this);
        // =============== START DETAILVIEW ===================== //
        $table.find('tbody div.detailview-actions button').prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
        var $showDetail = $table.find('div.detailview-actions button[id*=showDetail]');
        $showDetail.on('click.showdetail', showDetail);
        var $hideDetail = $table.find('div.detailview-actions button[id*=hideDetail]');
        $hideDetail.on('click.hidedetail', hideDetail);
        // =============== END DETAILVIEW ===================== //
    });
    // (5) activate JS Sorting
    $('.rf-data-table').each(function () {
        var $rfDataTable = $(this);
        var $sortFunction = $rfDataTable.find("[id$='rfDataTableJsSortFunction']");

        if ($sortFunction.length > 0) {

            var $sortAttribute = $rfDataTable.find("[id$='rfDataTableSortProperty']");
            var $sortDirection = $rfDataTable.find("[id$='rfDataTableSortDirection']");
            var $jsSortedList = $rfDataTable.find("[id$='rfDataTableJsSortedList']");

            var $ths = $(this).find("th");
            $ths.each(function (index) {
                var $th = $(this);

                if ($th.hasClass("sortable")) {

                    var $thLink = $th.find("a");
                    $thLink.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"
                    $thLink.unbind("click");
                    $thLink.click(function (event) {
                        event.preventDefault();

                        // save details before sorting
                        var $details = $rfDataTable.find("tbody .details-preview");
                        $details.remove();

                        // determine new sort attribute and direction
                        $rfDataTable.find("thead th.sorted").removeClass("sorted");
                        var newSortDirection = "";

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
                        var $trsNeu = $rfDataTable.find("tbody tr");
                        var sortedList = "";
                        $trsNeu.each(function () {
                            var id = $(this).attr("id");
                            if (sortedList.length > 0) {
                                sortedList = sortedList + ",";
                            }
                            sortedList = sortedList + id;
                        });
                        $jsSortedList.val(sortedList);

                        // reassign details after sorting
                        $details.each(function () {
                            var $detail = $(this);
                            var idDetail = $detail.attr("id");
                            var $afterTr = $rfDataTable.find("tbody tr[id='" + idDetail + "']");
                            $detail.insertAfter($afterTr);
                        });

                    });
                }
            });

        }

    });

    //(6) always set the correct state to the "select all" checkbox
    var tristateBerechnen = function ($checkboxes, $selectAllCheckbox, $rfDataTable) {
        $selectAllCheckbox.prop("indeterminate", false);

        var alleAusgewaehlt = true;
        var keineAusgewahlt = true;
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
        var $selectAllCheckbox = $(this).find("[id*='dataTableSelectAll']").first();
        var $rfDataTable = $(this);

        //register click on the tri-state-checkbox
        $selectAllCheckbox.parent().find("span").click(function () {
            selectAllFunction($selectAllCheckbox, $rfDataTable);
        });

        //register click on other checkboxes
        var $checkboxes = $rfDataTable.find("tbody").first().find(".checkbox input");
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
        var $popover = $(e.target).next();
        if ($(document).width() < ($popover.offset().left + $popover.width() + 30)) {
            var positionLeft = -($(document).width() - $popover.offset().left) + 20;
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

    var $datepickers = $('.rf-datepicker').filter(':not(.rf-datepicker_ajaxtoken)');
    $datepickers.each(function () {
        $(this).datepicker({
            format: $(this).attr('dateformat'),
            weekStart: 1,
            // in version 1.8.x the attribute componentButtonOnly was removed
            // componentButtonOnly: true,
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
                var dateReg = /^\d{2}[.]\d{2}[.]\d{4}$/;
                var inputField = $(this).prev();
                var date = inputField.val().split('.');

                // delete underscore placeholders
                var placeholderReg = /\D/gi;
                date[0] = date[0].replace(placeholderReg, "");
                date[1] = date[1].replace(placeholderReg, "");
                date[2] = date[2].replace(placeholderReg, "");
                var dateString;
                if (date[0] === "99") {
                    // Secret-Code: 99 = set focus of the datepicker to current date
                    dateString = currentDateAsString();
                } else if (date[0] === "00" || date[1] === "00") {
                    dateString = setValidDateAsString(date);
                } else {
                    // copy the manuell entered date into the datepicker
                    // out of range date inputs will be fixed
                    dateString = fixDateOutOfRange(date);
                }
                $(this).parent().datepicker('setDate', dateString);
                $(this).parent().datepicker('update');
            });

        //read the limit for expanding two-digit years. Is used later on.
        var zweistelligeJahreszahlenErgaenzenGrenze = $('#formDateJahresZahlenErgaenzenGrenze').val();
        var $datumInputFeld = $(this).find('input');
        $datumInputFeld.focusout(function (event) {
            if (zweistelligeJahreszahlenErgaenzenGrenze !== "-1") {
                datumErgaenzen($datumInputFeld, zweistelligeJahreszahlenErgaenzenGrenze);
            }

            // Magic Number: set date to today
            var date = $datumInputFeld.val().split('.');
            if (date[0] === "99") {
                $datumInputFeld.val(currentDateAsString());
            } else {
                // out of range date inputs will be fixed
                $datumInputFeld.val(fixDateOutOfRange(date));
            }
        });
    });

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });

    function fixDateOutOfRange(date) {
        var year = date[2],
            month = date[1],
            day = date[0];
        // Assume not leap year by default (note zero index for Jan)
        var daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

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
        var maxDays = daysInMonth[month - 1];
        if (day > maxDays) {
            day = maxDays;
        }

        return day + '.' + month + '.' + year;
    }

    // --------------------------------------------------------
    // Input Masks
    // --------------------------------------------------------
    // all input elements that have the attribut "inputmask"
    var $inputMasks = $('input[data-isymask-mask][data-isymask-mask!=""]').filter(':not(.isyfact-inputmask_ajaxtoken)');
    $inputMasks.each(function () {
        var $inputMask = $(this);
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
        var $inputMask = $(this);

        if (e.key === 'Enter') {
            // delete all placeholder characters
            var existentVal = $inputMask.val();
            var newVal = existentVal.replace(/_/g, '');
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
    var listpickerLoeseSchluesselAuf = function (listpickerfield, indexSpalteSchluesselWert) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // cut previously resolved value from the input field
            // only keep the key that will be resolved
            listpickerfield.val(listpickerfield.val().substring(0,
                listpickerfield.val().indexOf(" - ")));
        }
        var $id;
        if (listpickerfield.attr('data-isymask-mask') !== undefined) {
            listpickerfield.mask();
            $id = listpickerfield.val();
            listpickerfield.unmask();
            if (listpickerfield.val() == "null" || listpickerfield.val() == "nul") {
                listpickerfield.val("");
            }
        }
        var $parent = listpickerfield.parent();
        var $tr = $parent.find("tr[id='" + $id + "']");
        var $td = $tr.find("td:nth-child(" + indexSpalteSchluesselWert + ")");
        var $value = $td.text();
        if ($value !== '') {
            listpickerfield.val($id + ' - ' + $value);
        } else {
            var $filter = $parent.find("input[id*='listpickerFilter']");
            var $listpickerContent = $parent.find(".listpicker-content");
            var $listpickerLastFilter = $listpickerContent.find("input[id*=lastfilter]");
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
    var listpickerMaskieren = function (listpickerfield) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // prevents that digits from the value remain in the input field
            listpickerfield.val(listpickerfield.val().substring(0,
                listpickerfield.val().indexOf(" - ")));
        }
        if (listpickerfield.attr('data-isymask-mask') !== undefined) {
            listpickerfield.mask();
        }
    };

    var $listpickerContainer = $(".listpicker-container").filter(':not(.rf-listpicker_ajaxtoken)');
    $listpickerContainer.each(function () {

        var $listpicker = $(this);
        var $listpickerField = $listpicker.find("[id$='listpickerField']");
        var $listpickerContent = $listpicker.find(".listpicker-content").first();
        var $listpickerFilter = $listpicker.find("[id$='listpickerFilter']");
        var $listpickerMinWidth = $listpicker.find("[id$='listpickerMinWidth']");
        var listpickerAjaxFormId = $listpicker.find("[id$='listpickerAjaxForm']").val();
        var $listpickerAjaxForm = null;
        //find the hidden input, that contains the column which should be used as a value
        var listpickerSchluesselwertSpalte = $listpicker.find("[id$='inputComplement']").val();
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
            var $charpickers = $(".special-char-picker-widget");
            $charpickers.each(function () {
                $(this).hide();
            });

            // if a charpicker is open and new listpickers are opened, the old focus is removed
            var $active_charpickers_field = $(".charpicker-focused");
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
                var id;
                // if the key of the input field was already resolved, we need to reisolate the key
                if ($listpickerField.val().indexOf(" - ") >= 0) {
                    id = $listpickerField.val().substring(0, $listpickerField.val().indexOf(" - "));
                } else {
                    id = $listpickerField.val();
                }
                $listpickerContent.find("tbody tr[id='" + id + "']").addClass("active");
                $listpickerContent.find("tbody tr").not("[id='" + id + "']").removeClass("active");

                // set cursor in Listpickerwidget to currently active row
                var $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");
                scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow);
            } else {
                // on an empty or deleted $listpickerField delete previously 'active' elements
                $listpickerContent.find("tbody tr").removeClass("active");
            }

        });


        // intercept clicks and close picker if needed
        $(document).click(function (e) {

            var $target = $(e.target);

            if ($listpickerContent.has($target).length <= 0) {
                // click is outside of the dropdown, close picker
                $listpicker.removeClass('open');
            } else {
                // click is on the dropdown, select current row
                if ($target.is("tr") || $target.is("td")) {
                    var $row = null;
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
                var $row = $listpickerContent.find("tbody tr:visible.active").first();
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
            var keyCode = event.keyCode;
            var valid = inputChangingKeycode(keyCode); // does the key press change the filter content?
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
                                var $rows = $listpickerContent
                                    .find(".rf-listpicker-table tbody tr");

                                var filterFunction = function ($row,
                                                               inverse) {

                                    var $tdsAfterFilter = $row
                                        .find('td')
                                        .filter(
                                            function () {
                                                var $td = $(this);
                                                var text = $td
                                                    .text();
                                                var listpickerVal = $listpickerFilter
                                                    .val();
                                                var compare = text
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

                                var $filteredRows = $rows
                                    .filter(function () {
                                        return filterFunction(
                                            $(this), false);
                                    });
                                var $unfilteredRows = $rows
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

            var keyPressed = e.which;

            // currently active visible element
            var $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");

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
    var $buttonSelectpicker = $('button.selectpicker');
    $buttonSelectpicker.click(function (event) {
        $listpickerContainer.removeClass('open');
    });

    // --------------------------------------------------------
    // Tabs
    // --------------------------------------------------------
    // control preloaded tabs
    $('.isy-tab').each(function () {
        var $isyTab = $(this);

        // TabAutoscroll: Is "tabHochScrollen" in a tabGroub active?
        var $tabHochScrollen = false;
        if ($isyTab.hasClass('tabHochScrollen')) {
            $tabHochScrollen = true;
        }

        $isyTab.children().each(function () {
            var $li = $(this);
            var $liLink = $(this).find("a");

            if ($li.hasClass('skipAction')) {
                $liLink.unbind("click");
                $liLink.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"
            }

            $liLink.click(function (event) {
                if ($li.hasClass('skipAction')) {
                    event.preventDefault();
                }

                // remove current tab
                var liIdAlt = $isyTab.find(".active").attr('id');
                $isyTab.find(".active").removeClass("active");
                $isyTab.next().find("#" + liIdAlt).removeClass("active");

                // activate tab
                var liIdNeu = $li.attr('id');
                $li.addClass("active");
                //$isyTab.next().find("#" + liIdNeu).addClass("active");

                var aktiverTab = $isyTab.next().find("#" + liIdNeu);
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
    var $buttonInjectPostGroups = $("[id$='buttonInjectPostGroup']");
    $buttonInjectPostGroups.each(function () {
        var $group = $(this);

        // find clickable element in the ButtonInjectPostGroup
        var $actualButton = $group.find(":nth-child(4)");

        // find button for POST-action
        var $postButtonId = $group.find("[id$='postButton']").val();
        var $postButton = $("[id$='" + $postButtonId + "']");
        //jsf ids might have other suffixes delimited with ':', e.g. ':ajax_button'
        //prefer no-suffix version, but if none is found, look for suffixed buttons as well
        if ($postButton.length === 0){
            $postButton = $("[id*='" + $postButtonId + ":']");
        }

        // find field for posted
        var $posted = $group.find("[id$='posted']");

        // fiend field for continue
        var $continue = $group.find("[id$='continue']");

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
    var $forms = $("form");
    $forms.each(function () {
        var $form = $(this);
        $form.unbind("keypress");
        var $defaultButton = $form.find("[id*='" + $form.find("[id$='defaultButtonID']").val() + "']");
        if ($defaultButton.length > 0) {
            // remove original bind
            $form.unbind("keypress");
            // the form contains a default button
            $form.bind("keypress", function (event) {
                if (event.keyCode == 13) {
                    var $source = $(document.activeElement);
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
        var $bc = $(this);
        var timerId;

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
            var $table = $(this);

            // =============== START FILTER-ROW ===================== //
            var timeId = 0;
            // replace buttons so that no server action is called
            var $filterRow = $table.find("thead tr.filter-row");
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
                var $this = $(this);
                $filterRow.find('select.filter-dropdown').selectpicker('val', '');
                $filterRow.find('select.filter-dropdown').data('property', '');
                $filterRow.find('input.table-filter').val('');
                $filterRow.find('input.table-filter').data('property', '');
                $filterRow.find('a.table-clear-filter').hide();
                $this.hide();
                doItAll();
            });

            //list of items with details, is used by multiple functions
            var $itemsWithDetails = $table.find("tbody tr");
            var $allFilters = $filterRow.find('td');
            var filterSingle = function ($td) {
                // filter value
                var filter = '';
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
                var filterTd = 'td:nth-child(' + ($td.index() + 1) + ')';
                var lastMatched = false; // helpervariable for showing details
                $.each($itemsWithDetails, function (i, item) {
                    var $item = $(this);
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
                    var $td = $item.find(filterTd);
                    var val = $td.data('filter');
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
            var filterAll = function (init) {
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
            var $pageControl = $table.find("tfoot tr").eq(0).find("td").eq(0);
            var getCurrentPage = function () {
                return $pageControl.data("currentpage") || 1;
            };
            var setCurrentPage = function (pageNumber) {
                $pageControl.data("currentpage", pageNumber);
                $table.find("input[type=hidden][id$=rfDataTableCurrentPage]").val(pageNumber);
            };
            var getPageSize = function () {
                return $pageControl.data("pagesize") || getItemCount();
            };
            var getItemCount = function () {
                return $table.find("tbody tr:not('.details-preview'):not('.filtered')").length;
            };
            var getPageCount = function () {
                var pageSize = getPageSize();
                return Math.floor((getItemCount() + pageSize - 1) / pageSize);
            };
            var isFirstPage = function () {
                return (getCurrentPage() == 1);
            };
            var isLastPage = function () {
                return (getCurrentPage() == getPageCount());
            };
            var getPageFrom = function () {
                return Math.max(getCurrentPage() - getPaginatorSize() + 1, 1);
            };
            var getPageTo = function () {
                return Math.min(getCurrentPage() + getPaginatorSize() - 1, getPageCount());
            };
            var getPaginatorSize = function () {
                return $pageControl.data("paginatorsize");
            };
            var rePageNumber = new RegExp('\\{0\\}', 'gi');
            var rePageCount = new RegExp('\\{1\\}', 'gi');
            var setupLi = function ($li, pageNumber, disabled) {
                setupButton($li.find('button'), pageNumber, disabled);
                $li.data('page', pageNumber);
            };
            var setupButton = function ($btn, pageNumber, disabled) {
                var text = $btn.parents('li').data('text');
                $btn.text(text.replace(rePageNumber, pageNumber).replace(rePageCount, getPageCount()));
                var tooltip = $btn.parents('li').data('tooltip');
                $btn.attr('title', tooltip.replace(rePageNumber, pageNumber).replace(rePageCount, getPageCount()));
                $btn.prop('disabled', disabled);
            };
            var doPagination = function () {
                var text = $pageControl.find('li.pagination-text').data('text');
                $pageControl.find('li.pagination-text').text(text.replace(rePageNumber, getCurrentPage()).replace(rePageCount, getPageCount()));

                setupLi($pageControl.find('li.page-first'), 1, isFirstPage());
                setupLi($pageControl.find('li.page-pre'), isFirstPage() ? 1 : getCurrentPage() - 1, isFirstPage());

                $pageControl.find("li.page-number.generated").remove();
                var $next = $pageControl.find('li.page-next');
                var pageNum = getPageFrom();
                for (pageNum = pageNum; pageNum <= getPageTo(); pageNum++) {
                    var $page = $pageControl.find("li.page-number.master").clone().removeClass("master hidden").addClass("generated");
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
            var cumulative = $pageControl.hasClass('SIMPLE');
            var renderPage = function () {
                if ($pageControl.hasClass('NORMAL')) {
                    doPagination();
                }
                var currentPage = getCurrentPage();
                var pageSize = getPageSize();
                // find all table entries with details-preview and hide them
                $itemsWithDetails.hide();
                var itemCount = getItemCount();
                // limit
                var itemFrom = cumulative ? 0 : (currentPage - 1) * pageSize;
                var itemTo = Math.min(currentPage * pageSize, itemCount);
                var isLastPage = (itemCount == itemTo);
                var items = $itemsWithDetails.filter(":not(.details-preview):not(.filtered)");
                var itemFromIndex = items.eq(itemFrom).index();
                var pageItems;
                if (isLastPage) {
                    pageItems = $itemsWithDetails.slice(itemFromIndex);
                } else {
                    var lastItemToShow = items.eq(itemTo - 1);
                    var itemToIndex = lastItemToShow.index();
                    if (lastItemToShow.next().hasClass('details-preview')) {
                        itemToIndex++;
                    }
                    pageItems = $itemsWithDetails.slice(itemFromIndex, itemToIndex + 1);
                }
                // fix body widths wenn using pagination with scrolling. do you really have to?
                if ($table.hasClass("tablescroll_body")) {
                    var header = $table.parent().prev().find("thead tr:first-of-type th");
                    var body = pageItems.filter(':not(.filtered)').eq(0).find("td");
                    var i;
                    for (i = 0; i < header.length; i++) {
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
                    var page = $(this).data('page');
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
                var $th = $(this);
                $th.find('a').each(function () {
                    var $a = $(this);
                    $a.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"-events
                    $a.unbind("click");
                });
            });
            var getSortProperty = function () {
                return $table.find("input[type=hidden][id$=rfDataTableSortProperty]").val();
            };
            var setSortProperty = function (sortProperty) {
                return $table.find("input[type=hidden][id$=rfDataTableSortProperty]").val(sortProperty);
            };
            var getSortDirection = function () {
                return $table.find("input[type=hidden][id$=rfDataTableSortDirection]").val();
            };
            var setSortDirection = function (sortDirection) {
                return $table.find("input[type=hidden][id$=rfDataTableSortDirection]").val(sortDirection);
            };
            var sortValue = function (tr, index) {
                var filter = 'td:nth-child(' + index + ')';
                var $td = $(tr).find(filter);
                var val = $td.data('sort');
                if (val === undefined || val === '') {
                    val = $td.text().trim();
                }
                return val;
            };
            var sort = function ($th) {
                var thisSortProperty = $th.data("sortattribute");
                var index = $th.index() + 1;
                var items = $itemsWithDetails.filter(":not(.details-preview)");
                var isAsc = $th.hasClass('sort-up');
                var comp = function (v1, v2) {
                    if (v1 > v2) {
                        return isAsc ? 1 : -1;
                    }
                    if (v1 < v2) {
                        return isAsc ? -1 : 1;
                    }
                    return 0;
                };
                items.sort(function (tr1, tr2) {
                    var v1 = sortValue(tr1, index);
                    var v2 = sortValue(tr2, index);
                    if (+v1 === +v1 && +v2 === +v2) {
                        // compare as numbers
                        return comp(+v1, +v2);
                    }
                    // compare as strings
                    return comp(v1, v2);
                });
                // at this point the table entries are sorted without the details
                // we need to correctly assign the details now
                var newItems = [];
                $.each(items, function (i, item) {
                    var $item = $(item);
                    var index = $item.index(); // index in DOM before sorting
                    newItems.push($item);
                    // check if details are visible
                    var detail = $itemsWithDetails.eq(index).next();
                    if (detail.hasClass('details-preview')) {
                        // preserve detail-row
                        newItems.push(detail);
                    }
                });
                $itemsWithDetails.detach();
                var tbody = $table.find("tbody");
                tbody.append(newItems);
                $itemsWithDetails = $table.find("tbody tr");
            };
            $table.find('thead th.sortable a').bind('click', function (e) {
                e.preventDefault();
                var $th = $(this).parents('th');
                var sortClass = 'sort-up'; // sort ascending by default
                var thisSortProperty = $th.data("sortattribute");
                if (thisSortProperty == getSortProperty()) {
                    // invert sort direction
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
            var doItAll = function (init) {
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
        var $list = $(this);
        var timerId;

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
            var $this = $(this);
            var $sel = $this.find('select');
            $sel.find('option').each(function () {
                if (!this.value) return; // überspringe platzhalter
                $this.append('<button type="button" class="btn btn-default' + (this.selected ? ' active' : '') + (this.disabled ? ' disabled' : '') + '" value="' + this.value + '">' + this.text + '</button>');
            });
        }).on('click', 'button', function () {
        if ($(this).hasClass('active') || $(this).hasClass('disabled')) return;
        var $sel = $(this).parent().find('select');
        $sel.val(this.value);
        $(this).parent().find('input[type=submit]').click();
    });
}

function listpickerAjaxReload(callback, keyCode) {
    'use strict';

    // the listpicker filter that sends the event
    var $listpickerFilter = $(callback.source).first();
    var $listpickerContent = $listpickerFilter.parents(".listpicker-container").first().find(".listpicker-content");
    var $ajaxSpinner = $listpickerFilter.parent().parent().parent().parent().find('.listpicker-ajax-spinner');


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

        var listpickerAjaxFormId = $listpickerContent.find("[id$='listpickerAjaxForm']").val();
        var $listpickerAjaxForm = $("form[id$='" + listpickerAjaxFormId + "']");

        $listpickerContent.find("tbody").replaceWith($listpickerAjaxForm.find("tbody").clone());
        var $listpickerLastFilter = $listpickerContent.find("input[id*=lastfilter]");
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
    var invalid = (keyCode > 8 && keyCode < 14) ||
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

    // images
    $("[data-src].lazy").each(function () {
        var $lazyImage = $(this);
        if ($lazyImage.visible()) {
            var src = $lazyImage.attr("data-src");
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
    var inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        var dezimalstellen = $(inputField).data("decimalplaces");
        var result = formatiereInput(ref.value, dezimalstellen);
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
    var inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        var dezimalstellen = $(inputField).data("decimalplaces");
        var tausenderpunktGewuenscht = $(inputField).data("tausenderpunkt");
        var result = formatiereInput(ref.value, dezimalstellen);
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
    var value = input.split(".").join("");
    value = value.replace(',', '.');
    var tmp = parseFloat(value).toFixed(dezimalstellen);
    return tmp.replace('.', ',');
}

/**
 * shortens input to the given length
 */
function kuerzeInput(value, length) {
    'use strict';
    var kommaPosition = value.indexOf(",");
    var anzahlTausenderPunkte = parseInt(((kommaPosition - 1) / 3));
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
    var kommaPosition = value.indexOf(",");
    for (var i = 1; i < kommaPosition; i++) {
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
    var kommaPosition = value.indexOf(",");
    if (kommaPosition === -1) {
        kommaPosition = value.length;
    }
    for (var i = 1; i < kommaPosition; i++) {
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
        var start = ref.selectionStart;
        var end = ref.selectionEnd;

        // length of text is saved:
        // is used later on to determine how many characters were removed
        // to calculate the shift in position of the cursor
        var length = ref.value.length;

        // removes all characters but numbers and comma as a decimal separator
        // Attention: dots as thousands separators are also removed
        ref.value = ref.value.replace(/[^\d,.]/g, '');

        // checks whether characters were removed and shifts the cursor
        // accordingly - is needed for IE and Chrome; for FF replacing the text is sufficient
        var lengthAfterReplace = ref.value.length;
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
    var currentDate = new Date();
    var dayOfMonth = currentDate.getDate();
    var month = currentDate.getMonth() + 1;
    var year = currentDate.getFullYear();
    var heute = dayOfMonth.toString() + '.' + month.toString() + '.' + year.toString();
    var heuteMitFuehrendenNullen = heute.replace(/(^|\D)(\d)(?!\d)/g, '$10$2');

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
            var $table = $(this);
            // validate number of columns as a hint for the developer
            var spaltenImHeader = $table.find('thead tr').eq(0).find('th').length;
            var spaltenImFilter = $table.find('thead tr').eq(1).find('th').length || spaltenImHeader;
            var spaltenImBody = $table.find('tbody tr').eq(0).find('td').length || spaltenImHeader;
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
            var $filterRow = $table.find("thead tr.filter-row");
            var $clearAllFilterIcon = $filterRow.find("td.table-clear-all-filter button");
            var resetFilter = function () {
                // reset all filters
                $filterRow.find('input.table-filter').val('').data('property', '');
                // reset all dropdown filters
                $filterRow.find('select.filter-dropdown').data('property', '').selectpicker('val', '');
                // hides all "clear filter" icons
                $filterRow.find('a.table-clear-filter').hide();
                // hide "clear-all-filters" icon
                $clearAllFilterIcon.hide();
            };
            var hasFilter = function () {
                // at least one filterfield has a value or one dropdown is selected
                return ($filterRow.find('input.table-filter, select.filter-dropdown').filter(function () {
                    return !!this.value;
                }).length);
            };
            var checkClearAllFilter = function () {
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
                    var $this = $(this);
                    checkClearAllFilter();
                    // click on hidden button to execute its action
                    $this.parent().next().click();
                });
            // click on clear-filter icon:
            // resets filter, adjusts view of the clear-filter/clear-all-filter icons and executes the action
            $filterRow.find('a.table-clear-filter')
                .click(function () {
                    var $this = $(this);
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
                    var isClient = $table.hasClass('CLIENT');
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
                    var $this = $(this);
                    $(this).trigger('blur');
                    $(this).focus();
                }
            // generally: execute action if the filter loses focus, but only if the filter has changed
            }).blur(function () {
                var $this = $(this);
                $this.trigger('change');
                if ($this.val() != $this.data('property')) {
                    // click on hidden button to execute action
                    $this.data('property', $this.val());
                    $this.next().next().click();
                }
                // adjust view of clear-filter and clear-all-filter icons
            }).change(function () {
                var $this = $(this);
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
    var aktuellesJahr = parseInt(new Date().getFullYear().toString().substring(2, 4));
    grenze = (aktuellesJahr + parseInt(grenze)).toString();
    var aktuelleWerte = inputFeld.val().split('.');
    if (aktuelleWerte.length === 3 && aktuelleWerte[2].replace(/_/g, '').length === 2) {
        var praefix;
        var jahresZahl = aktuelleWerte[2].replace(/_/g, '');
        if (jahresZahl <= grenze) {
            praefix = "20";
        } else if (jahresZahl > grenze) {
            praefix = "19";
        }
        var ergebnis = praefix + jahresZahl;
        aktuelleWerte[2] = ergebnis;
        inputFeld.val(aktuelleWerte[0] + "." + aktuelleWerte[1] + "." + aktuelleWerte[2]);
    }
};

//Code that triggers initialization of a listpicker through the servlet
initialisierenListpickerServlet = function () {
    "use strict";
    var $listpicker = $(".servlet.listpicker-filter");
    $listpicker.each(function (i, listpicker) {
        registerListpickerfilter(listpicker);
    });
};


//register a listpicker
registerListpickerfilter = function (identifier) {
    "use strict";
    var $listpickerFilter = $(identifier);
    var listpickerFilterInput = $listpickerFilter.children()[0];
    var url = $listpickerFilter.siblings("div.rf-listpicker-table-container").find(".servletTable")[0].getAttribute("data-servleturl");

    //Hereafter the url paramaters will be encoded
    //only the paramater value will be encoded, not the parameter name itself
    var urlsplit = url.split("?");

    //the first part of the URL (the part before the parameters) remains unchanged
    var urlEncoded = urlsplit[0] + '?';

    //split the second part
    if (urlsplit.length > 1) {
        var attributeGesetzt = urlsplit[1].split("&");
        attributeGesetzt.forEach(function (attribut) {
            var attributSplit = attribut.split("=");
            urlEncoded = urlEncoded + attributSplit[0] + '=' + encodeURIComponent(attributSplit[1]) + "&";
        });
    }

    //Initialize contents of listpicker
    //This is where the actual request is sent!
    $.get(urlEncoded + "filter=" + encodeURIComponent(listpickerFilterInput.value)).done(function (data) {
        createListpickerTable(data, $listpickerFilter);
    });
    listpickerFilterInput.dataset.oldvalue = listpickerFilterInput.value;

    var $listpickerContent = $listpickerFilter.parent().parent();
    var $listpickerContainer = $listpickerContent.parent();
    var $listpickerField = $listpickerContainer.find('*[id*=listpickerField]');

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
    var servletUrl = event.data.url;
    var listpickerFilter = event.data.listpickerfilter;
    var listpickerFilterInput = event.data.listpickerfilter;
    var delay = 500;
    window.setTimeout(function (filter) {
        var input = listpickerFilter.children()[0];
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
    var $tablecontainer = $(listfilter).siblings("div.rf-listpicker-table-container");
    var $table = $tablecontainer.find(".servletTable");
    $table.empty();
    var tableJson = JSON.parse(responseText);
    for (var j in tableJson.items) {
        var item = tableJson.items[j];
        var tr = $('<tr>').attr('id', item.id);
        for (var i = 0; i < item.attrs.length; i++) {
            var td = $('<td>').text(item.attrs[i].trim());
            tr.append(td);
        }
        $table.append(tr);
    }
    if (tableJson.weiterFiltern === true) {
        var trWeiterFiltern = $('<tr>');
        var tdWeiterFiltern = $("<td>").text(tableJson.messageItem).attr('colspan', 2);
        trWeiterFiltern.append(tdWeiterFiltern);
        $table.append(trWeiterFiltern);
    }
    $(listfilter).parent().parent().siblings('.form-control').focusout();
};

//On click inside the document, if a listpicker was open, it will be closed
//and additionally the focusout-method will be triggered, to cause the key to be resolved
$(document).click(function (e) {
    "use strict";
    var $target = $(e.target);
    var $listpickerContainer = $('.listpicker-container.open');
    var $listpickerContent = $listpickerContainer.find('.listpicker-content');
    if ($listpickerContent.has($target).length <= 0 && $listpickerContainer.hasClass('open')) {
        $listpickerContainer.removeClass('open');
        $listpickerContent.siblings('.form-control').focusout();
        $target.focus();

    }

});
