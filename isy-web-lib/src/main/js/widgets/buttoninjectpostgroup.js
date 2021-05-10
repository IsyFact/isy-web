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
        const $postButton = $("[id$='" + $group.find("[id$='postButton']").val() + "']");

        // find field for posted
        const $posted = $group.find("[id$='posted']");

        // fiend field for continue
        const $continue = $group.find("[id$='continue']");


        if ($posted.attr("value") === 'true') {
            // the POST action was successfully completed. Reset the flag.
            $posted.attr("value", "false");

            $actualButton.unbind("click.postInject");
            // If <a>-Tag: return onclick
            $actualButton.attr("onclick", $actualButton.attr("onclickStandby"));

            // Only trigger the click, if continue-flag was set
            if ($continue.attr("value") === 'true') {
                $actualButton.click();
            }
        }

        // bind events, if they haven't already
        if (!$group.hasClass(".isyfact-buttonInjectPostGroup_ajaxtoken")) {
            // <a>-tag: remove onclick
            $actualButton.attr("onclickStandby", $actualButton.attr("onclick"));
            $actualButton.prop("onclick", null); // IE11 does not support .removeAttr() for "onclick"

            // <input type=submit ...> prevent transmission
            // overwrite button action
            $actualButton.bind("click.postInject", function (event) {
                event.preventDefault();
                $posted.attr("value", "true");
                $postButton.click();
            });

            $group.addClass('isyfact-buttonInjectPostGroup_ajaxtoken');
        }
    });
}

