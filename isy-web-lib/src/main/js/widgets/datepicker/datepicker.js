import { currentDateAsString, setValidDateAsString, datumErgaenzen, fixDateOutOfRange } from "./datum-utils";

import 'bootstrap-datepicker/js/locales/bootstrap-datepicker.de';

/**
 * Adds handlers to all datepickers that haven't been initialized yet.
 * Initialized datepickers are marked with a token class (rf-datepicker_ajaxtoken).
 */
export function initDatepickers() {
    // --------------------------------------------------------
    // Datepicker
    // --------------------------------------------------------
    const $datepickers = $('.rf-datepicker').filter(':not(.rf-datepicker_ajaxtoken)').filter(':not(.rf-datepicker_readonly)');
    $datepickers.addClass('rf-datepicker_ajaxtoken');
    $datepickers.each(createDatepicker);

    $datepickers.on('changeDate', function (ev) {
        $(this).find('input').val(ev.format());
    });
}

/**
 * Initializes the datepicker when it is loaded and
 * handles the actions when the user interacts with the datepicker.
 */
export function createDatepicker () {
    $(this).datepicker({
        format: $(this).attr('dateformat'),
        weekStart: 1,
        todayBtn: "linked",
        language: $(this).attr('language'),
        autoclose: true,
        todayHighlight: true,
        // showOnFocus: If false, the datepicker will be prevented from showing when the input field associated with it receives focus.
        showOnFocus: false,
        // enableOnReadonly: If false the datepicker will not show on a readonly datepicker field.
        enableOnReadonly: false,
        // remove arrows, set by css styles
        templates: {
            leftArrow: ' ',
            rightArrow: ' '
        }
    });

    $(this).children("a").click(openDatepicker);

    //read the limit for expanding two-digit years. Is used later on.
    const $datumInputFeld = $(this).find('input');
    $datumInputFeld.focusout(datepickerFocusout);
}

/**
 * Opens a datepicker.
 */
function openDatepicker() {
    const dateReg = /^\d{2}[.]\d{2}[.]\d{4}$/;
    const inputField = $(this).prev();
    const date = inputField.val().split('.');

    // delete underscore placeholders
    const placeholderReg = /\D/gi;
    date[0] = date[0].replace(placeholderReg, "");
    date[1] = date[1].replace(placeholderReg, "");
    date[2] = date[2].replace(placeholderReg, "");
    let dateString;
    if (date[0] === "99") {
        // Secret-Code: 99 = set focus of the datepicker to current date
        dateString = currentDateAsString();
    } else if (date[0] === "00" || date[1] === "00") {
        dateString = setValidDateAsString(date);
    } else {
        // copy the manually entered date into the datepicker
        // out of range date inputs will be fixed
        dateString = fixDateOutOfRange(date);
    }
    $(this).parent().datepicker('setDate', dateString);
    $(this).parent().datepicker('update');
}

/**
 * Handles the behavior of the datepicker, when the user loses focus of it.
 */
function datepickerFocusout () {
    const zweistelligeJahreszahlenErgaenzenGrenze = $('#formDateJahresZahlenErgaenzenGrenze').val();
    const $datumInputFeld = $(this);
    if (zweistelligeJahreszahlenErgaenzenGrenze !== "-1") {
        datumErgaenzen($datumInputFeld, zweistelligeJahreszahlenErgaenzenGrenze);
    }

    // Magic Number: set date to today
    const date = $datumInputFeld.val().split('.');
    if (date[0] === "99") {
        $datumInputFeld.val(currentDateAsString());
    } else {
        // out of range date inputs will be fixed
        $datumInputFeld.val(fixDateOutOfRange(date));
    }
}
