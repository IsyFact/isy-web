import { listpickerAjaxReload } from'../widgets/listpicker';
import { blockSingleButton } from '../widgets/buttons';

// global exports needed for direct calls from xhtml-Elements
// they are bundled inside an "isywebjs" object, to avoid overly polluting the window-namespace
const isywebjs = {};
isywebjs.listpickerAjaxReload = listpickerAjaxReload;
isywebjs.blockSingleButton = blockSingleButton;

window.isywebjs = isywebjs;
