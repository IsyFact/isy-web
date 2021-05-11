/**
 * execute post-Action of ButtonInjectPostGroup if necessary
 * and refreshes handlers.
 */
export function executeAndRefreshButtonInjectPostGroups(){
    const $buttonInjectPostGroups = $("[id$='buttonInjectPostGroup']");
    $buttonInjectPostGroups.each(function () {
        const $group = $(this);

        // find clickable element in the ButtonInjectPostGroup
        const $actualButton = $group.find(":nth-child(4)");

        // find button for POST-action
        const $postButtonId = $group.find("[id$='postButton']").val();
        let $postButton = $("[id$='" + $postButtonId + "']");
        //jsf ids might have other suffixes delimited with ':', e.g. ':ajax_button'
        //prefer no-suffix version, but if none is found, look for suffixed buttons as well
        if ($postButton.length === 0){
            $postButton = $("[id*='" + $postButtonId + ":']");
        }

        // find field for posted
        const $posted = $group.find("[id$='posted']");

        // fiend field for continue
        const $continue = $group.find("[id$='continue']");


        if ($posted.attr("value") === 'true') {
            // the POST action was successfully completed. Reset the flag.
            $posted.attr("value", "false");

            // only click if continue-flag was set
            if ($continue.attr("value") === 'true') {
                $actualButton.trigger("onclickStandby");
            }
        }

        // bind events, if they haven't already
        if (!$group.hasClass("isyfact-buttonInjectPostGroup_ajaxtoken")) {
            // <a>-tag: remove onclick
            $actualButton.on("onclickStandby", $actualButton.prop("onclick"));
            $actualButton.prop("onclick", null);

            // <input type=submit ...> prevent transmission
            // overwrite button action
            $actualButton.on("click.postInject", function (event) {
                event.preventDefault();
                $posted.attr("value", "true");
                $postButton.click();
            });
            $group.addClass('isyfact-buttonInjectPostGroup_ajaxtoken');
        }
    });
}

