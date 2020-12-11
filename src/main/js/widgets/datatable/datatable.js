import {refreshDatatableFilterRow} from "./datatable-filterrow";
import {
    activateJSSorting,
    initClickableArea,
    setSelectAllCheckboxState,
    showHideDetail
} from "./datatable-functionalities";

/** Adds Handlers for all datatables that aren't initialized yet.
 * Already initialized Tables are marked with rf-data-table_ajaxtoken */
export function initDatatables(){
    const $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) expand clickable area of header columns
    $rfDataTables.find('th.sortable').click(function (event) {
        const $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    // (2) expand clickable area for selection of rows / set double click / initialize selection mode
    $rfDataTables.each(initClickableArea);
    // (3) register show-/hide-detail logic
    $("table.CLIENT.rf-data-table").each(showHideDetail);
    // (4) activate JS Sorting
    $('.rf-data-table').each(activateJSSorting);
    // (5) always set the correct state to the "select all" checkbox
    $rfDataTables.each(setSelectAllCheckboxState);

    $rfDataTables.addClass('rf-data-table_ajaxtoken');
}

// Creates a datatable and its necessary event handlers
export function createDatatable() {
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
        // limit
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
        let sortClass = 'sort-up'; // ascending sort per default
        const thisSortProperty = $th.data("sortattribute");
        if (thisSortProperty == getSortProperty()) {
            // reverse order
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
}
