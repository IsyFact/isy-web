$(document).ready(function() {
    'use strict';

    if (typeof (jsf) != "undefined") {

        // --------------------------------------------------------
        // Ajax-Callback
        // --------------------------------------------------------
        jsf.ajax.addOnEvent(function(callback) {

            if (callback.status === 'success') {
                // Listpicker values
                tastatursteuerungAktualisieren();
            }

        });
    }

    tastatursteuerungAktualisieren();

    // keyboard controls for link navigation
    $(document).bind('keydown', 'alt+shift+l', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungLinksnavigation(null, $(e.target));
    });

    // keyboard controls for main menu
    $(document).bind('keydown', 'alt+shift+m', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungNavigation(null, $(e.target));
    });

    // keyboard controls for side toolbar
    $(document).bind('keydown', 'alt+shift+s', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungSeitentoolbar(0, $(e.target));
    });

});

tastatursteuerungAktualisieren = function() {
    'use strict';
    const $inputs = $("input").filter(':not(.tatatursteuerung_ajaxtoken)');

    // link navigation on input fields
    $inputs.bind('keydown', 'alt+shift+l', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungLinksnavigation(null, $(e.target));
    });
    // keyboard controls for main menu
    $inputs.bind('keydown', 'alt+shift+m', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungNavigation(null, $(e.target));
    });
    // keyboard controls for side toolbar
    $inputs.bind('keydown', 'alt+shift+s', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungSeitentoolbar(0, $(e.target));
    });

    $inputs.addClass('tatatursteuerung_ajaxtoken');
};

tastatursteuerungLinksnavigation = function($focuselement, $origin) {
    'use strict';
    let $neuerFocus;

    // focus first element
    if ($focuselement === null) {
        $neuerFocus = $($('.linksnavigation-option').first());
    } else {
        $neuerFocus = $focuselement;
    }

    $neuerFocus.focus();

    // register tabs
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const $optionNext = $(this).parent().nextAll().find(
                '.linksnavigation-option');
        if ($optionNext.length > 0) {
            tastatursteuerungLinksnavigation($($optionNext.first()), $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const $optionPrev = $(this).parent().prevAll().find(
                '.linksnavigation-option');
        if ($optionPrev.length > 0) {
            tastatursteuerungLinksnavigation($($optionPrev.last()), $origin);
        }

    });

    // open link on pressing enter
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // focus origin on pressing Esc
    $neuerFocus.bind('keydown', 'esc', function(e) {
        e.preventDefault();
        e.stopPropagation();
        this.blur();
        if ($origin.length > 0) {
            $origin.first().focus();
        } else {
            $(document).focus();
        }
    });

    // remove listeners on focusout
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).unbind();
    });

};

tastatursteuerungNavigation = function($focuselement, $origin) {
    'use strict';
    let $neuerFocus;

    // focus first element
    if ($focuselement === null) {
        $neuerFocus = $($("[id*='main-nav'] ul li a").first());
    } else {
        $neuerFocus = $focuselement;
    }

    $neuerFocus.focus();
    $neuerFocus.addClass('main-nav-focus');

    // register tabs
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const $optionNext = $(this).parent().nextAll().find('a');
        if ($optionNext.length > 0) {
            tastatursteuerungNavigation($($optionNext.first()), $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const $optionPrev = $(this).parent().prevAll().find('a');
        if ($optionPrev.length > 0) {
            tastatursteuerungNavigation($($optionPrev.last()), $origin);
        }

    });

    // open link when pressing enter
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // focus origin when pressing Esc
    $neuerFocus.bind('keydown', 'esc', function(e) {
        e.preventDefault();
        e.stopPropagation();
        this.blur();
        if ($origin.length > 0) {
            $origin.first().focus();
        } else {
            $(document).focus();
        }
    });

    // remove listeners on focusout
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).removeClass('main-nav-focus');
        $(this).unbind();
    });

};

tastatursteuerungSeitentoolbar = function(index, $origin) {
    'use strict';
    let $neuerFocus;
    const $alleElemente = $("form[id*='seitenToolbarForm']")
            .find(".toolbar.page").find("input:enabled, button:enabled, a");
    const indexFocus = index;

    // focus first element
    if ($alleElemente.length === 0) {
        return;
    }
    $neuerFocus = $($alleElemente.get(indexFocus));

    $neuerFocus.focus();

    // register tabs
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const $alleElemente = $("form[id*='seitenToolbarForm']").find(
                ".toolbar.page").find("input:enabled, button:enabled, a");
        const indexNext = $alleElemente.index(this) + 1;
        console.log(indexNext);
        if ($alleElemente.length > indexNext) {
            tastatursteuerungSeitentoolbar(indexNext, $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e, indexFocus) {
        e.preventDefault();
        e.stopPropagation();
        const $alleElemente = $("form[id*='seitenToolbarForm']").find(
                ".toolbar.page").find("input:enabled, button:enabled, a");

        const indexPrev = $alleElemente.index(this) - 1;
        if (0 <= indexPrev) {
            tastatursteuerungSeitentoolbar(indexPrev, $origin);
        }

    });

    // open link when pressing enter
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // focus origin when pressing Escape
    $neuerFocus.bind('keydown', 'esc', function(e) {
        e.preventDefault();
        e.stopPropagation();
        this.blur();
        if ($origin.length > 0) {
            $origin.first().focus();
        } else {
            $(document).focus();
        }
    });

    // remove listeners on focusout
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).unbind();
    });

};
