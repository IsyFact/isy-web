/**
 * Formats currency input to use the correct amount of decimal places.
 *
 * parseFloat is used, as the component formCurrencyInput already prevents input of letters.
 */
export function formatAmountOfMoney(ref) {
    'use strict';
    const inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        const dezimalstellen = $(inputField).data("decimalplaces");
        let result = formatiereInput(ref.value, dezimalstellen);
        result = kuerzeInput(result, ref.maxLength);
        result = setzeTausenderPunkte(result);
        inputField.value = result;
    }
}

/**
 * Formats numeric values and floating point numbers.
 *
 * parseFloat is used, as the component formNumericInput already prevents input of letters
 * in the "onkeyup"-event.
 */
export function formatNumericValue(ref) {
    'use strict';
    const inputField = document.getElementById(ref.id);
    if (ref.value !== "") {
        const dezimalstellen = $(inputField).data("decimalplaces");
        const tausenderpunktGewuenscht = $(inputField).data("tausenderpunkt");
        let result = formatiereInput(ref.value, dezimalstellen);
        result = kuerzeInput(result, ref.maxLength);
        if (tausenderpunktGewuenscht) {
            result = setzeNumerischeTausenderPunkte(result);
        }
        inputField.value = result;
    }
}

/**
 * Formats input value: remove thousands separator and show the given number of decimal places
 * (e.g. xxxxx,xx)
 *
 * Used by formCurrencyInput and formNumericInput components
 */
function formatiereInput(input, dezimalstellen) {
    'use strict';
    let value = input.split(".").join("");
    value = value.replace(',', '.');
    let tmp = parseFloat(value).toFixed(dezimalstellen);
    return tmp.replace('.', ',');
}

/**
 * Shortens input to the given length.
 */
function kuerzeInput(value, length) {
    'use strict';
    let kommaPosition = value.indexOf(",");
    let anzahlTausenderPunkte = parseInt(((kommaPosition - 1) / 3));
    while (value.length > (length - anzahlTausenderPunkte)) {
        value = value.substring(0, kommaPosition - 1) + value.substring(kommaPosition);
        kommaPosition = value.indexOf(",");
        anzahlTausenderPunkte = parseInt(((kommaPosition - 1) / 3));
    }
    return value;
}

/**
 * Format currency value input to use dot as thousands separator ( e.g. xx.xxx,xx )
 */
function setzeTausenderPunkte(value) {
    'use strict';
    const kommaPosition = value.indexOf(",");
    for (let i = 1; i < kommaPosition; i++) {
        if (i % 3 === 0) {
            value = value.substring(0, kommaPosition - i) + "." + value.substring(kommaPosition - i);
        }
    }
    return value;
}

/**
 * Format input of numeric values or floating point number to use dot as thousands separator
 * (e.g. xx.xxx or xx.xxx,xx)
 */
function setzeNumerischeTausenderPunkte(value) {
    'use strict';
    let kommaPosition = value.indexOf(",");
    if (kommaPosition === -1) {
        kommaPosition = value.length;
    }
    for (let i = 1; i < kommaPosition; i++) {
        if (i % 3 === 0) {
            value = value.substring(0, kommaPosition - i) + "." + value.substring(kommaPosition - i);
        }
    }
    return value;
}

/**
 * Delete all characters apart from numbers and comma in an input field.
 * Is used by formCurrencyInput and formNumericInput Components as a onkeyup-event.
 *
 * @param ref -
 *            reference to the input field whose contents are replaced
 */
export function deleteNonDigitCharacters(ref) {
    'use strict';
    if (ref.value !== "") {
        // saves the current cursor position as a variable
        // needed for browser compatibility with IE and Chrome
        let start = ref.selectionStart;
        let end = ref.selectionEnd;

        // length of text is saved:
        // is used later on to determine how many characters were removed
        // to calculate the shift in position of the cursor
        const length = ref.value.length;

        // removes all characters but numbers and comma as a decimal separator
        // Attention: dots as thousands separators are also removed
        ref.value = ref.value.replace(/[^\d,.]/g, '');

        // checks whether characters were removed and shifts the cursor
        // accordingly - is needed for IE and Chrome; for FF replacing the text is sufficient
        const lengthAfterReplace = ref.value.length;
        if (length > lengthAfterReplace) {
            start = start - (length - lengthAfterReplace);
            end = end - (length - lengthAfterReplace);
        }

        // sets cursor position
        ref.setSelectionRange(start, end);
    }
}