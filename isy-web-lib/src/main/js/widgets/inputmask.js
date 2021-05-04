/**
 * add handlers to input fields with input masks.
 * Adds a token-class (isyfact-inputmask_ajaxtoken) if the field has already been initialized.
 * Marked classes will be skipped on subsequent calls.
 */
export function initInputMasks(){
    const $inputMasks = $('input[data-isymask-mask][data-isymask-mask!=""]').filter(':not(.isyfact-inputmask_ajaxtoken)');

    $inputMasks.each(applyMask);
    $inputMasks.bind('keydown keypress', deletePlaceholdersOnReturn);

    $inputMasks.addClass('isyfact-inputmask_ajaxtoken');
}

/**
 * Applies the mask on an input field
 */
function applyMask() {
    const $inputMask = $(this);
    if ($inputMask.attr('name').indexOf('listpickerField') > -1) {
        if ($inputMask.val().indexOf(" - ") >= 0) {
            // prevents that digits of the value stay in the field
            $inputMask.val($inputMask.val().substring(0, $inputMask.val().indexOf(" - ")));
        }
    }

    $inputMask.mask();

    // max length is set via mask, otherwise ctrl+v wouldn't work
    $inputMask.removeAttr('maxlength');
}

/**
 * Deletes the placeholder characters if the pressed
 * key is the return rey
 * @param e keydown keypress event
 */
function deletePlaceholdersOnReturn(e) {
    const $inputMask = $(this);

    if (e.key === 'Enter') {
        // delete all placeholder characters
        const existentVal = $inputMask.val();
        const newVal = existentVal.replace(/_/g, '');
        $inputMask.val(newVal);
    }
}
