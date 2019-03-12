$(document).ready(function () {
    'use strict';

    // Reaktion auf AJAX Requests
    // jsf = JSF JavaScript Library  von SUN Microsystems
    if (typeof(jsf) != "undefined") {
        // --------------------------------------------------------
        // Fehlerbehandlung
        // --------------------------------------------------------
        jsf.ajax
            .addOnError(function (data) {

                // Fehlernachricht bestimmen
                var errorMessage = $("[id$='ajaxErrorMessage']").val();
                var errorMessageTitle = $("[id$='ajaxErrorMessageTitle']").val();

                // In Konsole schreiben
                var error = data.description;
                console.log(error);

                // Fehlernachricht ersetzen
                errorMessage = errorMessage.replace("%%FEHLER%%", error);

                // Als Nachricht rendern
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
    // Magnific Popup Initialisierung
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
    // Funktionen initial aktivieren
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
    // Multipart Forms bei Bedarf aktivieren
    // --------------------------------------------------------
    if ($("[id$='multipartFormEnabled']").val() === 'true') {
        $("form").attr("enctype", "multipart/form-data");
    }

    // --------------------------------------------------------
    // Navigation
    // --------------------------------------------------------
    $('#main-nav').mainNavigation();

    // Tastatursteuerung für Navigation
    $(document).keydown(function (e) {
        if (e.altKey && !e.shiftKey && !e.ctrlKey && e.keyCode == 77) {
            $('.menustart').addClass('open');
            $(".search-option:first").focus();
        }
    });

    $(".search-option").keydown(function (e) {


        // Aktuelle Spalte
        var $divCol = $(this).parents(".col-lg-4").first();

        // Aktuelle Zeile
        var $divRow = $divCol.parents(".row").first();

        // Nächste Elemente
        var $liMenu;
        var $liMenuNext;
        var $divRowNext;
        var $divColNext;

        // Aktuellen Spaltenindex merken
        var $divColNeighbour = $divCol;
        var colIndex = 1;
        while ($divColNeighbour.prev().length !== 0) {
            $divColNeighbour = $divColNeighbour.prev();
            colIndex = colIndex + 1;
        }

        if (e.which == 37) {

            // Pfeil link
            // Springe eine Spalte nach links oder ins nächste Menü
            if (colIndex != 1) {
                // Es existiert noch ein Menüpunkt
                $($divRow.children().get(colIndex - 2)).find(".search-option").focus();
            } else {
                // Springe in das Menü auf der linken Seite, falls eines existiert
                $liMenu = $divRow.parents("li").first();
                $liMenuNext = $liMenu.prev();

                if ($liMenu.length >= 1) {
                    // Ein weiteres Menü existiert
                    $liMenu.removeClass("open").removeClass("active");
                    $liMenuNext.addClass("open").addClass("active");
                    $liMenuNext.find(".search-option:first").focus();
                }

            }

            // Verhindere Scrolling
            return false;
        }

        if (e.which == 39) {

            // Pfeil rechts
            // Springe eine Spalte nach rechts oder ins nächste Menü
            if ($divRow.children().length >= colIndex + 1) {
                // Es existiert noch ein Menüpunkt
                $($divRow.children().get(colIndex)).find(".search-option").focus();
            } else {
                // Springe in das Menü auf der nächsten Seite, falls eines existiert
                $liMenu = $divRow.parents("li").first();
                $liMenuNext = $liMenu.next();

                if ($liMenu.length >= 1) {
                    // Ein weiteres Menü existiert
                    $liMenu.removeClass("open").removeClass("active");
                    $liMenuNext.addClass("open").addClass("active");
                    $liMenuNext.find(".search-option:first").focus();
                }

            }

            // Verhindere Scrolling
            return false;
        }

        if (e.which == 40) {

            // Pfeil unten
            // Springe in nächste Zeile bei gleichem Index
            $divRowNext = $(this).parents(".row").first().next();

            if ($divRowNext.length > 0) {

                if ($divRowNext.children().length >= colIndex) {
                    // Genügende Kinder zur Auswahl vorhanden
                    divColNext = $divRowNext.children().get(colIndex - 1);
                } else {
                    // Wähle letztes Element
                    divColNext = $divRowNext.children().last();
                }

                $(divColNext).find(".search-option").focus();
            }

            // Verhindere Scrolling
            return false;
        }
        if (e.which == 38) {

            // Pfeil nach oben
            // Springe in vorherige Zeile bei gleichem Index
            $divRowNext = $(this).parents(".row").first().prev();

            if ($divRowNext.length > 0) {

                if ($divRowNext.children().length >= colIndex) {
                    // Genügende Kinder zur Auswahl vorhanden
                    divColNext = $divRowNext.children().get(colIndex - 1);
                } else {
                    // Wähle letztes Element
                    divColNext = $divRowNext.children().last();
                }

                $(divColNext).find(".search-option").focus();
            }

            // Verhindere Scrolling
            return false;
        }
    });

    // --------------------------------------------------------
    // Bootstrap Select Plugin
    // --------------------------------------------------------
    $('.selectpicker').filter(':not(.selectpicker_ajaxtoken)').selectpicker({
        style: 'dropdown btn btn-icon dropdown-toggle',
        size: 10,
        dropupAuto: false
    });
    $('.selectpicker').filter(':not(.selectpicker_ajaxtoken)').addClass(
        'selectpicker_ajaxtoken');

    // --------------------------------------------------------
    // Panels
    // --------------------------------------------------------
    var $panels = $(".panel-collapse[id$='PanelCollapse']").filter(':not(.panel_ajaxtoken)');
    $panels.on('hidden.bs.collapse', function (e) {
        var $panel = $(this).parents('.panel').first();

        // Setze Wert in verstecktes Eingabefeld
        var $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('false');
        e.stopPropagation();
    });
    $panels.on('shown.bs.collapse', function (e) {
        var $panel = $(this).parents('.panel').first();

        // Setze Wert in verstecktes Eingabefeld
        var $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('true');
        e.stopPropagation();
    });
    $panels.addClass('panel_ajaxtoken');

    // --------------------------------------------------------
    // Modale Dialoge
    // --------------------------------------------------------
    // Zeige modale Dialoge, wenn vorhanden
    var $modalDialogs = $('#modal-add-personal').filter(':not(.modal_ajaxtoken)');
    $modalDialogs.modal('show');
    $modalDialogs.addClass('modal_ajaxtoken');

    // brandest: DSD-1467 Hintergrund bei modalem Dialog wird dunkler
    // Bei Aktionen in modalen Dialogen, die mittels AJAX das Formluar neu laden lassen, wird auch der modale Dialog neu angezeigt
    // und dadurch erneut ein "modal-backdrop" hinzugefügt - diese überlagern sich. Durch das vorherige Entfernen wird sichergestellt,
    // das maximal ein backdrop vorhanden ist.
    var $modalVisible = $('.modal-dialog').is(':visible');
    $($(".modal-backdrop").get().reverse()).each(function (index, element) {
        // Entfernen wenn ein .modal-backdrop vorhanden ist, obwohl es keinen modalen Dialog gibt (passiert in edge-cases).
        // Ansonsten mit get().reverse() entfernen wir die älteste .modal-backdrops, da nur der neuste mit dem button-event verbunden ist
        if (!$modalVisible || index > 0) {
            $(element).remove();
        }
    });

    // --------------------------------------------------------
    // FocusOnload
    // Initial das linke obere Element des Inhaltsbereichs auswählen.
    // Das zu fokussierende Element kann durch das Tag isy:focusOnload
    // überschrieben bzw. deaktiviert werden. Weiterhin kann erzwungen werden
    // (Force), dass das Element fokussiert wird.
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
                    // Default-Wert nutzen
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
                    // Default-Wert nutzen
                    $('#modalDialogPlaceholderForm').find('input:not([type=hidden]), a:not([tabindex=-1]), button').first().focus();
                } else {
                    $('#modalDialogPlaceholderForm').find($('#modalDialogPlaceholderForm').find(focusOnloadElement).val()).first().focus();
                }
            }
        }
    }

    // --------------------------------------------------------
    // Hilfsfunktionen für Data-Tables (Klickbereich erweitern)
    // --------------------------------------------------------
    var $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) Klickbereich der Headerspalten erweitern
    $rfDataTables.find('th.sortable').click(function (event) {
        var $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    // Funktion, um ausgewählte Zeilen zu markieren
    var formatRowsFunction = function ($trs, $tr, selectionMode) {
        // moossenm: Klasse row-selection hinzugefuegt um Zeilenauswahl-Checkbox von anderen zu unterscheiden.
        var $input = $tr.find("td div.row-selection .checkbox label input");
        if ($input.is(":checked")) {
            $tr.addClass("active");
            // Falls der Selection Mode "single" ist, dann müssen jetzt alle anderen Input Felder deaktiviert werden.
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
    // (2) Klickbereich für die Auswahl von Zeilen erweitern / Doppelklick setzen / Selection Mode initialisieren
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

                        // Setzen der Auswahl
                        $rfDataTable.find("[id$='rfDataTableDoubleClickSelectedRow']").val($row.attr('id'));

                        // Klicken des versteckten Buttons
                        $rfDataTable.parent().find("[id$='rfDataTableDoubleClickButton']").click();
                    }
                };
            }

            multiClick($row, functionSingleClick, functionDoubleClick, 200, clicks, timer);

        });

        // Klick Event auf Checkboxen registrieren
        $rows.find("td .checkbox label input").click(function () {
            formatRowsFunction($rows, $(this).parents("tr").first(), selectionMode);
        });

        // Initial die Markierungen setzen
        if (selectActive) {
            $rows.each(function () {
                formatRowsFunction($rows, $(this), selectionMode);
            });
        }
    });
    // (3) 'Alle Auswählen' Checkbox registrieren
    var selectAllFunction = function ($selectAllCheckbox, $rfDataTable) {
        //Auf jeden Fall erst einmal den Zustand 'teilweise' entfernen.
        $selectAllCheckbox.prop("indeterminate", false);

        if ($selectAllCheckbox.is(":checked")) {
            // Transition zu unchecked
            $rfDataTable.find("tbody").first().find(".checkbox input").prop("checked", false);
        } else {
            // Transition zu checked
            $rfDataTable.find("tbody").first().find(".checkbox input").prop("checked", true);
        }
        // Zeilen bei Bedarf selecktieren
        var selectionMode = $rfDataTable.find("[id$='rfDataTableSelectionMode']").first().val();
        var $rows = $rfDataTable.find("tbody tr");
        $rows.each(function () {
            // moossenm: DSD-509 - 16.06.2015
            // Fehlenden Parameter rows und selectionMode hinzugefügt, jetzt werden auch die ausgewählte Zeilen hervorgehoben
            formatRowsFunction($rows, $(this), selectionMode);
        });
    };
    // (4) Show-/Hide-Detail-Logik registrieren
    var showDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        var $this = $(this);
        var $tr = $this.parents('tr');
        var $table = $this.parents("table.CLIENT.rf-data-table");
        var allowMultiple = $table.find("input[id$='rfDataTableDetailMode']").val() == 'multiple';
        if (!allowMultiple) {
            // Alle Detailzeilen ausblenden
            $table.find("tr[id*='detail-']").addClass('hidden');
            // Eventhandler, Tooltip, ID für hideDetail-Buttons wechseln
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
        // Eventhandler wechseln
        $this.off('click.showdetail');
        $this.on('click.hidedetail', hideDetail);
        // Lazy-Loading
        setTimeout(function () {
            // Der LazyLoad darf nicht direkt getriggert werden
            // Bilder in der Detailansicht sind erst NACH der click-Funktion "visible"
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
        $table.find('tbody div.detailview-actions button').removeAttr('onclick', '');
        var $showDetail = $table.find('div.detailview-actions button[id*=showDetail]');
        $showDetail.on('click.showdetail', showDetail);
        var $hideDetail = $table.find('div.detailview-actions button[id*=hideDetail]');
        $hideDetail.on('click.hidedetail', hideDetail);
        // =============== ENDE DETAILVIEW ===================== //
    });
    // (5) JS Sortierung aktivieren
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
                    $thLink.removeAttr("onclick");
                    $thLink.unbind("click");
                    $thLink.click(function (event) {
                        event.preventDefault();

                        // Details vor Sortierung speichern
                        var $details = $rfDataTable.find("tbody .details-preview");
                        $details.remove();

                        // Neu Sortierattribute und Richtung ermitteln
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


                        // Sortierung
                        window[$sortFunction.val()]($rfDataTable, $th, index, newSortDirection);

                        // Sortierung speichern
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

                        //     Details nach Sortierung wieder zuordnen
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

    //(6) Den Zustand der 'Alle Auswählen' Checkbox immer korrekt setzen.
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

        //Click auf der Tri-State-Checkbox registrieren.
        $selectAllCheckbox.parent().find("span").click(function () {
            selectAllFunction($selectAllCheckbox, $rfDataTable);
        });

        //Click auf den restlichen Checkboxes registrieren.
        var $checkboxes = $rfDataTable.find("tbody").first().find(".checkbox input");
        $checkboxes.each(function () {
            $(this).click(function () {
                tristateBerechnen($checkboxes, $selectAllCheckbox, $rfDataTable);
            });
        });

        //Den Zustand einmal initial berechnen. Sonst geht der Zustand u.U. bei einem Request an den Server verloren.
        tristateBerechnen($checkboxes, $selectAllCheckbox, $rfDataTable);

    });


    $rfDataTables.addClass('rf-data-table_ajaxtoken');

    // --------------------------------------------------------
    // Popovers und Tooltips
    // --------------------------------------------------------
    $('.rf-popover').filter(':not(.rf-popover_ajaxtoken)').popover({animation: false});
    // Popovers, die nach rechts rausragen, werden weiter nach links verschoben
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
    // Maginific Popup (Plugin für Bilder-Popups)
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
            // in Version 1.8. gibt es kein Attribut componentButtonOnly mehr
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
            function () {// Öffnen eines Datepickers
                var dateReg = /^\d{2}[.]\d{2}[.]\d{4}$/;
                var inputField = $(this).prev();
                var date = inputField.val().split('.');

                // eleminiere die Unterstrich-Platzhalterzeichen
                var placeholderReg = /_/gi;
                date[0] = date[0].replace(placeholderReg, "");
                if (date[0] === "99") {
                    // Secret-Code: 99 = setze Fokus des Datepickers auf das aktuelle Datum
                    var dateString = currentDateAsString();
                    $(this).parent().datepicker('setDate', dateString);
                    $(this).parent().datepicker('update');
                } else {
                    // uebernehme das manuell eingegebene Datum als Datumswert für den Datepicker
                    $(this).parent().datepicker();
                    $(this).parent().datepicker('update');
                }
            });

        //Lese den Grenzwert zum Vervollständigen von zweistelligen Jahreszahlen aus. Wird weiter unten verwendet.
        var zweistelligeJahreszahlenErgaenzenGrenze = $('#formDateJahresZahlenErgaenzenGrenze').val();
        var $datumInputFeld = $(this).find('input');
        $datumInputFeld.focusout(function (event) {
            if (zweistelligeJahreszahlenErgaenzenGrenze !== "-1") {
                datumErgaenzen($datumInputFeld, zweistelligeJahreszahlenErgaenzenGrenze);
            }

            // Magic Number: setze Datum auf Tagesdatum
            var date = $datumInputFeld.val().split('.');
            if (date[0] === "99") {
                $datumInputFeld.val(currentDateAsString());
            }
        });
    });

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });


    // --------------------------------------------------------
    // Input Masks
    // --------------------------------------------------------
    // Alle Input Elemente, welche ein Attribut 'inputmask' besitzen
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

        // Maximale Länge wird über Maske abgedeckt, ansonsten würde Strg+V nicht funktionieren.
        $inputMask.removeAttr('maxlength');
    });

    $inputMasks.bind('keydown keypress', function (e) {
        var $inputMask = $(this);

        if (e.key === 'Enter') {
            // Alle Platzhalter-Zeichen entfernen
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
     * Die Funktion sorgt dafür, dass im Listpickerfeld der Schlüssel aufgelöst wird. Dazu wird in der
     * zugrunde liegenden Tabelle der Wert zu dem übergebenen Spalten-Index extrahiert und im Listpickerfeld angefügt.
     * @param listpickerfield Das Listpickerfeld.
     * @param indexSpalteSchluesselWert Der Index der Headerspalte, deren Inhalt im Eingabefeld ergänzt werden soll.
     */
    var listpickerLoeseSchluesselAuf = function (listpickerfield, indexSpalteSchluesselWert) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // verhindere, dass Ziffern aus dem Wert im Feld verbleiben
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
                    if ($(".ajax-status span").text() !== 'begin') { // Verhindere Kollision mit anderen AJAX-Requests
                        $filter.val(listpickerfield.val());
                        $filter.change();
                    }
                }
            }
        }
    };

    /**
     * Die Funktion maskiert das Listpickerfeld, sofern eine Maske definiert ist.
     * @param listpickerfield Das Listpickerfeld.
     */
    var listpickerMaskieren = function (listpickerfield) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // verhindere, dass Ziffern aus dem Wert im Feld verbleiben
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
        //Finde das Hidden-Input, in dem hinterlegt ist, welche Spalte jeweils den Wert zum Schlüssel enthält.
        var listpickerSchluesselwertSpalte = $listpicker.find("[id$='inputComplement']").val();
        if (typeof(listpickerAjaxFormId) != "undefined") {
            $listpickerAjaxForm = $("form[id$='" + listpickerAjaxFormId + "']");
        }

        // Standard-Schließ Event verhindern
        $listpicker.on('hide.bs.dropdown', function (e) {
            e.preventDefault();
        });

        // Falls es sich um eine AJAX Listbox handelt, müssen hier die korrekten Werte geladen werden
        if ($listpickerAjaxForm !== null) {
            $listpickerContent.find("tbody").replaceWith($listpickerAjaxForm.find("tbody").clone());
        }

        // Nachdem das Dropdown geöffnet wurde
        $listpicker.on('shown.bs.dropdown', function (e) {
            // Alle Charpicker schließen
            var $charpickers = $(".special-char-picker-widget");
            $charpickers.each(function () {
                $(this).hide();
            });

            // Wenn ein Charpicker geöffnet ist und man neuen Listpicker öffnet, wollen wir veraltetes Fokus löschen
            var $active_charpickers_field = $(".charpicker-focused");
            $active_charpickers_field.each(function () {
                $(this).focusout();
                $(this).removeClass("charpicker-focused");
            });

            // Alle Listpicker außer den aktuellen schließen
            $listpickerContainer.not($listpicker).removeClass('open');

            // Die minimale Größe setzen
            if (parseInt($listpickerMinWidth.val()) > $listpickerField.outerWidth()) {
                $listpickerContent.css("min-width", parseInt($listpickerMinWidth.val()) + "px");
            } else {
                $listpickerContent.css("min-width", $listpickerField.outerWidth() + "px");
            }

            // Wenn das Element angezeigt wird, dann soll das Filterfeld selektiert werden
            $listpickerFilter.focus();

            // Aktuelle Auswahl als aktiv markieren
            if ($listpickerField.val() !== '') {
                var id;
                //Falls für das Feld bereits der Schlüssel aufgelöst wurde, müssen wir den Schlüssel isolieren.
                if ($listpickerField.val().indexOf(" - ") >= 0) {
                    id = $listpickerField.val().substring(0, $listpickerField.val().indexOf(" - "));
                } else {
                    id = $listpickerField.val();
                }
                $listpickerContent.find("tbody tr[id='" + id + "']").addClass("active");
                $listpickerContent.find("tbody tr").not("[id='" + id + "']").removeClass("active");

                // Cursor im Listpickerwidget auf aktive Zeile setzen
                var $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");
                scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow);
            } else {
                // bei leerem bzw. geloeschten $listpickerField sind ehem. active-Eintraege zu loeschen
                $listpickerContent.find("tbody tr[id=").removeClass("active");
            }

        });


        // Klicks abfangen und Feld ggf. schließen
        $(document).click(function (e) {

            var $target = $(e.target);

            if ($listpickerContent.has($target).length <= 0) {
                // Der Klick ist außerhalb des Dropdowns, schliesse Picker
                $listpicker.removeClass('open');
            } else {
                // Der Klick ist innerhalb des Dropdowns, daher sollte die aktuelle Zelle ausgewählt werden
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

        // Registriere Eingabefeld
        $listpickerFilter.bind('keypress', function (e) {
            if (!e.ctrlKey && !e.altKey && !e.shiftKey && e.keyCode == 13) {
                // Kein Aufsteigen im DOM-Tree: Dieser Tastendruck betrifft nur den Filter
                e.stopPropagation();

                // Beim Drücken von Enter wird der aktive Eintrag ausgewählt
                var $row = $listpickerContent.find("tbody tr:visible.active").first();
                // Wenn es keinen aktiven Eintrag gibt, dann wird der oberste Eintrag ausgewählt
                if ($row.length <= 0) {
                    $row = $listpickerContent.find("tbody tr:visible").first();
                }

                $listpickerField.val($row.attr('id'));
                $listpickerField.focus();
                $listpicker.removeClass('open');
                e.preventDefault();
            }
        });

        // Sorge dafür, dass bei einer Veränderung des Listpickerfilters auch immer das Change-Event gefeuert wird
        $listpickerFilter.bind('keyup.ensureChange', function (event) {
            var keyCode = event.keyCode;
            var valid = inputChangingKeycode(keyCode); // ändert der Tastendruck den Inhalt des Filters?
            if (valid) {
                $(this).change();
            }
        });

		// Reagiere auf Eingaben im Eingabefeld (auch bei AJAX-Widgets).
        //Für die Picker, die per Servlet filtern, ist dies nicht nötig!
        if (!$listpickerFilter.parent().hasClass('servlet')) {
			$listpickerFilter
					.bind(
							'keydown.ajaxFilter keypress.ajaxFilter',
							function() {

								setTimeout(
										function() {

											// Filter
											var $rows = $listpickerContent
													.find(".rf-listpicker-table tbody tr");

											var filterFunction = function($row,
													inverse) {

												var $tdsAfterFilter = $row
														.find('td')
														.filter(
																function() {
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
													.filter(function() {
														return filterFunction(
																$(this), false);
													});
											var $unfilteredRows = $rows
													.filter(function() {
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

            // aktuell aktives sichtbares Element
            var $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");

            // Pfeil nach unten
            if (keyPressed == 40) {
                // Wenn bisher kein Element aktiv, dann erstes Element wählen
                if ($activeVisibleRow.length <= 0 && $listpickerContent.find("tbody tr:visible").first().length > 0) {
                    $listpickerContent.find("tbody tr:visible").first().addClass("active");
                }
                // Sonst nächstes Element wählen
                else if ($activeVisibleRow.nextAll('tr:visible:first[id]').length > 0) {
                    $activeVisibleRow.removeClass("active");
                    $activeVisibleRow.nextAll('tr:visible:first[id]').addClass("active");
                    scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow.nextAll('tr:visible:first[id]'));
                }

                // Pfeil nach oben
            } else if (keyPressed == 38) {
                // Wenn es ein vorheriges Element gibt, dann dieses wählen
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

        // Funktion, die die Auswahlliste eines List-Pickers ausklappt, falls die Taste
        // "Pfeil-nach-unten + alt" (Event "keydown", Key-Code: 40) betaetigt wurde.
        $listpickerField.bind('keydown', 'alt+down', function (e) {
            // Oeffne die Auswahlliste.
            // Die Auswahlliste liegt auf einer Ebene mit dem Input-Feld
            $(this).parent().find('.listpicker-button').click();
        });

        //Bei Fokusverlust soll der Schlüssel aufgelöst werden. Das Feature gilt nur als aktiviert, wenn
        //ein größerer Wert als 1 definiert ist, denn ansonsten macht es keinen Sinn.
        if (listpickerSchluesselwertSpalte > 1) {
            $listpickerField.focusout(function () {
                listpickerLoeseSchluesselAuf($listpickerField, listpickerSchluesselwertSpalte);
            });

            //Wenn das Feld den Fokus erhält, müssen wir maskieren, denn sonst wäre der
            //aufgelöste Schlüssel immer noch im Feld und der Anwender müsste das zuerst manuell entfernen.
            $listpickerField.focus(function () {
                listpickerMaskieren($listpickerField);
            });

            //Einmalig lösen wir direkt initial den Schlüssel auf. Damit das Feld z.B. nach einem Request
            //an den Server noch korrekt aussieht.
            listpickerLoeseSchluesselAuf($listpickerField, listpickerSchluesselwertSpalte);
        }
    });

    $listpickerContainer.addClass('rf-listpicker_ajaxtoken');

    // Scrollt innerhalb eines Elements zu einem bestimmten DIV
    function scroll_to(element, div) {
        $(element).animate({
            scrollTop: $(div).parent().scrollTop() + $(div).offset().top - $(div).parent().offset().top
        }, 0);
    }

    // Alle Listpicker schließen, wenn ein Selectpicker geöffnet wird
    // TODO: Lösung ist aktuell nicht generisch. Das sollte verbessert werden.
    var $buttonSelectpicker = $('button.selectpicker');
    $buttonSelectpicker.click(function (event) {
        $listpickerContainer.removeClass('open');
    });

    // --------------------------------------------------------
    // Tabs
    // --------------------------------------------------------
    // Vorgeladene Tabs steuern
    $('.isy-tab').each(function () {
        var $isyTab = $(this);

        // TabAutoscroll: Ist in tabGroup ein Tab-Inhaltsbereich-HochScrollen gewünscht?
        var $tabHochScrollen = false;
        if ($isyTab.hasClass('tabHochScrollen')) {
            $tabHochScrollen = true;
        }

        $isyTab.children().each(function () {
            var $li = $(this);
            var $liLink = $(this).find("a");

            if ($li.hasClass('skipAction')) {
                $liLink.unbind("click");
                $liLink.removeAttr("onclick");
            }

            $liLink.click(function (event) {
                if ($li.hasClass('skipAction')) {
                    event.preventDefault();
                }

                // Aktuelles Tab entfernen
                var liIdAlt = $isyTab.find(".active").attr('id');
                $isyTab.find(".active").removeClass("active");
                $isyTab.next().find("#" + liIdAlt).removeClass("active");

                // Tab aktivieren
                var liIdNeu = $li.attr('id');
                $li.addClass("active");
                //$isyTab.next().find("#" + liIdNeu).addClass("active");

                var aktiverTab = $isyTab.next().find("#" + liIdNeu);
                aktiverTab.addClass("active");

                // Tab-Autoscroll unterstützen
                if ($tabHochScrollen) {
                    $('html, body').animate({
                        scrollTop: $(aktiverTab).offset().top - 50
                    }, 'slow');
                }

                // Zustand merken
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

        // Finde klickbares Element in der ButtonInjectPostGroup
        var $actualButton = $group.find(":nth-child(4)");

        // Finde Button für POST-Aktion
        var $postButton = $("[id$='" + $group.find("[id$='postButton']").val() + "']");

        // Finde Feld für posted
        var $posted = $group.find("[id$='posted']");

        // Finde Feld für continue
        var $continue = $group.find("[id$='continue']");


        if ($posted.attr("value") === 'true') {
            // Die POST-Aktion wurde zuvor erfolgreich beendet. Setze das Flag zurück.
            $posted.attr("value", "false");

            $actualButton.unbind("click.postInject");
            // Falls <a>-Tag: Gib onclick zurück
            $actualButton.attr("onclick", $actualButton.attr("onclickStandby"));

            // Tatsächlich geklickt wird nur, wenn das continue-Flag gesetzt ist
            if ($continue.attr("value") === 'true') {
                $actualButton.click();
            }
        }

        // Events binden, falls noch nicht geschehen
        if (!$group.hasClass(".isyfact-buttonInjectPostGroup_ajaxtoken")) {
            // <a>-Tag: Entferne onclick
            $actualButton.attr("onclickStandby", $actualButton.attr("onclick"));
            $actualButton.removeAttr("onclick");

            // <input type=submit ...> Verhindere Übermittlung
            // Überschreibe Buttonaktion
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
    var $forms = $("form");
    $forms.each(function () {
        var $form = $(this);
        $form.unbind("keypress");
        var $defaultButton = $form.find("[id*='" + $form.find("[id$='defaultButtonID']").val() + "']");
        if ($defaultButton.length > 0) {
            // Ursprüngliches Bind deaktiveieren
            $form.unbind("keypress");
            // Das Form enthält einen DefaultButton
            $form.bind("keypress", function (event) {
                if (event.keyCode == 13) {
                    var $source = $(document.activeElement);
                    if (!$source.is("[type='submit']") && !$source.is("a") && !$source.is("button") && !$source.hasClass("charpicker")) {
                        // Kein Link, Button, Charpicker oder anderes Submit-Element fokussiert, das eine eigene sinnvolle Aktion bei Enter hat
                        // Betätige den Default-Button
                        $defaultButton.first().click();
                        // Verhindere normale Default-Aktion, das wäre ein Klicken des ersten Buttons mit type="submit"
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
        .addClass('datatable-client-init') // als initialisiert markieren
        .each(function () {
            var $table = $(this);

            // =============== START FILTER-ZEILE ===================== //
            var timeId = 0;
            // replace buttons so that no server action is called
            var $filterRow = $table.find("thead tr.filter-row");
            $filterRow
                .find("button.btn:not(.selectpicker)")
                .replaceWith("<button type='button' class='btn hidden' />");

            // clear all filter, vom replaceWith fixen
            $filterRow.find("td.table-clear-all-filter button")
                .removeClass("hidden")
                .addClass("table-clear-all-filter icon btn-icon btn-icon-small icon-cancel");

            // Event-Auslöser für Filter
            $filterRow
                .on("click", "button.btn:not('.selectpicker,.table-clear-all-filter')", function (e) {
                    // um das stauen der Evente zu verhindern hat man einen 100ms puffern
                    if (timeId) {
                        clearTimeout(timeId);
                    }
                    timeId = setTimeout(function () {
                        doItAll();
                    }, 100);
                });

            // Event-Auslöser für alle Filter zurücksetzen
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

            var $itemsWithDetails = $table.find("tbody tr");
            var $allFilters = $filterRow.find('td');
            var filterSingle = function ($td) {
                // den zu filternenden Wert
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
                    // kein Filter
                    return;
                }
                // die zu filternende Spalte
                var filterTd = 'td:nth-child(' + ($td.index() + 1) + ')';
                var lastMatched = false; // hilfvariable zum einblenden der Details
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
                    // Wert ermitteln
                    var $td = $item.find(filterTd);
                    var val = $td.data('filter');
                    if (val === undefined || val === '') {
                        val = $td.text();
                    }
                    val = val.trim().toLowerCase();
                    // filtern
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
            // initialisierung
            $table.removeClass('datatable-filterrow-init');
            refreshDatatableFilterRow(); //
            // =============== ENDE FILTER-ZEILE ===================== //

            // =============== START PAGINIERUNG ===================== //
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
                // alle Tabelleeinträge mit details-preview finden und verstecken
                $itemsWithDetails.hide();
                var itemCount = getItemCount();
                // eingrenzen
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
                // aktuelle Seite anzeigen
                pageItems.filter(':not(.filtered)').show();
                // gibt true zurück falls die letzte Seite angezeigt worden ist
                return isLastPage;
            };
            // ...mehr Anzeigen... Variante
            $pageControl.filter('.SIMPLE').find('li.page-next').click(function (e) {
                setCurrentPage(getCurrentPage() + 1);
                if (renderPage()) {
                    $(this).hide();
                } else {
                    setupLi($(this), getCurrentPage() + 1, false);
                }
                // default action nicht ausführen
                e.stopPropagation();
                e.preventDefault();
                return false;
            }).each(function () {
                // buttons replace
                $pageControl
                    .find('li.page-next')
                    .find('button')
                    .replaceWith("<button type='button' class='btn' />");

                // Initialisierung
                setCurrentPage(getCurrentPage() - 1);
                $(this).click();
            });
            // Normaler Paginator Variante
            $pageControl.filter('.NORMAL').each(function () {
                // installieren vom EventHandler
                $pageControl.find('ul.pagination').on('click', 'li', function (e, skipDisabledTest) {
                    if (!skipDisabledTest && $(this).find('button').prop('disabled')) {
                        return false;
                    }
                    var page = $(this).data('page');
                    setCurrentPage(page);
                    doPagination();
                    renderPage();
                });
                // initialisieren vom page Knopf-Master
                $pageControl.find('li.page-number:not(.master)').remove();

                // buttons replace
                $pageControl
                    .find('li.page-first,li.page-pre,li.page-next,li.page-last')
                    .find('button')
                    .replaceWith("<button type='button' class='btn' />");
            });

            // =============== ENDE PAGINIERUNG ===================== //

            // =============== START SORTIERUNG ===================== //
            $table.find("thead th.sortable").each(function () {
                var $th = $(this);
                $th.find('a').each(function () {
                    var $a = $(this);
                    $a.removeAttr("onclick");
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
                        // nummernvergleich
                        return comp(+v1, +v2);
                    }
                    // zeichenkettevergleich
                    return comp(v1, v2);
                });
                // jetzt sind die tabelleneinträge ohne die Details sortiert.
                // wir müssen noch die details zuordnen
                var newItems = [];
                $.each(items, function (i, item) {
                    var $item = $(item);
                    var index = $item.index(); // index im DOM vor der Sortierung
                    newItems.push($item);
                    // gucke ob Detail sichtbar
                    var detail = $itemsWithDetails.eq(index).next();
                    if (detail.hasClass('details-preview')) {
                        // Detailzeile mitnehmen
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
                var sortClass = 'sort-up'; // standardmäßig aufsteigend sortiert
                var thisSortProperty = $th.data("sortattribute");
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
                // wenn anwendungsspezifische Sortierfunktion existiert, verwende diese
                if (typeof window.sortDataTable === 'function') {
                    window.sortDataTable($table, $th, $itemsWithDetails);
                    $itemsWithDetails = $table.find("tbody tr");
                }
                else {
                    sort($th);
                }

                if (!cumulative) {
                    setCurrentPage(1);
                }
                renderPage();
            });
            // =============== ENDE SORTIERUNG ===================== //

            // Hauptfunktion
            var doItAll = function (init) {
                filterAll(init);
                // wenn anwendungsspezifische Sortierfunktion existiert, verwende diese
                if (typeof window.sortDataTable === 'function') {
                    window.sortDataTable($table, $.merge($table.find('thead th.sorted'), $table.parent().prev().find('thead th.sorted')).eq(0), $itemsWithDetails);
                    $itemsWithDetails = $table.find("tbody tr");
                }
                else {
                    sort($.merge($table.find('thead th.sorted'), $table.parent().prev().find('thead th.sorted')).eq(0));
                }
                renderPage();
            };
            // initialisierung
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
        .addClass('toggle-filter-ajax') // als initialisiert markieren
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

    // Der Listpicker Filter sendet das Event
    var $listpickerFilter = $(callback.source).first();
    var $listpickerContent = $listpickerFilter.parents(".listpicker-container").first().find(".listpicker-content");
    var $ajaxSpinner = $listpickerFilter.parent().parent().parent().parent().find('.listpicker-ajax-spinner');


    if (callback.status === 'begin' && $listpickerFilter.is($(document.activeElement))) {
        // UI-Block des Listpickers
        $ajaxSpinner.css("position", $listpickerContent.css("position"));
        $ajaxSpinner.css("top", $listpickerContent.css("top"));
        $ajaxSpinner.css("left", $listpickerContent.css("left"));
        $ajaxSpinner.css("height", $listpickerContent.css("height"));
        $ajaxSpinner.css("width", $listpickerContent.css("width"));
        $ajaxSpinner.css("margin-top", $listpickerContent.css("margin-top"));
        $ajaxSpinner.css("display", "block");

        // Blockiere Tastatureingaben
        $listpickerFilter.bind("keydown.prevent keypress.prevent", function (event) {
            event.preventDefault();
        });

    }

    if (callback.status === 'complete') {
        // Entferne UI-Block des Listpickers
        $ajaxSpinner.css("display", "none");

        // Deblockiere Tastatureingaben
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
 * Prueft, ob die gedrücke Taste den Inhalt eines Textfeldes veraendern kann
 */
function inputChangingKeycode(keyCode) {
    'use strict';
    // keyCodes, die die Eingabe nicht verändern. Gilt nur für IE und Firefox!
    // Hiermit werden bekannte Steuerzeichen auf der Tastatur ignoriert
    // invalide Keycodes: 0 ODER 9-13 ODER 16-20 ODER 27 ODER 33-45 ODER 91-93 ODER 112-123 ODER 144 ODER 145 ODER 181-183
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
 * Blockiert einen einzelnen Button beim Klick. Verhindert Doppelklick.
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
 * Lädt Elemente nach, falls notwendig.
 */
function lazyLoad() {
    'use strict';

    // Bilder
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
 * formatiert einen Stundensatz auf zwei Nachkommastellen
 *
 * Das mit dem parseFloat funktioniert, da die Komponente formCurrencyInput
 * bereits verhindert, dass Buchstaben eingegeben werden können.
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
 * Formatiert eine Numerische-/Fliesskomma Zahl
 *
 * parseFloat funktioniert, da die Komponente formNumericInput
 * mit "onkeyup" bereits verhindert, dass Buchstaben eingegeben werden können.
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
 * formatiert den Eingabewert auf die angegebene Anzahl Nachkommastellen ohne Tausenderpunkte (z.B. xxxxx,xx)
 * Wird von der formCurrencyInput- und der formNumericinput-Komponente aufgerufen.
 */
function formatiereInput(input, dezimalstellen) {
    'use strict';
    var value = input.split(".").join("");
    value = value.replace(',', '.');
    var tmp = parseFloat(value).toFixed(dezimalstellen);
    return tmp.replace('.', ',');
}

/**
 * kürzt den Eingabewert auf die angegebene Länge
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
 * Setzt die Tausenderpunkte bei Geldbeträgen
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
 * Setzt die Tausenderpunkte bei Numerischen- und Fliesskommazahlen
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
 * Löscht alle Zeichen außer Zahlen und Kommas aus der Einabe eines Textfeldes.
 * Wird von der formCurrencyInput- und formNumericInput-Komponente beim onkeyup-Event aufgerufen.
 *
 * @param ref -
 *            Referenz auf das Textfeld, in dem die Ersetzung vorgenommen werden
 *            soll.
 */
function deleteNonDigitCharacters(ref) {
    'use strict';
    if (ref.value !== "") {
        // Speichert die aktuelle Cursor-Position in Variablen
        // wird für die Browser-Kompatibilität von IE und Chrome benötigt
        var start = ref.selectionStart;
        var end = ref.selectionEnd;

        // länge des Textes wird gespeichert, wird später benötigt um
        // festzustellen wie viele Zeichen entfernt wurden und um wie viele
        // Zeichen der Cursor verschoben werden muss.
        var length = ref.value.length;

        // entfernt alle Zeichen ausser Zahlen und Komma aus der Eingabe.
        // Achtung auch Tausender-Punkte werden entfernt
        ref.value = ref.value.replace(/[^\d,.]/g, '');

        // Prüft ob Zeichen entfernt wurden und verschiebt den Cursor
        // entsprechend - Wird für IE und Chrome benötigt, bei FF reicht das
        // ersetzen des Textes aus.
        var lengthAfterReplace = ref.value.length;
        if (length > lengthAfterReplace) {
            start = start - (length - lengthAfterReplace);
            end = end - (length - lengthAfterReplace);
        }

        // setzt die Cursor-Position
        ref.setSelectionRange(start, end);
    }
}

/**
 * JavaScript Typkonvertierung String = "true" zu Boolean = true
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

function refreshDatatableFilterRow() {
    "use strict";
    $("table.rf-data-table:not('datatable-filterrow-init')")
        .addClass('datatable-filterrow-init') // als initialisiert markieren
        .each(function () {
            var $table = $(this);
            // validiere Anzahl der Spalten als Hint für den Entwickler
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
                // setzt alle filter zurück
                $filterRow.find('input.table-filter').val('').data('property', '');
                // setzt alle dropdown-Filter zurück
                $filterRow.find('select.filter-dropdown').data('property', '').selectpicker('val', '');
                // versteckt alle clear-filter icons
                $filterRow.find('a.table-clear-filter').hide();
                // clear-all-filter verstecken
                $clearAllFilterIcon.hide();
            };
            var hasFilter = function () {
                // mindestens einen Filterfeld hat einen Wert oder eine Dropdownliste ist ausgewählt
                return ($filterRow.find('input.table-filter, select.filter-dropdown').filter(function () {
                    return !!this.value;
                }).length);
            };
            var checkClearAllFilter = function () {
                if (hasFilter()) {
                    // all-clear-filter icon anzeigen
                    $clearAllFilterIcon.show();
                } else {
                    // all-clear-filter icon verstecken
                    $clearAllFilterIcon.hide();
                }
            };
            // ausbessern der clear-all-filter Icons, nötig wg. Limitierungen der Implementierung von isy:buttonIcon
            $clearAllFilterIcon
                .removeClass("btn-icon-round btn-icon-round-small") // schlechte buttonIcon klassen entfernen
                .addClass("table-clear-all-filter btn-icon btn-icon-small") // richtige icon klassen hinzufügen
                // click auf clear-all-filter Icon: setzt alle Filter zurück, versteckt alle clear-filter und clear-all-filter Icons und führt die Aktion aus
                .click(function () {
                    resetFilter();
                    // default action ausführen
                    return true;
                })
                .each(function () {
                    /// initialisieren
                    checkClearAllFilter();
                });
            $filterRow.find('select.filter-dropdown')
                .change(function (e) {
                    var $this = $(this);
                    checkClearAllFilter();
                    // click auf den versteckte Knopf um die Aktion auszuführen
                    $this.parent().next().click();
                });
            // click auf clear-filter Icon: setzt Filter zurück, passt Anzeige der clear-filter und clear-all-filter Icons an und führt die Aktion aus
            $filterRow.find('a.table-clear-filter')
                .click(function () {
                    var $this = $(this);
                    // setzt filter zurück
                    $this.prev().val('');
                    // versteckt clear-filter icon und evtl. clear-all-filter Icon
                    $this.prev().trigger('blur');
                    // default Aktion unterdrücken
                    return false;
                });
            // Evente des Filter-Inputfeldes:
            // i.A. aktion ausführen wenn <enter> gedrückt, aber nur wenn der Filter geändert wurde
            $filterRow.find('input.table-filter')
                .attr("autocomplete", "off")
                .keypress(function (event) {
                    var isClient = $table.hasClass('CLIENT');
                    // falls Enter gedrückt wird
                    if (event.which == 13) {
                        // Aktion auslösen
                        if (isClient) {
                            $(this).trigger('keyup');
                        } else {
                            $(this).trigger('blur');
                        }
                        //  default-action unterdrücken
                        return false;
                    }
                    // falls Backspace gedrückt wird
                    if (event.which == 8 && isClient) {
                        $(this).trigger('keyup');
                    }
                }).keyup(function (e) {
                // nur im CLIENT Mode
                if ($table.hasClass('CLIENT')) {
                    var $this = $(this);
                    $(this).trigger('blur');
                    $(this).focus();
                }
                // i.A. aktion ausführen wenn der Filter den Fokus verliert, aber nur wenn der Filter geändert wurde
            }).blur(function () {
                var $this = $(this);
                $this.trigger('change');
                if ($this.val() != $this.data('property')) {
                    // click auf den versteckte Knopf um die Aktion auszuführen
                    $this.data('property', $this.val());
                    $this.next().next().click();
                }
                // Anzeige der clear-filter und clear-all-filter Icons anpassen
            }).change(function () {
                var $this = $(this);
                if ($this.val()) {
                    // falls neuer Wert nicht leer, clear-filter Icon anzeigen
                    $this.next().show();
                } else {
                    // falls neuer Wert leer, clear-filter Icon verstecken
                    $this.next().hide();
                }
                checkClearAllFilter();
            });
        });
}

//Die Funktion ergänzt zweistellige Jahresangaben innerhalb eines Datum-Inputfeld.
datumErgaenzen = function (inputFeld, grenze) {
    "use strict";
    //Der Grenzwert wird auf das aktuelle Jahr addiert, damit der resultierende Wert im Laufe der Jahre mitläuft.
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

//Code der das Initialisieren eines Listpickers über das Servlet anstößt
initialisierenListpickerServlet = function() {
	$listpicker = $( ".servlet.listpicker-filter" );
	$listpicker.each(function (i, listpicker) {
		registerListpickerfilter(listpicker);
	});
}


//registrieren eines Listpickers
registerListpickerfilter = function (identifier) {
	var $listpickerFilter = $(identifier);
	var listpickerFilterInput = $listpickerFilter.children()[0];
	var url = $listpickerFilter.siblings("div.rf-listpicker-table-container").find(".servletTable")[0].getAttribute("data-servleturl");
	
	//Im Folgenden werden die einzelnen Parameter, die in der URL enthalten sind encoded.
	//Es wird jeweils der Wert des Parameters encoded, nicht der Parameter selbst.
	var urlsplit=url.split("?");
	
	//Der erste Teil der URL (alles ohne Paramater) bleibt unverändert.
	var urlEncoded=urlsplit[0]+'?';
	
	
	//Splitte den zweiten Teil
	if(urlsplit.length > 1){
		var attributeGesetzt = urlsplit[1].split("&");
		attributeGesetzt.forEach(function(attribut){
			var attributSplit = attribut.split("=");
			urlEncoded=urlEncoded+attributSplit[0]+'='+encodeURIComponent(attributSplit[1])+"&";	
		});
	}
	
	//initiale Befüllung des Listpickers
	//Hier wird der eigentliche Request abgeschickt!
	$.get( urlEncoded+"filter="+encodeURIComponent(listpickerFilterInput.value)).success(function(data){createListpickerTable(data, $listpickerFilter)});
	listpickerFilterInput.dataset.oldvalue = listpickerFilterInput.value;
	
	var $listpickerContent = $listpickerFilter.parent().parent();
	var $listpickerContainer = $listpickerContent.parent();
	var $listpickerField = $listpickerContainer.find('*[id*=listpickerField]');
	
    //Hat man sich im Dropdownmenü befunden und klickt anschließend außerhalb, werden die Felder synchronisiert.
	$(listpickerFilterInput).focusout(function() {
		$listpickerFilter.parent().parent().siblings('.form-control').focusout();
	});
	    
    //Die Filtermethode, die die Liste aktualisiert
    //Zunächst deaktivieren wir den Handler für den Fall, dass er schon existiert und aktualisiert
    //werden muss. (Dies ist beispielsweise der Fall, wenn die URL per JavaScript manipuliert wurde, ohne dass die gesamte Seite neu gerendert wird.)
    //Wenn wir den Handler nicht vorher deaktivieren, bleibt die Servlet-URL effektiv unverändert und
    //der Filter funktioniert dann nicht korrekt.
    $(listpickerFilterInput).off('change keyup', servletListpickerFilterChanged);
    //Die benötigten Daten (die URL und der Filter selbst) geben wir als Data-Attribute rein.
	$(listpickerFilterInput).on('change keyup', {url: urlEncoded, listpickerfilter: $listpickerFilter}, servletListpickerFilterChanged);
}

/**
 * Die Funktion behandelt change und keyup Events für die Listpicker, die per Servlet filtern.
 * @param event Das change/keyup Event.
 */
function servletListpickerFilterChanged(event){
	event.stopImmediatePropagation();
	//Hole die benötigten Daten aus den Data-Attributen des Events (wurden im Aufruf gesetzt).
	var servletUrl = event.data.url;
	var listpickerFilter = event.data.listpickerfilter;
	$this = $(this);
	var delay = 500;
	timer = window.setTimeout(function(filter) {
		var input = $this[0];
		if (input.dataset.oldvalue == "undefined" || input.value != input.dataset.oldvalue) {
			$.get( servletUrl+"filter="+encodeURIComponent(input.value)).success(function(data){createListpickerTable(data, listpickerFilter)});
			input.dataset.oldvalue=input.value;
		}
	},delay, $this);
}

//Erstellt einen ListpickerTable anhand des responseTextes.
createListpickerTable = function (responseText, listfilter) {
	
	var $tablecontainer = $(listfilter).siblings("div.rf-listpicker-table-container");
	var $table = $tablecontainer.find(".servletTable");
	$table.empty();
	tableJson = JSON.parse(responseText);
	for(var j in tableJson.items) {
		var item = tableJson.items[j];
		var tr = $('<tr>').attr('id', item.id);
		for(var i = 0; i < item.attrs.length; i++) {
			var td = $('<td>').text(item.attrs[i].trim());
			tr.append(td);
		}
		$table.append(tr);
	}
	if(tableJson.weiterFiltern === true) {
		var tr = $('<tr>');	
		var td = $("<td>").text(tableJson.messageItem).attr('colspan', 2);
		tr.append(td);
		$table.append(tr);
	}
	$(listfilter).parent().parent().siblings('.form-control').focusout();	
}

//Bei einem Klick im Dokument, wird ein Listpicker, falls dieser geöffnet war, geschlossen und zusätzlich die 
//Focusout-Methode getriggert, um das Auflösen des Schlüssels zu bewirken.
$(document).click(function(e) {
    var $target = $(e.target);
    var $listpickerContainer = $('.listpicker-container.open');
    var $listpickerContent = $listpickerContainer.find('.listpicker-content');
    if($listpickerContent.has($target).length <= 0 && $listpickerContainer.hasClass('open')) {
    	$listpickerContainer.removeClass('open');
    	$listpickerContent.siblings('.form-control').focusout();
    	$target.focus();
    	
    } 

});
