package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.List;

/**
 * Das Modell eines Tabellenkontrollers bei der alle möglichen Tabelleeinträgen im Hauptspeicher gespeichert
 * werden.
 *
 *
 * @param <I>
 *            Der konkrete Tabelleneintrag.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableInMemoryModel<I extends DataTableItem> extends DataTableModel<I> {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    private DataTableInMemoryDataModel<I> dataModel = new DataTableInMemoryDataModel<I>();

    @Override
    public DataTableInMemoryDataModel<I> getDataModel() {

        return this.dataModel;
    }

    @Override
    public void setDataModel(DataTableDataModel<I> dataModel) {

        this.dataModel = (DataTableInMemoryDataModel<I>) dataModel;
    }

    @SuppressWarnings("javadoc")
    public List<I> getAllitems() {

        return this.dataModel.getAllItems();
    }

    @SuppressWarnings("javadoc")
    public void setAllitems(List<I> allItems) {

        this.dataModel.setAllItems(allItems);
    }
}
