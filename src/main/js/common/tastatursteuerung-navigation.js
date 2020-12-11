// --------------------------------------------------------
// navigation
// --------------------------------------------------------
export function initNavigation(){
    $('#main-nav').mainNavigation();
    registerKeybindOpenMainNavigation();
    registerKeybindsNavigateBetweenItems();
}

/** register alt+m keybind on document to open main navigation */
function registerKeybindOpenMainNavigation(){
    // keyboard control for navigation
    $(document).keydown(function (e) {
        //alt+m
        if (e.altKey && !e.shiftKey && !e.ctrlKey && e.keyCode === 77) {
            openMainNavigation();
        }
    });
}

/** Open Main Navigation */
function openMainNavigation(){
    $('.menustart').addClass('open');
    $(".search-option:first").focus();
}

/** register all keybinds to navigate between items. */
function registerKeybindsNavigateBetweenItems() {
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

        if (e.which === 37) {

            // left arrow
            // jump one column to the left or to the previous menu heading
            if (colIndex !== 1) {
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

        if (e.which === 39) {

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

        if (e.which === 40) {

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
        if (e.which === 38) {
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
}