/**
 * Creates the togglefilter bar
 */
export function createToggleFilter () {
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
export function applyToggleFilter () {
    if ($(this).hasClass('active') || $(this).hasClass('disabled')) return;
    const $sel = $(this).parent().find('select');
    $sel.val(this.value);
    $(this).parent().find('input[type=submit]').click();
}
