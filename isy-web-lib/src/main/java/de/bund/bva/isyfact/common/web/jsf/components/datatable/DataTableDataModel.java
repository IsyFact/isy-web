package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Das DatenModell einer Datentabelle.
 * Das Datenmodell enthält eine Liste von aktuell angezeigten Tabelleneinträge.
 *
 * @param <I> Der konkrete Tabelleneintrag.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableDataModel<I extends DataTableItem> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Dia Anzahl der gefilterten(bzw. alle) Tabelleneinträge. */
    private int filteredItemCount;

    /** Die Liste an aktuell geladenen und anzuzeigenden Tabelleneinträge. */
    private List<I> displayItems = new ArrayList<I>();

    /**
     * Die Anzahl an gefilterten(bzw. insgesamt vorhandenen) Tabelleneinträge, d.h. nicht zwingend die Anzahl
     * der geladenen Tabelleneinträge aus {@link #displayItems}.
     *
     * @return die Anzahl an insgesamt vorhandenen Items
     */
    public int getFilteredItemCount() {

        return filteredItemCount;
    }

    @SuppressWarnings("javadoc")
    public List<I> getDisplayItems() {

        return displayItems;
    }

    @SuppressWarnings("javadoc")
    public void setFilteredItemCount(int itemCount) {

        this.filteredItemCount = itemCount;
    }

    @SuppressWarnings("javadoc")
    public void setDisplayItems(List<I> items) {

        this.displayItems = items;
    }
}
