package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.io.Serializable;

/**
 * Dies ist die Oberklasse für Listpicker Items, welche einem Listpicker-Model zugewiesen werden.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface ListpickerItem extends Serializable {

    /**
     * Gibt eine Listpickerangabe mit dem Wert für das Item zurück. Dieser wird in das zum Listpicker gehörige
     * Feld geschrieben. Wird nur im non-JS Fall benötigt.
     *
     * @return Der Wert.
     */
    public Object getListpickerValueForItem();

    /**
     * Gibt den Wert für das Item zurück. Dieser wird in das zum Listpicker gehörige Feld geschrieben.
     *
     * @return Der Wert.
     */
    public String getValueForItem();

    /**
     * Gibt das Item in lesbarer Form zur Anzeige in Steuerelementen zurück.
     * @return Der Wert.
     */
    public String getReadableValueForItem();

    /**
     * Gibt die anwendungsspezifische CSS-Klasse für das Item zurück.
     * @return Eine CSS-Klasse.
     */
    public String getCssClass();

}
