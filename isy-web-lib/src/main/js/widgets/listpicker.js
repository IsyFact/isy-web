import { inputChangingKeycode, scroll_to } from '../common/common-utils';

/**
 * Add handlers to Listpickers that haven't been initialized yet.
 * Already initialized Listpickers are marked with a token class (rf-listpicker-ajaxtoken)
 * and will be skipped on subsequent calls.
 */
export function initListpickers(){
    const $listpickerContainer = $(".listpicker-container").filter(':not(.rf-listpicker_ajaxtoken)');
    $listpickerContainer.each(registerListpickerHandlers);
    $listpickerContainer.addClass('rf-listpicker_ajaxtoken');
}

/** registers all handlers for the listpicker. */
export function registerListpickerHandlers() {
    const $listpicker = $(this);
    const $listpickerContainer = $(".listpicker-container");
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

    //On click inside the document, if a listpicker was open, it will be closed
    //and additionally the focusout-method will be triggered, to cause the key to be resolved
    $(document).click(closeListpickerHandler);
    // select current row
    $(document).click(function (e) {
        const $target = $(e.target);
        if ($listpickerContent.has($target).length > 0) {
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
        if (!e.ctrlKey && !e.altKey && !e.shiftKey && e.keyCode === 13) {
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
                function () {setTimeout(filterRows, 100);});
    }


    function filterRows() {
        // Filter
        const $rows = $listpickerContent.find(".rf-listpicker-table tbody tr");
        const listpickerVal = $listpickerFilter.val();

        const $filteredRows = $rows
            .filter(function () {return filterFunction($(this), listpickerVal, false);});
        const $unfilteredRows = $rows
            .filter(function () {return filterFunction($(this), listpickerVal, true);});

        $filteredRows.css("display", "none");
        $unfilteredRows.css("display","table-row");
    }


    /** checks whether a value is contained somewhere in the row */
    function filterFunction($row, listpickerVal, inverse) {
        const $tdsAfterFilter = $row
            .find('td')
            .filter(
                function () {
                    const $td = $(this);
                    const text = $td.text();
                    const compare = text
                        .toLowerCase()
                        .indexOf(
                            listpickerVal.toLowerCase());
                    return compare !== -1;
                });

        if (inverse) {
            return $tdsAfterFilter.length > 0;
        } else {
            return $tdsAfterFilter.length <= 0;
        }
    }

    $listpickerFilter.bind('keydown', function (e) {

        const keyPressed = e.which;

        // currently active visible element
        const $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");

        // downwards arrow key
        if (keyPressed === 40) {
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
        } else if (keyPressed === 38) {
            // if there is a previous element, select it
            if ($activeVisibleRow.length > 0 && $activeVisibleRow.prevAll('tr:visible:first').length > 0) {
                $activeVisibleRow.removeClass("active");
                $activeVisibleRow.prevAll('tr:visible:first').addClass("active");
                scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow.prevAll('tr:visible:first'));
            }
            // ESC
        } else if (keyPressed === 27) {
            $listpicker.removeClass('open');
            $listpickerField.focus();
            // Tab
        } else if (keyPressed === 9) {
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

        //if the field is focused, we need to mask it.
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
}

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

// Used to reload Listpicker on ajax event
// is called via f:ajax call directly from xhtml-template
export function listpickerAjaxReload(callback, keyCode) {
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

//Code that triggers initialization of a listpicker through the servlet
export function initialisierenListpickerServlet() {
    "use strict";
    const $listpicker = $(".servlet.listpicker-filter");
    $listpicker.each(function (i, listpicker) {
        registerListpickerfilter(listpicker);
    });
};

/** register a filter for a servlet-listpicker. */
const registerListpickerfilter = function (identifier) {
    "use strict";
    const $listpickerFilter = $(identifier);
    const listpickerFilterInput = $listpickerFilter.children()[0];
    const url = $listpickerFilter.siblings("div.rf-listpicker-table-container").find(".servletTable")[0].getAttribute("data-servleturl");

    //Hereafter the url parameters will be encoded
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
    event.stopImmediatePropagation();
    //fetch the needed values from the data-attributes of the event (were set when called)
    const servletUrl = event.data.url;
    const listpickerFilter = event.data.listpickerfilter;
    const listpickerFilterInput = event.data.listpickerfilter;
    const delay = 500;
    window.setTimeout(function (filter) {
        const input = listpickerFilter.children()[0];
        if (input.dataset.oldvalue === "undefined" || input.value !== input.dataset.oldvalue) {
            $.get(servletUrl + "filter=" + encodeURIComponent(input.value)).done(function (data) {
                createListpickerTable(data, listpickerFilter);
            });
            input.dataset.oldvalue = input.value;
        }
    }, delay, listpickerFilterInput);
}

/** Creates a ListpickerTable based on the responseText. */
function createListpickerTable (responseText, listfilter) {
    const $tablecontainer = $(listfilter).siblings("div.rf-listpicker-table-container");
    const $table = $tablecontainer.find(".servletTable");
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
        const trWeiterFiltern = $('<tr>');
        const tdWeiterFiltern = $("<td>").text(tableJson.messageItem).attr('colspan', 2);
        trWeiterFiltern.append(tdWeiterFiltern);
        $table.append(trWeiterFiltern);
    }
    $(listfilter).parent().parent().siblings('.form-control').focusout();
}

/**
 * Handler used for clickevent outside of listpicker.
 * On click inside the document, if a listpicker was open, it will be closed
 * and additionally the focusout-method will be triggered, to cause the key to be resolved
 * @param event: click-event
 */
export function closeListpickerHandler(event) {
    const $target = $(event.target);
    const $listpickerContainer = $('.listpicker-container.open');
    const $listpickerContent = $listpickerContainer.find('.listpicker-content');
    if ($listpickerContent.has($target).length <= 0 && $listpickerContainer.hasClass('open')) {
        $listpickerContainer.removeClass('open');
        $listpickerContent.siblings('.form-control').focusout();
        $target.focus();
    }
}
