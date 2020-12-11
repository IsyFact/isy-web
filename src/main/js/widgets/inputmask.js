export function applyMask() {
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

export function deletePlaceholdersOnReturn(e) {
    const $inputMask = $(this);

    if (e.key === 'Enter') {
        // delete all placeholder characters
        const existentVal = $inputMask.val();
        const newVal = existentVal.replace(/_/g, '');
        $inputMask.val(newVal);
    }
}
