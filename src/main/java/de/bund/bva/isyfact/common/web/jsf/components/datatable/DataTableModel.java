/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Das Modell eines Tabellenkontrollers.
 * <p>
 * Enthält der aktuelle Zustand der Tabelle.
 * <ul>
 * <li>Im {@link #getDetailViewModel()} findet man für welche Tabelleneinträge die Detailansicht aktiv ist.</li>
 * <li>Im {@link #getPaginationModel()} findet man den Zustand der Paginierung.</li>
 * <li>Im {@link #getFilterModel()} findet man den Zustand der Filter-Zeile.</li>
 * <li>Im {@link #getSelectionModel()} findet man alles über selektierte Tabelleneinträge.</li>
 * <li>Im {@link #getSortModel()} findet man den Zustand der Sortierung der Tabelle.</li>
 * <li>Im {@link #getDataModel()} findet man die aktuell angezeigte Tabelleneinträge.</li>
 * </ul>
 *
 * @author msg
 * @author Capgemini
 *
 * @param <I>
 *            Der konkrete Tabelleneintrag.
 */
public class DataTableModel<I extends DataTableItem> implements Serializable {

    /**
     * Ob die Datatable Client- oder Server-seitig funktioniert.
     * <p>
     * Dies betrifft folgende Funktionen:
     * <ul>
     * <li>Paginierung</li>
     * <li>Sortierung</li>
     * <li>Filterzeile</li>
     * <li>Detailauswahl</li>
     * </ul>
     *
     * @author msg
     */
    public enum DatatableOperationMode {
        /** Die Tabelle wird client-seitig betrieben */
        CLIENT,
        /** Die Tabelle wird server seitig betrieben. */
        SERVER;
    }

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    private DataTableDataModel<I> dataModel = new DataTableDataModel<I>();

    private DataTableDetailViewModel detailViewModel = new DataTableDetailViewModel();

    private DataTableFilterModel filterModel = new DataTableFilterModel();

    private DatatableOperationMode mode = DatatableOperationMode.CLIENT;

    private DataTablePaginationModel paginationModel = new DataTablePaginationModel();

    private DataTableSelectionModel selectionModel = new DataTableSelectionModel();

    private DataTableSortModel sortModel = new DataTableSortModel();

    @SuppressWarnings("javadoc")
    public DataTableDataModel<I> getDataModel() {

        return this.dataModel;
    }

    @SuppressWarnings("javadoc")
    public DataTableDetailViewModel getDetailViewModel() {

        return this.detailViewModel;
    }

    @SuppressWarnings("javadoc")
    public DataTableFilterModel getFilterModel() {

        return this.filterModel;
    }

    @SuppressWarnings("javadoc")
    public DatatableOperationMode getMode() {

        return this.mode;
    }

    @SuppressWarnings("javadoc")
    public DataTablePaginationModel getPaginationModel() {

        return this.paginationModel;
    }

    @SuppressWarnings("javadoc")
    public DataTableSelectionModel getSelectionModel() {

        return this.selectionModel;
    }

    @SuppressWarnings("javadoc")
    public DataTableSortModel getSortModel() {

        return this.sortModel;
    }

    @SuppressWarnings("javadoc")
    public void setDataModel(DataTableDataModel<I> dataModel) {

        this.dataModel = dataModel;
    }

    @SuppressWarnings("javadoc")
    public void setDetailViewModel(DataTableDetailViewModel detailViewModel) {

        this.detailViewModel = detailViewModel;
    }

    @SuppressWarnings("javadoc")
    public void setFilterModel(DataTableFilterModel filterModel) {

        this.filterModel = filterModel;
    }

    @SuppressWarnings("javadoc")
    public void setMode(DatatableOperationMode mode) {

        this.mode = mode;
    }

    @SuppressWarnings("javadoc")
    public void setPaginationModel(DataTablePaginationModel paginationModel) {

        this.paginationModel = paginationModel;
    }

    @SuppressWarnings("javadoc")
    public void setSelectionModel(DataTableSelectionModel selectionModel) {

        this.selectionModel = selectionModel;
    }

    @SuppressWarnings("javadoc")
    public void setSortModel(DataTableSortModel sortModel) {

        this.sortModel = sortModel;
    }

    /**
     * Liefert die aktuell angezeigte Tabelleneinträge.
     *
     * @return alle aktuell angezeigte Tabelleneinträge
     *
     * @deprecated: nur für Rückwärtskompatibilität. wenn Du glaubst die Tabelleneinträge selbst verwalten zu
     *              müssen, dann bitte direkt das {@link DataTableDataModel} Untermodel benutzen:
     *              <code>model.getDataModel().getDisplayItems()</code> Aber sei bewusst dass es nun 2
     *              unterschiedliche Mengen an Tabelleneinträge gibt, zum einem die gerade Angezeigten, d.h.
     *              nach Sortieren, Filtern und Paginieren, und alle Tabelleneinträge: diese sind aber i.A.
     *              nicht verfügbar sondern von der Methode
     *              {@link DataTableController#readItems(DataTableModel, DataTableRequest)} verwaltet, nur im
     *              {@link DataTableInMemoryDataModel} gibt es auch die <code>allItems</code> Eigenschaft.
     */
    @Deprecated
    public List<I> getDataTableItems() {

        return getDataModel().getDisplayItems();
    }

    /**
     * Liefert das Sortierattribut.
     *
     * @return Das Sortierattribut
     *
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSortModel} Untermodel benutzen
     *              <code>model.getSortModel().getProperty()</code>
     */
    @Deprecated
    public String getSortAttribute() {

        return getSortModel().getProperty();
    }

    /**
     * Setzt das Sortierattribut.
     *
     * @param property
     *            Das Sortierattribut
     *
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSortModel} Untermodel benutzen
     *              <code>model.getSortModel().setProperty(property)</code>
     */
    @Deprecated
    public void setSortAttribute(String property) {

        getSortModel().setProperty(property);
    }

    /**
     * Liefert die Sortierrichtung.
     *
     * @return Die Sortierrichtung
     *
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSortModel} Untermodel benutzen
     *              <code>model.getSortModel().getDirection()</code>
     */
    @Deprecated
    public SortDirection getSortDirection() {

        return getSortModel().getDirection();
    }

    /**
     * Setzt die Sortierrichtung.
     *
     * @param direction
     *            Die neue Sortierrichtung
     *
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSortModel} Untermodel benutzen
     *              <code>model.getSortModel().setDirection(direction)</code>
     */
    @Deprecated
    public void setSortDirection(SortDirection direction) {

        getSortModel().setDirection(direction);
    }

    /**
     * Liefert die Anzahl der aktuell angezeigten Tabelleneinträge.
     *
     * @return Anzahl der aktuell angezeigten Tabelleneinträge
     *
     * @deprecated: nur für Rückwärtskompatibilität. bitte direkt das {@link DataTableDataModel} Untermodel
     *              benutzen: <code>model.getDataModel().getDisplayItems().size()</code> Aber sei bewusst dass
     *              es nun 3 unterschiedliche Mengen an Tabelleneinträge gibt, zum einem die gerade
     *              Angezeigten, d.h. nach Sortieren, Filtern und Paginieren, dann hat man auch Zugriff auf
     *              die Anzahl der Tabelleneinträge nach dem Filtern und vor dem Paginieren, und alle
     *              Tabelleneinträge: diese sind aber i.A. nicht verfügbar sondern von der Methode
     *              {@link DataTableController#readItems(DataTableModel, DataTableRequest)} verwaltet, nur im
     *              {@link DataTableInMemoryDataModel} gibt es auch die <code>allItems</code> Eigenschaft.
     */
    @Deprecated
    public long getItemCount() {

        return getDataModel().getDisplayItems().size();
    }

    /**
     * Gibt die Liste an aktuell ausgewählten Items zurück.
     *
     * @return Die Liste an Items, leer, falls keine Elemente ausgewählt sind.
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSelectionModel} Untermodel
     *              benutzen <code>model.getSelectionModel().getSelectedItems()</code>
     */
    @Deprecated
    public List<Long> getSelectedItems() {

        return new ArrayList<Long>(getSelectionModel().getSelectedItems());
    }

    /**
     * Gibt das Item mit der angegebenen ID aus dem aktuellen Model zurück.
     *
     * @param itemId
     *            Die Item-ID.
     * @return Das Item oder <code>null</code>, falls es nicht vorhanden ist.
     *
     * @deprecated: nur für Rückwärtskompatibilität. Diese Methode ist nun im {@link DataTableController}.
     */
    @Deprecated
    public DataTableItem getItemById(Long itemId) {

        if (getDataTableItems() == null) {
            return null;
        }

        for (DataTableItem dataTableItem : getDataTableItems()) {
            if (dataTableItem.getIdentifierForItem() == itemId) {
                return dataTableItem;
            }
        }

        return null;
    }

    /**
     * Gibt die ID vom Item für welches ein Doppelklick ausgeführt wurde zurück.
     *
     * @return die ID vom Item für welches ein Doppelklick ausgeführt wurde
     * @deprecated: nur für Rückwärtskompatibilität. Bitte das {@link DataTableSelectionModel} Untermodel
     *              benutzen <code>model.getSelectionModel().getSelectedItems()</code>
     */
    @Deprecated
    public Long getDoubleClickSelectedItem() {

        return getSelectionModel().getDoubleClickSelectedItem();
    }

    /**
     * Setzt die aktuell angezeigte Tabelleneinträge.
     *
     * @param items
     *            alle aktuell angezeigte Tabelleneinträge
     *
     * @deprecated: nur für Rückwärtskompatibilität. wenn Du glaubst die Tabelleneinträge selbst verwalten zu
     *              müssen, dann bitte direkt das {@link DataTableDataModel} Untermodel benutzen:
     *              <code>model.getDataModel().setDisplayItems(items)</code> Aber sei bewusst dass es nun 2
     *              unterschiedliche Mengen an Tabelleneinträge gibt, zum einem die gerade Angezeigten, d.h.
     *              nach Sortieren, Filtern und Paginieren, und alle Tabelleneinträge: diese sind aber i.A.
     *              nicht verfügbar sondern von der Methode
     *              {@link DataTableController#readItems(DataTableModel, DataTableRequest)} verwaltet, nur im
     *              {@link DataTableInMemoryDataModel} gibt es auch die <code>allItems</code> Eigenschaft.
     */
    @Deprecated
    public void setDataTableItems(ArrayList<I> items) {

        getDataModel().setDisplayItems(items);
    }

    /**
     * Setzt die Anzahl der aktuell angezeigten Tabelleneinträge.
     *
     * @param itemCount
     *            Anzahl der aktuell angezeigten Tabelleneinträge
     *
     * @deprecated: nur für Rückwärtskompatibilität. Jetzt gibt es die Möglichkeit nicht dieser Wert selbst zu
     *              verwalten, jetzt wird es so berechnet:
     *              <code>model.getDataModel().getDisplayItems().size()</code> Aber sei bewusst dass es nun 3
     *              unterschiedliche Mengen an Tabelleneinträge gibt, zum einem die gerade Angezeigten, d.h.
     *              nach Sortieren, Filtern und Paginieren, dann hat man auch Zugriff auf die Anzahl der
     *              Tabelleneinträge nach dem Filtern und vor dem Paginieren, und alle Tabelleneinträge: diese
     *              sind aber i.A. nicht verfügbar sondern von der Methode
     *              {@link DataTableController#readItems(DataTableModel, DataTableRequest)} verwaltet, nur im
     *              {@link DataTableInMemoryDataModel} gibt es auch die <code>allItems</code> Eigenschaft.
     */
    @Deprecated
    public void setItemCount(int itemCount) {

        // macht nichts
    }
}
