/** initialized Image Popups (magnificPopup)
 * Already intiialized Popups are marked with a token class (rf-image-popup_ajaxtoken)
 * respectively rf-imagelink_ajaxtoken
 * and will be skipped on subsequent calls.
 */
export function initImagePopups(){
    $('.rf-image-popup').filter(':not(.rf-imagepopup_ajaxtoken)')
        .addClass('rf-imagepopup_ajaxtoken')
        .magnificPopup({
            type: 'image'
        });

    $('.image-link').filter(':not(.rf-imagelink_ajaxtoken)')
        .addClass('rf-imagelink_ajaxtoken')
        .magnificPopup({type: 'image'});
}
