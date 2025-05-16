package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.List;

/**
 * Das DatenModell einer Datentabelle bei der alle möglichen Tabelleeinträgen im Hauptspeicher gespeichert
 * werden.
 *
 * @param <I>
 *            Der konkrete Tabelleneintrag.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableInMemoryDataModel<I extends DataTableItem> extends DataTableDataModel<I> {

    private static final long serialVersionUID = 1L;

    /** Die Liste aller Tabelleneinträge. */
    private List<I> allItems;

    @SuppressWarnings("javadoc")
    public List<I> getAllItems() {

        return this.allItems;
    }

    @SuppressWarnings("javadoc")
    public void setAllItems(List<I> allItems) {

        this.allItems = allItems;
    }

}
