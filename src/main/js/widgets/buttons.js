export function bindReturnToDefaultButton () {
    const $form = $(this);
    $form.unbind("keypress");
    const $defaultButton = $form.find("[id*='" + $form.find("[id$='defaultButtonID']").val() + "']");
    if ($defaultButton.length > 0) {
        // remove original bind
        $form.unbind("keypress");
        // the form contains a default button
        $form.bind("keypress", function (event) {
            if (event.keyCode == 13) {
                const $source = $(document.activeElement);
                // No link, button, charpicker or other submit-element is focused,
                // that has their own action on enter
                if (!$source.is("[type='submit']") &&
                    !$source.is("a") &&
                    !$source.is("button") &&
                    !$source.hasClass("charpicker") &&
                    !$defaultButton.first().is("[disabled]")) {
                    // trigger click on default button
                    $defaultButton.first().click();
                    // prevent original default-action,
                    // which would be clicking the first button found having type="submit"
                    event.preventDefault();
                }
            }
        });
    }
}

/**
 * Blocks a single button on click. Prevents doubleclicks.
 */
export function blockSingleButton(data) {
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
