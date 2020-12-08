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
