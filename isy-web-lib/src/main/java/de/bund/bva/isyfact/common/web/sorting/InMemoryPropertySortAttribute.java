package de.bund.bva.isyfact.common.web.sorting;

import de.bund.bva.isyfact.common.web.jsf.components.datatable.SortAttribute;

/**
 * Eine Schnittstelle für Enums des Typs {@link SortAttribute}. Dieses dient zum generischen Sortieren von
 * Listen.
 *
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface InMemoryPropertySortAttribute {

    /**
     * Gibt den Propertynamen zurück, nachdem sortiert werden soll.
     * @return Der Propertyname.
     */
    public String getSortProperty();
}
