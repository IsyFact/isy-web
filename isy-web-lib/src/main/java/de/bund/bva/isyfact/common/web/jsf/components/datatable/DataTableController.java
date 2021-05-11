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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Strings;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.comparator.NullSafeBeanComparator;
import de.bund.bva.isyfact.common.web.jsf.components.datatable.DataTableModel.DatatableOperationMode;
import de.bund.bva.isyfact.common.web.jsf.components.datatable.DataTablePaginationModel.PaginationType;

/**
 * Der Kontroller für die JSF Datentabelle Komponente (<code>isy:datatable</code>).
 *
 * @param <I>
 *            Der konkrete Tabelleneintrag.
 * @param <M>
 *            Das konkrete Modell.
 *
 * @author msg
 * @author Capgemini
 */
public abstract class DataTableController<I extends DataTableItem, M extends DataTableModel<I>>
    implements GuiController {

    /** Standardgröße einer Seite. */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Gibt das Tabelleneintrag mit der angegebenen ID zurück.
     * <p>
     * Diese Methode kann aus Rückwärtskompatibilitätsgründe nicht abstract sein sollte aber von alle neue
     * Kontroller implementiert werden.
     * <p>
     * Eigentlich müsste diese Methode immer vom Kontroller implementiert werden und im Backend suchen, da man
     * hier nur Zugriff auf die gerade angezeigten Daten hat und nicht auf den ganzen Datenbestand.
     * <p>
     * <em><strong>Hinweis:</strong>
     * Wenn man das {@link DataTableInMemoryController} und {@link DataTableInMemoryDataModel}, oder
     * {@link DatatableOperationMode#CLIENT}) benutzt oder eine einheitliche ID benutzt wird, kann man
     * diesen Hinweis ignorieren.
     * <p>
     * Ansonsten wird hier die Frontend-ID übergeben, weil der normalen Anwendungsfall ist, die ID aus
     * des {@link DataTableSelectionModel} hier zu übergeben, um den ganzen Tabelleneintrag zu bekommen.
     * Leider brauchen wir hier aber teilweise die Backend-ID um im Backend zu suchen, falls den
     * selektierten Item nicht mehr sichtbar ist.
     * <p>
     * Deswegen wird man in der Regel keinen Tabelleneintrag finden, was nicht gerade angezeigt wird.
     * <p>
     * Die beste Lösung dafür wäre die ganze {@link DataTableItem} Objekte für die Selektion zu behalten,
     * und nicht nur die IDs.
     * </em>
     *
     * @param model
     *            Das Tabellenmodell
     * @param itemId
     *            Die Frontend-ID des gewünschten Tabelleneintrags
     *
     * @return Der Tabelleneintrag oder <code>null</code>, falls nicht vorhanden
     */
    public I getItemById(M model, long itemId) {

        for (I dataTableItem : model.getDataModel().getDisplayItems()) {
            if (dataTableItem.getIdentifierForItem() == itemId) {
                return dataTableItem;
            }
        }
        return null;
    }

    /**
     * Versteckt die Details eines Eintrags in der Tabelle.
     *
     * @param model
     *            Das Tabellenmodel
     * @param itemId
     *            Die ID des Eintrags welche Details versteckt werden sollen
     */
    public void hideDetails(M model, long itemId) {

        model.getDetailViewModel().getDetailViewItems().remove(itemId);
    }

    /**
     * Initialisert das Model beim initialen Aufruf des Flows. Diese Methode sollte nur einmal beim Start des
     * Flows (on-start) aufgerufen werden.
     * <p>
     * Diese Methode kann aus Rückwärtskompatibilitätsgründe nicht abstract sein sollte aber von alle neue
     * Kontroller implementiert werden.
     *
     * @param model
     *            Das zu-inisialisierende Tabellenmodel
     */
    public void initialisiereModel(M model) {

        if (model != null) {
            model.getPaginationModel().setPaginatorSize(DEFAULT_PAGE_SIZE);
            model.getPaginationModel().setType(PaginationType.NORMAL);
            model.setMode(DatatableOperationMode.CLIENT);
        }
    }

    /**
     * Führt eine Paginierung durch.
     *
     * @param model
     *            Das Tabellenmodel
     * @param currentPage
     *            die neue aktuelle Seite
     *
     * @see #updateDisplayItems(DataTableModel)
     */
    public void paginate(M model, int currentPage) {

        if ((currentPage < 1) || (currentPage > model.getPaginationModel().getPageCount())) {
            // sanity check
            return;
        }
        if (currentPage == model.getPaginationModel().getCurrentPage()) {
            // save CPU
            return;
        }
        model.getPaginationModel().setCurrentPage(currentPage);
        updateDisplayItems(model);
    }

    /**
     * Setzt alle Filter zurück.
     *
     * @param model
     *            Das Tabellenmodel
     *
     * @see #filter(DataTableModel)
     */
    public void filterClear(M model) {

        model.getFilterModel().getFilters().clear();
        filter(model);
    }

    /**
     * Führt eine Filterung mit dem angegebenen Filtermodell durch.
     *
     * @param model
     *            Das Tabellenmodel
     *
     * @see #updateDisplayItems(DataTableModel)
     */
    public void filter(M model) {

        // Aktuelle Seite zurücksetzen
        model.getPaginationModel().setCurrentPage(1);
        // Inhalt aktualisieren
        updateDisplayItems(model);
    }

    /**
     * Zeigt die Details eines Eintrags in der Tabelle an.
     *
     * @param model
     *            Das Tabellenmodel
     * @param itemId
     *            Die ID des Eintrags welche Details angezeigt werden sollen
     * @param allowMultipleDetailViews
     *            Ob mehrere Detailansichten erlaubt sind oder nicht
     */
    public void showDetails(M model, long itemId, boolean allowMultipleDetailViews) {

        if (!allowMultipleDetailViews) {
            // Falls notwendig, muss die Detailansicht der aktuellen Items geschlossen werden
            model.getDetailViewModel().getDetailViewItems().clear();
        }
        model.getDetailViewModel().getDetailViewItems().add(itemId);
    }

    /**
     * Führt eine Sortierung durch.
     *
     * @param model
     *            Das Tabellenmodel
     * @param sortAttribute
     *            das neue Sortierattribut
     *
     * @see #updateDisplayItems(DataTableModel)
     */
    public void sort(M model, String sortAttribute) {

        if (!sortAttribute.equals(model.getSortModel().getProperty())) {
            // Wenn sich das Sortierattribut geändert hat, dann wird standardmäßig aufsteigend
            // sortiert
            model.getSortModel().setDirection(SortDirection.ASCENDING);
            // Das neue Sortierattribut setzen
            model.getSortModel().setProperty(sortAttribute);
        } else {
            // Ansonsten wird die Richtung invertiert
            if (SortDirection.ASCENDING.equals(model.getSortModel().getDirection())) {
                model.getSortModel().setDirection(SortDirection.DESCENDING);
            } else {
                model.getSortModel().setDirection(SortDirection.ASCENDING);
            }
        }
        if (model.getPaginationModel().getType() == PaginationType.NORMAL) {
            // Aktuelle Seite zurücksetzen
            model.getPaginationModel().setCurrentPage(1);
        }
        updateDisplayItems(model);
    }

    /**
     * Liefert die Nummer vom ersten anzuzeigenden Tabelleneintrag zurück.
     *
     * @param paginationModel
     *            das Paginationmodell
     * @return die Nummer vom ersten anzuzeigenden Tabelleneintrag
     */
    protected int getFirstDisplayItem(DataTablePaginationModel paginationModel) {

        int low = 0;
        if (paginationModel.getType() == PaginationType.NORMAL) {
            low = (paginationModel.getCurrentPage() - 1) * paginationModel.getPageSize();
        }
        return low;
    }

    /**
     * Liefert die maximale Anzahl der anzuzeigenden Tabelleneinträge zurück.
     *
     * Falls alle Einträge anzuzeigen sind, wird <code>Integer.MAX_VALUE</code> benutzt.
     *
     * @param paginationModel
     *            das Paginationmodell
     * @return die Anzahl der maximal anzuzeigenden Tabelleneinträge
     */
    protected int getCountDisplayItems(DataTablePaginationModel paginationModel) {

        int high = Integer.MAX_VALUE;
        if (paginationModel.getType() != PaginationType.NONE) {
            high = paginationModel.getCurrentPage() * paginationModel.getPageSize();
            high -= getFirstDisplayItem(paginationModel);
        }
        return high;
    }

    /**
     * Setzt die Tabelleneinträge, die unter alle Bedingungen (wie z.B. filter, sort und pagination) angezeigt
     * werden sollen, im Datenmodel.
     * <p>
     * Diese Methode muss mindestens folgende Methoden aufrufen:
     * <ul>
     * <li>{@link DataTableDataModel#setDisplayItems(List)}
     * <li>{@link DataTableDataModel#setFilteredItemCount(int)}
     * <li>{@link DataTablePaginationModel#setPageCount(int)}
     * </ul>
     *
     * @param model
     *            das Tabellenmodell
     */
    public void updateDisplayItems(M model) {

        search(model);
        sortInvoked(model);
    }

    /**
     * Gibt alle ausgewählte Tabelleneinträge zurück.
     *
     * @param model
     *            das Tabellenmodell
     *
     * @return Liste der ausgewählten Tabelleneinträge
     *
     * @see #getSelectedItem(DataTableModel)
     */
    public List<I> getSelectedItems(M model) {

        List<I> selectedItems = new ArrayList<I>();
        for (Long id : model.getSelectionModel().getSelectedItems()) {
            selectedItems.add(getItemById(model, id));
        }
        return selectedItems;
    }

    /**
     * Gibt ein ausgewähltes Tabelleneinträg zurück.
     *
     * @param model
     *            das Tabellenmodell
     *
     * @return Ein ausgewähltes Tabelleneinträg, oder <code>null</code>
     *
     * @see #getSelectedItems(DataTableModel)
     */
    public I getSelectedItem(M model) {

        for (Long id : model.getSelectionModel().getSelectedItems()) {
            return getItemById(model, id);
        }
        return null;
    }

    /**
     * Nur als Rückwärtskompatibilitätsgründe noch da.
     *
     * @param model
     *            das Tabellenmodel
     *
     * @deprecated {@link #updateDisplayItems(DataTableModel)} benutzen.
     */
    @Deprecated
    public void sortInvoked(M model) {
        if (Strings.nullToEmpty(model.getSortModel().getProperty()).trim().isEmpty()) {
            return;
        }
        boolean isAscending = (SortDirection.ASCENDING == model.getSortModel().getDirection());
        Comparator<I> comparator = new NullSafeBeanComparator<>(model.getSortModel().getProperty());
        model.getDataModel().getDisplayItems()
            .sort(isAscending ? comparator : Collections.reverseOrder(comparator));
    }

    /**
     * Nur als Rückwärtskompatibilitätsgründe noch da.
     *
     * @param model
     *            das Tabellenmodel
     *
     * @deprecated {@link #updateDisplayItems(DataTableModel)} benutzen.
     */
    @Deprecated
    public void search(M model) {

        DataTablePaginationModel paginationModel = model.getPaginationModel();

        DataTableRequest request = new DataTableRequest();
        HashMap<String, String> filters = new HashMap<String, String>();
        request.setFilter(filters);
        // Im Client-Mode müssen alle Tabelleneinträge geliefert werden
        if (model.getMode() == DatatableOperationMode.SERVER) {
            filters.putAll(model.getFilterModel().getFilters());
            request.setSortDirection(model.getSortModel().getDirection());
            request.setSortProperty(model.getSortModel().getProperty());
            request.setItemFrom(getFirstDisplayItem(paginationModel));
            request.setItemsCount(getCountDisplayItems(paginationModel));
        }

        DataTableResult<I> result = readItems(model, request);

        model.getDataModel().setDisplayItems(result.getItems());
        model.getDataModel().setFilteredItemCount(result.getItemCount());

        if (model.getMode() == DatatableOperationMode.SERVER
            && paginationModel.getType() != PaginationType.NONE) {
            // Aktualisiert die Anzahl der Seiten durch Angabe der Anzahl der Items.
            // Im Client-Mode passiert dass im Client
            paginationModel.setPageCount(
                (model.getDataModel().getFilteredItemCount() + paginationModel.getPageSize() - 1)
                    / paginationModel.getPageSize());
        }
    }

    /**
     * Diese Methode ist die allgemeine Schnittstelle zum Backend.
     * <p>
     * Diese Methode kann aus Rückwärtskompatibilitätsgründe nicht abstract sein sollte aber von alle neue
     * Kontroller implementiert werden.
     * <p>
     * Im allgemeinen muss hier das Backend alle Tabellen-Operationen, wie z.B. Sortieren, Paginieren und
     * Filtern handhaben.
     * <p>
     * In SQL könnte die entsprechende Query so aussehen:
     *
     * <pre>
     * SELECT
     *   [field1], [field2], ..., [fieldN]
     * FROM
     *   [TABLE]
     * WHERE
     *   [field FOR filter.key1] LIKE '%[filter.value1]%' ESCAPE '\' AND
     *   ...
     *   [field FOR filter.keyM] LIKE '%[filter.valueM]%' ESCAPE '\'
     * ORDER BY [sortProperty] [sortDirection]
     * OFFSET [itemFrom] ROWS
     * FETCH NEXT [itemsCount] ROWS ONLY;
     * </pre>
     *
     * Wobei die Filterwerte so gescaped werden könnten:
     *
     * <pre>
     * filter.value.replace(&quot;%&quot;, &quot;\\%&quot;).replace(&quot;_&quot;, &quot;\\_&quot;)
     * </pre>
     *
     * @param model
     *            Das Tabellenmodell
     * @param request
     *            Der Request mit alle wichtigten Parametern für die Paginierung, Sortierung und Filterung
     * @return Das Ergebnis aus dem Backend
     *
     * @see "http://stackoverflow.com/questions/470542/how-do-i-limit-the-number-of-rows-returned-by-an-oracle-query-after-ordering"
     * @see "http://docs.oracle.com/cd/B12037_01/server.101/b10759/conditions016.htm"
     */
    protected DataTableResult<I> readItems(M model, DataTableRequest request) {

        // sollte immer überschrieben werden
        return null;
    }
}
