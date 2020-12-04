import { currentDateAsString, setValidDateAsString, datumErgaenzen, fixDateOutOfRange } from "./datum-utils";

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
        enableOnReadonly: false
    });

    $(this).children("a").click(openDatepicker);

    //read the limit for expanding two-digit years. Is used later on.
    const $datumInputFeld = $(this).find('input');
    $datumInputFeld.focusout(datepickerFocusout);
}

export function openDatepicker() {// open a datepicker
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
        // copy the manuell entered date into the datepicker
        // invalid date inputs will be fixed
        dateString = fixDateOutOfRange(date);
    }
    $(this).parent().datepicker('setDate', dateString);
    $(this).parent().datepicker('update');
}

export function datepickerFocusout () {
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
        // wrong dates will be fixed
        $datumInputFeld.val(fixDateOutOfRange(date));
    }
}
