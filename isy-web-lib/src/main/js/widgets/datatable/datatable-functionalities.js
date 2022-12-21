import {lazyLoad} from "../../common/common-utils";

/**
 * Function to highlight a selected row.
 * @param $trs rows
 * @param $tr row
 * @param selectionMode
 */
export function formatRowsFunction ($trs, $tr, selectionMode) {
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
}

/**
 * Expand clickable area for selection of rows / set double click / initialize selection mode.
 */
export function initClickableArea () {
    const $rfDataTable = $(this);
    const $rfDataTableSelectOption = $(this).find("[id$='rfDataTableSelectableOption']").first();
    const selectionMode = $(this).find("[id$='rfDataTableSelectionMode']").first().val();
    const selectActive = ($rfDataTableSelectOption.val() === 'true');
    const $rfDataTableDoubleClickActive = $(this).parent().find("[id$='rfDataTableDoubleClickActive']").first();
    const doubleClickActive = ($rfDataTableDoubleClickActive.text() === 'true');

    const $rows = $(this).find("tbody tr");
    $rows.each(function () {
        const $row = $(this);
        const clicks = 0;
        const timer = null;

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
}

/**
 * Register a "select all" checkbox.
 * @param $selectAllCheckbox
 * @param $rfDataTable
 */
export function selectAllFunction ($selectAllCheckbox, $rfDataTable) {
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
}

/**
 * Handles if certain details should be shown or hidden.
 */
export function showHideDetail () {
    const $table = $(this);
    // =============== START DETAILVIEW ===================== //
    $table.find('tbody div.detailview-actions button').prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
    const $showDetail = $table.find('div.detailview-actions button[id*=showDetail]');
    $showDetail.on('click.showdetail', showDetail);
    const $hideDetail = $table.find('div.detailview-actions button[id*=hideDetail]');
    $hideDetail.on('click.hidedetail', hideDetail);
    // =============== END DETAILVIEW ===================== //
}

/**
 * Hides unneeded elements.
 */
export function showDetail () {
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
    $tr.addClass('details-preview-master');
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
}

/**
 * Shows necessary elements that can be hidden.
 * @param e click event
 */
export function hideDetail (e) {
    e.preventDefault();
    e.stopImmediatePropagation();
    const $this = $(this);
    const $tr = $this.parents('tr');
    $tr.remove('details-preview-master');
    $tr.next().addClass('hidden');
    $this.attr('title', $this.parents('div.detailview-actions').data('show-tooltip'));
    $this.attr('id', $this.attr('id').replace("hideDetail", "showDetail"));
    $this.find('span').removeClass('icon-minus').addClass('icon-plus');
    // Eventhandler wechseln
    $this.off('click.hidedetail');
    $this.on('click.showdetail', showDetail);
}

/**
 * Activates the sorting for data table.
 */
export function activateJSSorting () {
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

}

/**
 * Calculate tristate.
 * @param $checkboxes
 * @param $selectAllCheckbox
 * @param $rfDataTable
 */
export function tristateBerechnen ($checkboxes, $selectAllCheckbox, $rfDataTable) {
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
}

/**
 * Sets the state of a select-all checkbox to the correct one.
 */
export function setSelectAllCheckboxState () {
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

}
