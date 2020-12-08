// Refreshes the filter row of a datatable.
export function refreshDatatableFilterRow() {
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
