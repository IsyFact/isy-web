/**
 * Returns the current date as a string.
 * @returns {string}
 */
export function currentDateAsString() {
    "use strict";
    const currentDate = new Date();
    const dayOfMonth = currentDate.getDate();
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();
    const heute = dayOfMonth.toString() + '.' + month.toString() + '.' + year.toString();
    const heuteMitFuehrendenNullen = heute.replace(/(^|\D)(\d)(?!\d)/g, '$10$2');

    return heuteMitFuehrendenNullen;
}

/**
 * Creates a string from a date, which is passed in form of an array.
 * @param date
 * @returns {string}
 */
export function setValidDateAsString(date) {
    "use strict";
    date[0] = date[0].replace("00", "01");
    date[1] = date[1].replace("00", "01");
    return date[0] + '.' + date[1] + '.' + date[2];
}

/**
 * This function transforms two digit years into four digit years in date input fields.
 *
 * @param inputFeld input field containing a date
 * @param grenze threshold indicating if the prefix "19" oder "20" should be added to the two digit year
 */
export function datumErgaenzen(inputFeld, grenze) {
    "use strict";
    //The given "grenze" as a limit is added to the current year, so the resulting limit year will update over time
    const aktuellesJahr = parseInt(new Date().getFullYear().toString().substring(2, 4));
    grenze = (aktuellesJahr + parseInt(grenze)).toString();
    const aktuelleWerte = inputFeld.val().split('.');
    if (aktuelleWerte.length === 3 && aktuelleWerte[2].replace(/_/g, '').length === 2) {
        let praefix;
        const jahresZahl = aktuelleWerte[2].replace(/_/g, '');
        if (jahresZahl <= grenze) {
            praefix = "20";
        } else if (jahresZahl > grenze) {
            praefix = "19";
        }
        const ergebnis = praefix + jahresZahl;
        aktuelleWerte[2] = ergebnis;
        inputFeld.val(aktuelleWerte[0] + "." + aktuelleWerte[1] + "." + aktuelleWerte[2]);
    }
};

/**
 * Creates a valid date from a date which is invalid, because certain values are out of range of actual dates.
 * The result will be returned as a string.
 * @param date
 * @returns {string}
 */
export function fixDateOutOfRange(date) {
    const year = date[2];
    let month = date[1],
        day = date[0];
    // Assumes it's not a leap year by default (note zero index for Jan)
    const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    // If evenly divisible by 4 and not evenly divisible by 100,
    // or if evenly divisible by 400, then a leap year
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