package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import de.bund.bva.isyfact.common.web.GuiController;

/**
 * Der abstrakte Controller für Listpicker. Jedes Listpicker-Feld muss einen eigenen definieren.
 *
 * @param <T>
 *            Das spezifische Listpicker Model.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class ListpickerController<T extends ListpickerModel<?>> implements GuiController {

    /**
     * Filtert das Model.
     *
     * @param model
     *            Das Model.
     */
    public abstract void filter(T model);

}
