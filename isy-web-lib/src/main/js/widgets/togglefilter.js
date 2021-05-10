/** Initializes togglefilters.
 * Adds eventhandlers and adds a token-class "toggle-filter-ajax" to mark already initialized toggle-filters.
 * Marked objects will be ignored on subsequent calls.
 */
export function initToggleFilters() {
    $("div.toggle-filter:not('.toggle-filter-ajax')")
        .addClass('toggle-filter-ajax') // mark as already initialized
        .removeClass('hidden')
        .each(createToggleFilter)
        .on('click', 'button', applyToggleFilter);
}

/**
 * Creates the togglefilter bar
 */
function createToggleFilter () {
    const $this = $(this);
    const $sel = $this.find('select');
    $sel.find('option').each(function () {
        if (!this.value) return; // skip placeholders
        $this.append('<button type="button" class="btn btn-default' + (this.selected ? ' active' : '') + (this.disabled ? ' disabled' : '') + '" value="' + this.value + '">' + this.text + '</button>');
    });
}

/**
 * Applies the selected filter
 */
function applyToggleFilter () {
    if ($(this).hasClass('active') || $(this).hasClass('disabled')) return;
    const $sel = $(this).parent().find('select');
    $sel.val(this.value);
    $(this).parent().find('input[type=submit]').click();
}
