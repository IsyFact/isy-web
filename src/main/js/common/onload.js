import { initDatepickers } from '../widgets/datepicker/datepicker';
import { initDatatablesClientmode, initDatatables } from '../widgets/datatable/datatable';
import { refreshDatatableFilterRow } from '../widgets/datatable/datatable-filterrow';
import { createTabGroup } from '../widgets/tabs';
import { enableMultipartFormIfNeeded, lazyLoad } from './common-utils';
import { initialisierenListpickerServlet, initListpickers } from '../widgets/listpicker/listpicker';
import { bindReturnToDefaultButton } from '../widgets/buttons';
import { initInputMasks } from '../widgets/inputmask';
import { initNavigation } from './tastatursteuerung-navigation';
import { focusOnload } from './focusOnload';
import { initToggleFilters} from '../widgets/togglefilter';
import { enableTooltips } from '../widgets/tooltip';
import { initPanels } from '../widgets/panels';
import { initModalDialogs } from '../widgets/modaldialog';
import { initSelectpickers } from '../widgets/selectpicker';
import { initImagePopups } from '../widgets/imagepopup';
import { initBrowseCollect } from '../widgets/browsecollect';
import { executeAndRefreshButtonInjectPostGroups } from '../widgets/buttoninjectpostgroup';
import { renderAjaxErrorMessage, trackAjaxRequests } from './ajax';
import { addHandlersToSidebar } from './sidebar-collapse';
import { initSelectlists } from "../widgets/selectlist";

$(document).ready(function () {
    'use strict';

    // Handler for AJAX requests
    // jsf = JSF JavaScript Library
    if (typeof (jsf) != "undefined") {

        // render error messages
        jsf.ajax.addOnError(renderAjaxErrorMessage);
        // track status
        jsf.ajax.addOnEvent(trackAjaxRequests);
        // refresh handlers after ajax call

        jsf.ajax.addOnEvent(function (callback) {

            if (callback.status === 'success') {
                refreshFunctions();
            }

        });
    }

    // Lazy Loading
    $(document).scrolled(0, function () {
        lazyLoad();
    });

    $(window).resize(function () {
        const tag = "resizeTimer";
        const self = $(this);
        let timer = self.data(tag);
        if (timer) {
            clearTimeout(timer);
        }
        timer = setTimeout(function () {
            self.removeData(tag);
            lazyLoad();
        }, 500);
        self.data(tag, timer);
    });


    // init handlers on first load
    refreshFunctions();

    // handlers that will only need to be loaded once, as they are generally not changed by ajax requests
    addHandlersToSidebar();

});

/**
 * Refreshes existing JS-listeners.
 * Every function has to ensure on ajax requests that the listeners are registered only once.
 * If the listeners already exists, they must not be registered again.
 */
function refreshFunctions() {
    'use strict';

    lazyLoad();
    // Initialize all custom Handlers
    initBrowseCollect();
    initNavigation();
    initSelectlists();
    initPanels();
    initialisierenListpickerServlet();
    initToggleFilters();
    initModalDialogs();
    initDatepickers();
    initSelectpickers();
    initImagePopups();
    initListpickers();
    initInputMasks();

    //data tables
    initDatatables();
    initDatatablesClientmode();
    refreshDatatableFilterRow();

    executeAndRefreshButtonInjectPostGroups();

    focusOnload();
    enableTooltips();
    enableMultipartFormIfNeeded();


    // control preloaded tabs
    $('.isy-tab').each(createTabGroup);



    // Bind Enter to Defaultbutton for all forms
    $("form").each(bindReturnToDefaultButton);



}



