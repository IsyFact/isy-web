$(document).ready(function() {
    'use strict';

    if (typeof (jsf) != "undefined") {

        // --------------------------------------------------------
        // Ajax-Callback
        // --------------------------------------------------------
        jsf.ajax.addOnEvent(function(callback) {

            if (callback.status === 'success') {
                // Listpickerwerte
                tastatursteuerungAktualisieren();
            }

        });
    }

    tastatursteuerungAktualisieren();

    // Tastatursteuerung für Linksnavigation
    $(document).bind('keydown', 'alt+shift+l', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungLinksnavigation(null, $(e.target));
    });

    // Tastatursteuerung für Hauptmenü
    $(document).bind('keydown', 'alt+shift+m', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungNavigation(null, $(e.target));
    });

    // Tastatursteuerung für Seitentoolbar
    $(document).bind('keydown', 'alt+shift+s', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungSeitentoolbar(0, $(e.target));
    });

});

tastatursteuerungAktualisieren = function() {
    'use strict';
    var $inputs = $("input").filter(':not(.tatatursteuerung_ajaxtoken)');

    // Linksnavigation auf Eingabefeldern
    $inputs.bind('keydown', 'alt+shift+l', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungLinksnavigation(null, $(e.target));
    });
    // Tastatursteuerung für Hauptmenü
    $inputs.bind('keydown', 'alt+shift+m', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungNavigation(null, $(e.target));
    });
    // Tastatursteuerung für Seitentoolbar
    $inputs.bind('keydown', 'alt+shift+s', function(e) {
        e.preventDefault();
        e.stopPropagation();

        tastatursteuerungSeitentoolbar(0, $(e.target));
    });

    $inputs.addClass('tatatursteuerung_ajaxtoken');
};

tastatursteuerungLinksnavigation = function($focuselement, $origin) {
    'use strict';
    var $neuerFocus;

    // Erstes Element fokusieren
    if ($focuselement === null) {
        $neuerFocus = $($('.linksnavigation-option').first());
    } else {
        $neuerFocus = $focuselement;
    }

    $neuerFocus.focus();

    // Tabs registrieren
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var $optionNext = $(this).parent().nextAll().find(
                '.linksnavigation-option');
        if ($optionNext.length > 0) {
            tastatursteuerungLinksnavigation($($optionNext.first()), $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var $optionPrev = $(this).parent().prevAll().find(
                '.linksnavigation-option');
        if ($optionPrev.length > 0) {
            tastatursteuerungLinksnavigation($($optionPrev.last()), $origin);
        }

    });

    // Beim Drücken von Enter Link öffnen
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // Beim Drücken von Esc das alte Element fokussieren
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

    // Beim Verlassen alle Listener entfernen
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).unbind();
    });

};

tastatursteuerungNavigation = function($focuselement, $origin) {
    'use strict';
    var $neuerFocus;

    // Erstes Element fokusieren
    if ($focuselement === null) {
        $neuerFocus = $($("[id*='main-nav'] ul li a").first());
    } else {
        $neuerFocus = $focuselement;
    }

    $neuerFocus.focus();
    $neuerFocus.addClass('main-nav-focus');

    // Tabs registrieren
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var $optionNext = $(this).parent().nextAll().find('a');
        if ($optionNext.length > 0) {
            tastatursteuerungNavigation($($optionNext.first()), $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var $optionPrev = $(this).parent().prevAll().find('a');
        if ($optionPrev.length > 0) {
            tastatursteuerungNavigation($($optionPrev.last()), $origin);
        }

    });

    // Beim Drücken von Enter Link öffnen
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // Beim Drücken von Esc das alte Element fokussieren
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

    // Beim Verlassen alle Listener entfernen
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).removeClass('main-nav-focus');
        $(this).unbind();
    });

};

tastatursteuerungSeitentoolbar = function(index, $origin) {
    'use strict';
    var $neuerFocus;
    var $alleElemente = $("form[id*='seitenToolbarForm']")
            .find(".toolbar.page").find("input:enabled, button:enabled, a");
    var indexFocus = index;

    // Erstes Element fokusieren
    if ($alleElemente.length === 0) {
        return;
    }
    $neuerFocus = $($alleElemente.get(indexFocus));

    $neuerFocus.focus();

    // Tabs registrieren
    $neuerFocus.bind('keydown', 'tab', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var $alleElemente = $("form[id*='seitenToolbarForm']").find(
                ".toolbar.page").find("input:enabled, button:enabled, a");
        var indexNext = $alleElemente.index(this) + 1;
        console.log(indexNext);
        if ($alleElemente.length > indexNext) {
            tastatursteuerungSeitentoolbar(indexNext, $origin);
        }
    });

    $neuerFocus.bind('keydown', 'shift+tab', function(e, indexFocus) {
        e.preventDefault();
        e.stopPropagation();
        var $alleElemente = $("form[id*='seitenToolbarForm']").find(
                ".toolbar.page").find("input:enabled, button:enabled, a");

        var indexPrev = $alleElemente.index(this) - 1;
        if (0 <= indexPrev) {
            tastatursteuerungSeitentoolbar(indexPrev, $origin);
        }

    });

    // Beim Drücken von Enter Link öffnen
    $neuerFocus.bind('keydown', 'enter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).click();
    });

    // Beim Drücken von Esc das alte Element fokussieren
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

    // Beim Verlassen alle Listener entfernen
    $neuerFocus.focusout(function($neuerFocus) {
        $(this).unbind();
    });

};
