import { listpickerAjaxReload } from'../widgets/listpicker/listpicker';
import { blockSingleButton } from '../widgets/buttons';
import { formatAmountOfMoney, formatNumericValue, deleteNonDigitCharacters } from '../widgets/input';

/**
 * Global exports needed for direct calls from xhtml-Elements.
 * They are bundled inside an "isywebjs" object, to avoid overly polluting the window-namespace.
 */
const isywebjs = {};
isywebjs.listpickerAjaxReload = listpickerAjaxReload;
isywebjs.blockSingleButton = blockSingleButton;
isywebjs.formatAmountOfMoney = formatAmountOfMoney;
isywebjs.formatNumericValue = formatNumericValue;
isywebjs.deleteNonDigitCharacters = deleteNonDigitCharacters;

window.isywebjs = isywebjs;

