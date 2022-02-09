import { listpickerAjaxReload } from'../widgets/listpicker/listpicker';

/**
 * Global exports needed for direct calls from xhtml-Elements.
 * They are bundled inside an "isywebjs" object, to avoid overly polluting the window-namespace.
 */
const isywebjs = {};
isywebjs.listpickerAjaxReload = listpickerAjaxReload;

window.isywebjs = isywebjs;