package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.List;

/**
 * Das Ergebnis einer Datentabelleanfrage an das Backend.
 *
 * Enthält die aktuell anzuzeigenden Tabelleneinträge unter berücksichtigung aller Parameter der
 * zugehörige Anfrage, plus die Anzahl der Einträge ohne Pagination-Einschränkungen.
 *
 * @param <I> Der konkrete Tabelleneintrag
 *
 *
 * @see DataTableRequest
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableResult<I extends DataTableItem> {

    /** Die Anzahl der Einträge ohne Pagination-Einschränkungen. */
    private int itemCount;

    /**
     * Die aktuell anzuzeigenden Tabelleneinträge unter berücksichtigung aller Parameter der
     * zugehörige Anfrage.
     */
    private List<I> items;

    @SuppressWarnings("javadoc")
    public int getItemCount() {

        return itemCount;
    }

    @SuppressWarnings("javadoc")
    public List<I> getItems() {

        return items;
    }

    @SuppressWarnings("javadoc")
    public void setItemCount(int itemCount) {

        this.itemCount = itemCount;
    }

    @SuppressWarnings("javadoc")
    public void setItems(List<I> items) {

        this.items = items;
    }
}
