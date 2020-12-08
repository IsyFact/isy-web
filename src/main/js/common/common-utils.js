/**
 * Lazy loads elements if necessary.
 */
export function lazyLoad() {
    'use strict';

    // Images
    $("[data-src].lazy").each(function () {
        const $lazyImage = $(this);
        if ($lazyImage.visible()) {
            const src = $lazyImage.attr("data-src");
            $lazyImage.attr("src", src);
            $lazyImage.removeAttr("data-src");
        }
    });
}

/** scrolls within an element to a specified div. */
export function scroll_to(element, div) {
    $(element).animate({
        scrollTop: $(div).parent().scrollTop() + $(div).offset().top - $(div).parent().offset().top
    }, 0);
}

/**
 * Checks whether the pressed key is a valid input character to change a text fields contents.
 * Common control characters are considered invalid.
 *
 */
export function inputChangingKeycode(keyCode) {
    'use strict';
    // keyCodes, that don't change input. Only valid for IE and Firefox!
    // Common control characters will be ignored.
    // invalid Keycodes:
    // 0 OR 9-13 OR 16-20 OR 27 OR 33-45 OR 91-93 OR 112-123 OR 144 OR 145 OR 181-183
    const invalid = (keyCode > 8 && keyCode < 14) ||
        (keyCode > 15 && keyCode < 21) ||
        keyCode === 0 || keyCode === 27 ||
        (keyCode > 32 && keyCode < 46) ||
        (keyCode > 90 && keyCode < 94) ||
        (keyCode > 111 && keyCode < 124) ||
        keyCode === 144 || keyCode === 145 ||
        (keyCode > 180 && keyCode < 184);
    return !invalid;
}
