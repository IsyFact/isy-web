import { listpickerAjaxReload } from'../widgets/listpicker';
import { blockSingleButton } from '../widgets/buttons';
import { formatAmountOfMoney, formatNumericValue, deleteNonDigitCharacters } from "../widgets/input";

// global exports needed for direct calls from xhtml-Elements
// they are bundled inside an "isywebjs" object, to avoid overly polluting the window-namespace
const isywebjs = {};
isywebjs.listpickerAjaxReload = listpickerAjaxReload;
isywebjs.blockSingleButton = blockSingleButton;
isywebjs.formatAmountOfMoney = formatAmountOfMoney;
isywebjs.formatNumericValue = formatNumericValue;
isywebjs.deleteNonDigitCharacters = deleteNonDigitCharacters;


window.isywebjs = isywebjs;
