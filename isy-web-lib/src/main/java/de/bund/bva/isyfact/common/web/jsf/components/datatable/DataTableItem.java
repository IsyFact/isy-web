package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;

/**
 * Ein Tabelleneintrag entspricht einer Zeile in einer Tabelle, also dem repräsentierten Objekt.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface DataTableItem extends Serializable {

    /**
     * Liefert die ID für den Tabelleneintrag zurück.
     *
     * @return Die ID.
     */
    public long getIdentifierForItem();
}
