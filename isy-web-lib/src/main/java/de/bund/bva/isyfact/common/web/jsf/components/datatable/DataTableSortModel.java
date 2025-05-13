package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;

/**
 * Das Sortiermodell einer Tabelle.
 * <p>
 * Das Sortieren nach mehreren Attributen wird nicht unterstützt.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableSortModel implements Serializable {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    /** Die aktuelle Sortierrichtung. */
    private SortDirection direction = SortDirection.ASCENDING;

    /** Die Sortiereigenschaft vom Tabelleneintrag. */
    private String property;

    @SuppressWarnings("javadoc")
    public SortDirection getDirection() {

        return this.direction;
    }

    @SuppressWarnings("javadoc")
    public String getProperty() {

        return this.property;
    }

    @SuppressWarnings("javadoc")
    public void setDirection(SortDirection sortDirection) {

        this.direction = sortDirection;
    }

    @SuppressWarnings("javadoc")
    public void setProperty(String sortProperty) {

        this.property = sortProperty;
    }

}
