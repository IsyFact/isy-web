$(document).ready(function () {
    'use strict';

    // Reaktion auf AJAX Requests
    // jsf = JSF JavaScript Library  von SUN Microsystems
    if (typeof (jsf) != "undefined") {
        // --------------------------------------------------------
        // Fehlerbehandlung
        // --------------------------------------------------------
        jsf.ajax
            .addOnError(function (data) {

                // Fehlernachricht bestimmen
                let errorMessage = $("[id$='ajaxErrorMessage']").val();
                const errorMessageTitle = $("[id$='ajaxErrorMessageTitle']").val();

                // In Konsole schreiben
                const error = data.description;
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
        const tag = "resizeTimer";
        const self = $(this);
        let timer = self.data(tag);
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
    // Selectpicker aktualisieren (damit bootstrap-select sie korrekt rendered)
    // --------------------------------------------------------
    $('.selectpicker').selectpicker('refresh');

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
        const $divCol = $(this).parents(".col-lg-4").first();

        // Aktuelle Zeile
        const $divRow = $divCol.parents(".row").first();

        // Nächste Elemente
        let $liMenu;
        let $liMenuNext;
        let $divRowNext;
        let divColNext;

        // Aktuellen Spaltenindex merken
        let $divColNeighbour = $divCol;
        let colIndex = 1;
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
    // Panels
    // --------------------------------------------------------
    const $panels = $(".panel-collapse[id$='PanelCollapse']").filter(':not(.panel_ajaxtoken)');
    $panels.on('hidden.bs.collapse', function (e) {
        const $panel = $(this).parents('.panel').first();

        // Setze Wert in verstecktes Eingabefeld
        const $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('false');
        e.stopPropagation();
    });
    $panels.on('shown.bs.collapse', function (e) {
        const $panel = $(this).parents('.panel').first();

        // Setze Wert in verstecktes Eingabefeld
        const $serverProperty = $panel.find("input[id$='panelCollapseAttribute']").first();
        $serverProperty.val('true');
        e.stopPropagation();
    });
    $panels.addClass('panel_ajaxtoken');

    // --------------------------------------------------------
    // Modale Dialoge
    // --------------------------------------------------------
    // Zeige modale Dialoge, wenn vorhanden
    const $modalDialogs = $('#modal-add-personal').filter(':not(.modal_ajaxtoken)');
    $modalDialogs.modal('show');
    $modalDialogs.addClass('modal_ajaxtoken');

    // brandest: DSD-1467 Hintergrund bei modalem Dialog wird dunkler
    // Bei Aktionen in modalen Dialogen, die mittels AJAX das Formluar neu laden lassen, wird auch der modale Dialog neu angezeigt
    // und dadurch erneut ein "modal-backdrop" hinzugefügt - diese überlagern sich. Durch das vorherige Entfernen wird sichergestellt,
    // das maximal ein backdrop vorhanden ist.
    const $modalVisible = $('.modal-dialog').is(':visible');
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

    const $focusOnloadActive = $("[id$='focusOnloadActive']").last();
    let $focusOnloadDeactivated;
    let $focusOnloadForce;
    const focusOnloadElement = "[id$='focusOnloadElement']";

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
    const $rfDataTables = $('.rf-data-table').filter(':not(.rf-data-table_ajaxtoken)');
    // (1) Klickbereich der Headerspalten erweitern
    $rfDataTables.find('th.sortable').click(function (event) {
        const $target = $(event.target);
        if ($target.is("th")) {
            $(this).find('a').click();
        }
    });
    // Funktion, um ausgewählte Zeilen zu markieren
    const formatRowsFunction = function ($trs, $tr, selectionMode) {
        // moossenm: Klasse row-selection hinzugefuegt um Zeilenauswahl-Checkbox von anderen zu unterscheiden.
        const $input = $tr.find("td div.row-selection .checkbox label input");
        if ($input.is(":checked")) {
            $tr.addClass("active");
            // Falls der Selection Mode "single" ist, dann müssen jetzt alle anderen Input Felder deaktiviert werden.
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
    };
    // (2) Klickbereich für die Auswahl von Zeilen erweitern / Doppelklick setzen / Selection Mode initialisieren
    $rfDataTables.each(function () {
        const $rfDataTable = $(this);
        const $rfDataTableSelectOption = $(this).find("[id$='rfDataTableSelectableOption']").first();
        const selectionMode = $(this).find("[id$='rfDataTableSelectionMode']").first().val();
        const selectActive = ($rfDataTableSelectOption.val() === 'true');
        const $rfDataTableDoubleClickActive = $(this).parent().find("[id$='rfDataTableDoubleClickActive']").first();
        const doubleClickActive = ($rfDataTableDoubleClickActive.text() === 'true');

        const $rows = $(this).find("tbody tr");
        $rows.each(function () {
            const $row = $(this);
            let clicks = 0;
            let timer = null;

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
    const selectAllFunction = function ($selectAllCheckbox, $rfDataTable) {
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
        const selectionMode = $rfDataTable.find("[id$='rfDataTableSelectionMode']").first().val();
        const $rows = $rfDataTable.find("tbody tr");
        $rows.each(function () {
            // moossenm: DSD-509 - 16.06.2015
            // Fehlenden Parameter rows und selectionMode hinzugefügt, jetzt werden auch die ausgewählte Zeilen hervorgehoben
            formatRowsFunction($rows, $(this), selectionMode);
        });
    };
    // (4) Show-/Hide-Detail-Logik registrieren
    const showDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        const $this = $(this);
        const $tr = $this.parents('tr');
        const $table = $this.parents("table.CLIENT.rf-data-table");
        const allowMultiple = $table.find("input[id$='rfDataTableDetailMode']").val() == 'multiple';
        if (!allowMultiple) {
            // Alle Detailzeilen ausblenden
            $table.find("tr[id*='detail-']").addClass('hidden');
            // Eventhandler, Tooltip, ID für hideDetail-Buttons wechseln
            const $hideDetailButtons = $table.find('div.detailview-actions button[id*=hideDetail]');
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
    const hideDetail = function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        const $this = $(this);
        const $tr = $this.parents('tr');
        $tr.next().addClass('hidden');
        $this.attr('title', $this.parents('div.detailview-actions').data('show-tooltip'));
        $this.attr('id', $this.attr('id').replace("hideDetail", "showDetail"));
        $this.find('span').removeClass('icon-minus').addClass('icon-plus');
        // Eventhandler wechseln
        $this.off('click.hidedetail');
        $this.on('click.showdetail', showDetail);
    };
    $("table.CLIENT.rf-data-table").each(function () {
        const $table = $(this);
        // =============== START DETAILVIEW ===================== //
        $table.find('tbody div.detailview-actions button').prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
        const $showDetail = $table.find('div.detailview-actions button[id*=showDetail]');
        $showDetail.on('click.showdetail', showDetail);
        const $hideDetail = $table.find('div.detailview-actions button[id*=hideDetail]');
        $hideDetail.on('click.hidedetail', hideDetail);
        // =============== END DETAILVIEW ===================== //
    });
    // (5) JS Sortierung aktivieren
    $('.rf-data-table').each(function () {
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
                    $thLink.prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
                    $thLink.unbind("click");
                    $thLink.click(function (event) {
                        event.preventDefault();

                        // Details vor Sortierung speichern
                        const $details = $rfDataTable.find("tbody .details-preview");
                        $details.remove();

                        // Neu Sortierattribute und Richtung ermitteln
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


                        // Sortierung
                        window[$sortFunction.val()]($rfDataTable, $th, index, newSortDirection);

                        // Sortierung speichern
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

                        //     Details nach Sortierung wieder zuordnen
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

    });

    //(6) Den Zustand der 'Alle Auswählen' Checkbox immer korrekt setzen.
    const tristateBerechnen = function ($checkboxes, $selectAllCheckbox, $rfDataTable) {
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
    };


    $rfDataTables.each(function () {
        const $selectAllCheckbox = $(this).find("[id*='dataTableSelectAll']").first();
        const $rfDataTable = $(this);

        //Click auf der Tri-State-Checkbox registrieren.
        $selectAllCheckbox.parent().find("span").click(function () {
            selectAllFunction($selectAllCheckbox, $rfDataTable);
        });

        //Click auf den restlichen Checkboxes registrieren.
        const $checkboxes = $rfDataTable.find("tbody").first().find(".checkbox input");
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
        const $popover = $(e.target).next();
        if ($(document).width() < ($popover.offset().left + $popover.width() + 30)) {
            const positionLeft = -($(document).width() - $popover.offset().left) + 20;
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

    const $datepickers = $('.rf-datepicker').filter(':not(.rf-datepicker_ajaxtoken)').filter(':not(.rf-datepicker_readonly)');
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
                const dateReg = /^\d{2}[.]\d{2}[.]\d{4}$/;
                const inputField = $(this).prev();
                let date = inputField.val().split('.');

                // eleminiere die Unterstrich-Platzhalterzeichen
                const placeholderReg = /\D/gi;
                date[0] = date[0].replace(placeholderReg, "");
                date[1] = date[1].replace(placeholderReg, "");
                date[2] = date[2].replace(placeholderReg, "");
                let dateString;
                if (date[0] === "99") {
                    // Secret-Code: 99 = setze Fokus des Datepickers auf das aktuelle Datum
                    dateString = currentDateAsString();
                } else if (date[0] === "00" || date[1] === "00") {
                    dateString = setValidDateAsString(date);
                } else {
                    // uebernehme das manuell eingegebene Datum als Datumswert für den Datepicker.
                    // Die falschen Datumsangaben werden gefixt
                    dateString = fixDateOutOfRange(date);
                }
                $(this).parent().datepicker('setDate', dateString);
                $(this).parent().datepicker('update');
            });

        //Lese den Grenzwert zum Vervollständigen von zweistelligen Jahreszahlen aus. Wird weiter unten verwendet.
        const zweistelligeJahreszahlenErgaenzenGrenze = $('#formDateJahresZahlenErgaenzenGrenze').val();
        const $datumInputFeld = $(this).find('input');
        $datumInputFeld.focusout(function (event) {
            if (zweistelligeJahreszahlenErgaenzenGrenze !== "-1") {
                datumErgaenzen($datumInputFeld, zweistelligeJahreszahlenErgaenzenGrenze);
            }

            // Magic Number: setze Datum auf Tagesdatum
            const date = $datumInputFeld.val().split('.');
            if (date[0] === "99") {
                $datumInputFeld.val(currentDateAsString());
            } else {
                // Die falschen Datumsangaben werden gefixt
                $datumInputFeld.val(fixDateOutOfRange(date));
            }
        });
    });

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });

    function fixDateOutOfRange(date) {
        let year = date[2],
            month = date[1],
            day = date[0];
        // Assume not leap year by default (note zero index for Jan)
        const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

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
        const maxDays = daysInMonth[month - 1];
        if (day > maxDays) {
            day = maxDays;
        }

        return day + '.' + month + '.' + year;
    }

    // --------------------------------------------------------
    // Input Masks
    // --------------------------------------------------------
    // Alle Input Elemente, welche ein Attribut 'inputmask' besitzen
    const $inputMasks = $('input[data-isymask-mask][data-isymask-mask!=""]').filter(':not(.isyfact-inputmask_ajaxtoken)');
    $inputMasks.each(function () {
        const $inputMask = $(this);
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
        const $inputMask = $(this);

        if (e.key === 'Enter') {
            // Alle Platzhalter-Zeichen entfernen
            const existentVal = $inputMask.val();
            const newVal = existentVal.replace(/_/g, '');
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
    const listpickerLoeseSchluesselAuf = function (listpickerfield, indexSpalteSchluesselWert) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // verhindere, dass Ziffern aus dem Wert im Feld verbleiben
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
    const listpickerMaskieren = function (listpickerfield) {
        if (listpickerfield.val().indexOf(" - ") >= 0) {
            // verhindere, dass Ziffern aus dem Wert im Feld verbleiben
            listpickerfield.val(listpickerfield.val().substring(0,
                listpickerfield.val().indexOf(" - ")));
        }
        if (listpickerfield.attr('data-isymask-mask') !== undefined) {
            listpickerfield.mask();
        }
    };

    const $listpickerContainer = $(".listpicker-container").filter(':not(.rf-listpicker_ajaxtoken)');
    $listpickerContainer.each(function () {

        const $listpicker = $(this);
        const $listpickerField = $listpicker.find("[id$='listpickerField']");
        const $listpickerContent = $listpicker.find(".listpicker-content").first();
        const $listpickerFilter = $listpicker.find("[id$='listpickerFilter']");
        const $listpickerMinWidth = $listpicker.find("[id$='listpickerMinWidth']");
        const listpickerAjaxFormId = $listpicker.find("[id$='listpickerAjaxForm']").val();
        let $listpickerAjaxForm = null;
        //Finde das Hidden-Input, in dem hinterlegt ist, welche Spalte jeweils den Wert zum Schlüssel enthält.
        const listpickerSchluesselwertSpalte = $listpicker.find("[id$='inputComplement']").val();
        if (typeof (listpickerAjaxFormId) != "undefined") {
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
            const $charpickers = $(".special-char-picker-widget");
            $charpickers.each(function () {
                $(this).hide();
            });

            // Wenn ein Charpicker geöffnet ist und man neuen Listpicker öffnet, wollen wir veraltetes Fokus löschen
            const $active_charpickers_field = $(".charpicker-focused");
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
                let id;
                //Falls für das Feld bereits der Schlüssel aufgelöst wurde, müssen wir den Schlüssel isolieren.
                if ($listpickerField.val().indexOf(" - ") >= 0) {
                    id = $listpickerField.val().substring(0, $listpickerField.val().indexOf(" - "));
                } else {
                    id = $listpickerField.val();
                }
                $listpickerContent.find("tbody tr[id='" + id + "']").addClass("active");
                $listpickerContent.find("tbody tr").not("[id='" + id + "']").removeClass("active");

                // Cursor im Listpickerwidget auf aktive Zeile setzen
                const $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");
                scroll_to($listpickerContent.find('.rf-listpicker-table-container'), $activeVisibleRow);
            } else {
                // bei leerem bzw. geloeschten $listpickerField sind ehem. active-Eintraege zu loeschen
                $listpickerContent.find("tbody tr").removeClass("active");
            }

        });


        // Klicks abfangen und Feld ggf. schließen
        $(document).click(function (e) {

            const $target = $(e.target);

            if ($listpickerContent.has($target).length <= 0) {
                // Der Klick ist außerhalb des Dropdowns, schliesse Picker
                $listpicker.removeClass('open');
            } else {
                // Der Klick ist innerhalb des Dropdowns, daher sollte die aktuelle Zelle ausgewählt werden
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

        // Registriere Eingabefeld
        $listpickerFilter.bind('keypress', function (e) {
            if (!e.ctrlKey && !e.altKey && !e.shiftKey && e.keyCode == 13) {
                // Kein Aufsteigen im DOM-Tree: Dieser Tastendruck betrifft nur den Filter
                e.stopPropagation();

                // Beim Drücken von Enter wird der aktive Eintrag ausgewählt
                let $row = $listpickerContent.find("tbody tr:visible.active").first();
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
            const keyCode = event.keyCode;
            const valid = inputChangingKeycode(keyCode); // ändert der Tastendruck den Inhalt des Filters?
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
                    function () {

                        setTimeout(
                            function () {

                                // Filter
                                const $rows = $listpickerContent
                                    .find(".rf-listpicker-table tbody tr");

                                const filterFunction = function ($row,
                                                               inverse) {

                                    const $tdsAfterFilter = $row
                                        .find('td')
                                        .filter(
                                            function () {
                                                const $td = $(this);
                                                const text = $td
                                                    .text();
                                                const listpickerVal = $listpickerFilter
                                                    .val();
                                                const compare = text
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

                                const $filteredRows = $rows
                                    .filter(function () {
                                        return filterFunction(
                                            $(this), false);
                                    });
                                const $unfilteredRows = $rows
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

            const keyPressed = e.which;

            // aktuell aktives sichtbares Element
            const $activeVisibleRow = $listpickerContent.find("tbody tr:visible.active");

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
    const $buttonSelectpicker = $('button.selectpicker');
    $buttonSelectpicker.click(function (event) {
        $listpickerContainer.removeClass('open');
    });

    // --------------------------------------------------------
    // Tabs
    // --------------------------------------------------------
    // Vorgeladene Tabs steuern
    $('.isy-tab').each(function () {
        const $isyTab = $(this);

        // TabAutoscroll: Ist in tabGroup ein Tab-Inhaltsbereich-HochScrollen gewünscht?
        let $tabHochScrollen = false;
        if ($isyTab.hasClass('tabHochScrollen')) {
            $tabHochScrollen = true;
        }

        $isyTab.children().each(function () {
            const $li = $(this);
            const $liLink = $(this).find("a");

            if ($li.hasClass('skipAction')) {
                $liLink.unbind("click");
                $liLink.prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht
            }

            $liLink.click(function (event) {
                if ($li.hasClass('skipAction')) {
                    event.preventDefault();
                }

                // Aktuelles Tab entfernen
                const liIdAlt = $isyTab.find(".active").attr('id');
                $isyTab.find(".active").removeClass("active");
                $isyTab.next().find("#" + liIdAlt).removeClass("active");

                // Tab aktivieren
                const liIdNeu = $li.attr('id');
                $li.addClass("active");
                //$isyTab.next().find("#" + liIdNeu).addClass("active");

                const aktiverTab = $isyTab.next().find("#" + liIdNeu);
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
    const $buttonInjectPostGroups = $("[id$='buttonInjectPostGroup']");
    $buttonInjectPostGroups.each(function () {
        const $group = $(this);

        // Finde klickbares Element in der ButtonInjectPostGroup
        const $actualButton = $group.find(":nth-child(4)");

        // Finde Button für POST-Aktion
        const $postButton = $("[id$='" + $group.find("[id$='postButton']").val() + "']");

        // Finde Feld für posted
        const $posted = $group.find("[id$='posted']");

        // Finde Feld für continue
        const $continue = $group.find("[id$='continue']");


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
            $actualButton.prop("onclick", null); // IE11 unterstützt .removeAttr() für "onclick" nicht

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
    const $forms = $("form");
    $forms.each(function () {
        const $form = $(this);
        $form.unbind("keypress");
        const $defaultButton = $form.find("[id*='" + $form.find("[id$='defaultButtonID']").val() + "']");
        if ($defaultButton.length > 0) {
            // Ursprüngliches Bind deaktivieren
            $form.unbind("keypress");
            // Das Form enthält einen DefaultButton
            $form.bind("keypress", function (event) {
                if (event.keyCode == 13) {
                    const $source = $(document.activeElement);
                    // Kein Link, Button, Charpicker oder anderes Submit-Element fokussiert, das eine eigene sinnvolle Aktion bei Enter hat
                    if (!$source.is("[type='submit']") &&
                        !$source.is("a") &&
                        !$source.is("button") &&
                        !$source.hasClass("charpicker") &&
                        !$defaultButton.first().is("[disabled]")) {
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
        const $bc = $(this);
        let timerId;

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
                // eingrenzen
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
                let sortClass = 'sort-up'; // standardmäßig aufsteigend sortiert
                const thisSortProperty = $th.data("sortattribute");
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
        const $list = $(this);
        let timerId;

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
            const $this = $(this);
            const $sel = $this.find('select');
            $sel.find('option').each(function () {
                if (!this.value) return; // überspringe platzhalter
                $this.append('<button type="button" class="btn btn-default' + (this.selected ? ' active' : '') + (this.disabled ? ' disabled' : '') + '" value="' + this.value + '">' + this.text + '</button>');
            });
        }).on('click', 'button', function () {
        if ($(this).hasClass('active') || $(this).hasClass('disabled')) return;
        const $sel = $(this).parent().find('select');
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

function setValidDateAsString(date) {
    "use strict";
    date[0] = date[0].replace("00", "01");
    date[1] = date[1].replace("00", "01");
    return date[0] + '.' + date[1] + '.' + date[2];
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
initialisierenListpickerServlet = function () {
    "use strict";
    var $listpicker = $(".servlet.listpicker-filter");
    $listpicker.each(function (i, listpicker) {
        registerListpickerfilter(listpicker);
    });
};


//registrieren eines Listpickers
registerListpickerfilter = function (identifier) {
    "use strict";
    var $listpickerFilter = $(identifier);
    var listpickerFilterInput = $listpickerFilter.children()[0];
    var url = $listpickerFilter.siblings("div.rf-listpicker-table-container").find(".servletTable")[0].getAttribute("data-servleturl");

    //Im Folgenden werden die einzelnen Parameter, die in der URL enthalten sind encoded.
    //Es wird jeweils der Wert des Parameters encoded, nicht der Parameter selbst.
    var urlsplit = url.split("?");

    //Der erste Teil der URL (alles ohne Paramater) bleibt unverändert.
    var urlEncoded = urlsplit[0] + '?';


    //Splitte den zweiten Teil
    if (urlsplit.length > 1) {
        var attributeGesetzt = urlsplit[1].split("&");
        attributeGesetzt.forEach(function (attribut) {
            var attributSplit = attribut.split("=");
            urlEncoded = urlEncoded + attributSplit[0] + '=' + encodeURIComponent(attributSplit[1]) + "&";
        });
    }

    //initiale Befüllung des Listpickers
    //Hier wird der eigentliche Request abgeschickt!
    $.get(urlEncoded + "filter=" + encodeURIComponent(listpickerFilterInput.value)).done(function (data) {
        createListpickerTable(data, $listpickerFilter);
    });
    listpickerFilterInput.dataset.oldvalue = listpickerFilterInput.value;

    var $listpickerContent = $listpickerFilter.parent().parent();
    var $listpickerContainer = $listpickerContent.parent();
    var $listpickerField = $listpickerContainer.find('*[id*=listpickerField]');

    //Hat man sich im Dropdownmenü befunden und klickt anschließend außerhalb, werden die Felder synchronisiert.
    $(listpickerFilterInput).focusout(function () {
        $listpickerFilter.parent().parent().siblings('.form-control').focusout();
    });

    //Die Filtermethode, die die Liste aktualisiert
    //Zunächst deaktivieren wir den Handler für den Fall, dass er schon existiert und aktualisiert
    //werden muss. (Dies ist beispielsweise der Fall, wenn die URL per JavaScript manipuliert wurde, ohne dass die gesamte Seite neu gerendert wird.)
    //Wenn wir den Handler nicht vorher deaktivieren, bleibt die Servlet-URL effektiv unverändert und
    //der Filter funktioniert dann nicht korrekt.
    $(listpickerFilterInput).off('change keyup', servletListpickerFilterChanged);
    //Die benötigten Daten (die URL und der Filter selbst) geben wir als Data-Attribute rein.
    $(listpickerFilterInput).on('change keyup', {
        url: urlEncoded,
        listpickerfilter: $listpickerFilter,
        listpickerFilterInput: $(listpickerFilterInput)
    }, servletListpickerFilterChanged);
};

/**
 * Die Funktion behandelt change und keyup Events für die Listpicker, die per Servlet filtern.
 * @param event Das change/keyup Event.
 */
function servletListpickerFilterChanged(event) {
    "use strict";
    event.stopImmediatePropagation();
    //Hole die benötigten Daten aus den Data-Attributen des Events (wurden im Aufruf gesetzt).
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

//Erstellt einen ListpickerTable anhand des responseTextes.
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

//Bei einem Klick im Dokument, wird ein Listpicker, falls dieser geöffnet war, geschlossen und zusätzlich die
//Focusout-Methode getriggert, um das Auflösen des Schlüssels zu bewirken.
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
