package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.Map;

/**
 * Die Parameter einer Datentabelleanfrage an das Backend.
 *
 * Enthält alle benötigte Informationen, wie Filter-, Sortier- und Pagination-Parametern.
 *
 *
 * @see DataTableResult
 * @see DataTableController#readItems(DataTableModel, DataTableRequest)
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableRequest {

    /**
     * Die Filtern, der Schlüssel representiert die Eigenschaft zu filtern und der Wert der Filter zu dieser
     * Eigenschaft.
     */
    private Map<String, String> filter;

    /** Die Nummer des erstes Eintrages zu ermitteln, der aller erste Eintrag hat die Nummer 1. */
    private int itemFrom;

    /** Die Anzahl der Einträge zu ermitteln. */
    private int itemsCount = Integer.MAX_VALUE;

    /** Die Sortierrichtung: ASC oder DESC. */
    private SortDirection sortDirection;

    /** Die Sortiereigenschaft, kann <code>null</code> sein. */
    private String sortProperty;

    @SuppressWarnings("javadoc")
    public Map<String, String> getFilter() {

        return this.filter;
    }

    @SuppressWarnings("javadoc")
    public int getItemFrom() {

        return this.itemFrom;
    }

    @SuppressWarnings("javadoc")
    public int getItemsCount() {

        return this.itemsCount;
    }

    @SuppressWarnings("javadoc")
    public SortDirection getSortDirection() {

        return this.sortDirection;
    }

    @SuppressWarnings("javadoc")
    public String getSortProperty() {

        return this.sortProperty;
    }

    @SuppressWarnings("javadoc")
    public void setFilter(Map<String, String> filter) {

        this.filter = filter;
    }

    @SuppressWarnings("javadoc")
    public void setItemFrom(int itemFrom) {

        this.itemFrom = itemFrom;
    }

    @SuppressWarnings("javadoc")
    public void setItemsCount(int itemCount) {

        this.itemsCount = itemCount;
    }

    @SuppressWarnings("javadoc")
    public void setSortDirection(SortDirection sortDirection) {

        this.sortDirection = sortDirection;
    }

    @SuppressWarnings("javadoc")
    public void setSortProperty(String sortProperty) {

        this.sortProperty = sortProperty;
    }

}
