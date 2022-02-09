import { listpickerAjaxReload } from'../widgets/listpicker/listpicker';
import { blockSingleButton } from '../widgets/buttons';

/**
 * Global exports needed for direct calls from xhtml-Elements.
 * They are bundled inside an "isywebjs" object, to avoid overly polluting the window-namespace.
 */
const isywebjs = {};
isywebjs.listpickerAjaxReload = listpickerAjaxReload;
isywebjs.blockSingleButton = blockSingleButton;

window.isywebjs = isywebjs;

