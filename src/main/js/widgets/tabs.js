import { lazyLoad } from "../common/common-utils"

/**
 * Creates a group of tabs
 */
export function createTabGroup () {
    const $isyTabGroup = $(this);

    // TabAutoscroll: Is "tabHochScrollen" in a tabGroup active?
    let tabHochScrollenAktiv = false;
    if ($isyTabGroup.hasClass('tabHochScrollen')) {
        tabHochScrollenAktiv = true;
    }

    $isyTabGroup.children().each(function () {
        // Creating a tab.
        const $tab = $(this);
        const $tabLink = $(this).find("a");

        if ($tab.hasClass('skipAction')) {
            $tabLink.unbind("click");
            $tabLink.prop("onclick", null); // IE11 doesn't support .removeAttr() for "onclick"
        }

        $tabLink.click((event) => { openTab(event, $isyTabGroup, tabHochScrollenAktiv, $tab) });
    });
}

/**
 * Show content when clicking on a tab.
 * @param event click event
 * @param $isyTabGroup the user has clicked on
 * @param tabHochScrollenAktiv is autoscrolling enabled on click
 * @param $tab tab from $isyTabGroup the user has clicked on
 */
function openTab (event, $isyTabGroup, tabHochScrollenAktiv, $tab) {
    if ($tab.hasClass('skipAction')) {
        event.preventDefault();
    }

    // remove current tab
    const liIdAlt = $isyTabGroup.find(".active").attr('id');
    $isyTabGroup.find(".active").removeClass("active");
    $isyTabGroup.next().find("#" + liIdAlt).removeClass("active");

    // activate tab
    const liIdNeu = $tab.attr('id');
    $tab.addClass("active");
    //$isyTabGroup.next().find("#" + liIdNeu).addClass("active");

    const aktiverTab = $isyTabGroup.next().find("#" + liIdNeu);
    aktiverTab.addClass("active");

    // support tab-autoscroll
    if (tabHochScrollenAktiv) {
        $('html, body').animate({
            scrollTop: $(aktiverTab).offset().top - 50
        }, 'slow');
    }

    // save state
    $isyTabGroup.next().find("[id$='isyTabCurrentActiveTab']").first().val(liIdNeu.replace("tabId", ""));
    lazyLoad();
}
