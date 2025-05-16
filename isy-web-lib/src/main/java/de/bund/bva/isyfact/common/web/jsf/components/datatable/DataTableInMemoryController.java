package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;

import de.bund.bva.isyfact.common.web.comparator.NullSafeBeanComparator;
import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.jsf.components.datatable.DataTableModel.DatatableOperationMode;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * In-Speicher Implementierung für den allgemeinen Gebrauch.
 * <p>
 * Hier werden alle Mögliche Tabelleneinträge nur einmal aus dem Backend gelesen, und alle Operationen wie
 * Filtern, Sortieren und Paginieren passieren automatisch im Hauptspeicher.
 *
 * @param <I> Der konkrete Tabelleneintrag.
 * @param <M> Das konkrete Modell.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableInMemoryController<I extends DataTableItem, M extends DataTableInMemoryModel<I>>
        extends DataTableController<I, M> {

    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(DataTableInMemoryController.class);

    /**
     * Filtert die Tabelleneinträge anhand den angegebenen Filtern.
     *
     * @param allItems alle Tabelleneinträge
     * @param filters  Menge an Filtern
     * @return Liste der Tabelleneinträge, die zu allen Filtern passen.
     */
    protected List<I> filterDisplayItems(List<I> allItems, Map<String, String> filters) {

        List<I> result = new ArrayList<>(allItems);
        if (filters == null || filters.isEmpty()) {
            // nichts zu filtern
            // nichts zu filtern
            return result;
        }

        Set<Map.Entry<String, String>> nonEmptyFilters = filters.entrySet().stream().filter(
                        filter -> filter.getKey() != null && !filter.getKey().trim().isEmpty()
                                && filter.getValue() != null && !filter.getValue().trim().isEmpty())
                .collect(Collectors.toSet());

        for (Iterator<I> it = result.iterator(); it.hasNext(); ) {
            I item = it.next();
            for (Map.Entry<String, String> filter : nonEmptyFilters) {
                Object value;
                try {
                    value = PropertyUtils.getProperty(item, filter.getKey());
                } catch (Exception e) {
                    IsyFactTechnicalRuntimeException runtimeException =
                            new IsyFactTechnicalRuntimeException("", e);
                    LOG.error(
                            "anscheinend ist die Property {} in dataTable2:facet[tableFilter] falsch konfiguriert",
                            runtimeException, filter.getKey());
                    throw runtimeException;
                }
                if (value == null || !value.toString().toLowerCase()
                        .contains(filter.getValue().toLowerCase())) {
                    // remove not matching item
                    it.remove();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I getItemById(M model, long id) {

        if (model.getDataModel().getAllItems() == null) {
            model.getDataModel().setAllItems(getAllItems(model));
        }
        for (I dataTableItem : model.getDataModel().getAllItems()) {
            if (dataTableItem.getIdentifierForItem() == id) {
                return dataTableItem;
            }
        }
        return null;
    }

    /**
     * Setzt die Items für die Tabelle.
     *
     * <p>
     * Das hat zufolge dass das Backend neu aufgerufen wird.
     * <p>
     *
     * @param model    Modell der DataTable
     * @param allItems Liste aller Tabelleneinträge
     */
    public void setItems(M model, List<I> allItems) {
        model.setAllitems(allItems);
        invalidateModel(model);
        updateDisplayItems(model);
    }

    /**
     * Invalidiert das Data Modell.
     * <p>
     * Hat zufolge dass beim nächsten Aufruf von {@link #updateDisplayItems(DataTableModel)} das Backend neu
     * aufgerufen wird.
     * <p>
     * Um die Tabelle zu aktualisieren, folgendes aufrufen:
     *
     * <pre>
     * controller.invalidateModel(model);
     * controller.updateDisplayItems(model);
     * </pre>
     *
     * @param model das Modell
     */
    public void invalidateModel(M model) {
        if (model.getMode() == DatatableOperationMode.SERVER) {
            model.getDataModel().setAllItems(null);
        }
        model.setSelectionModel(new DataTableSelectionModel());
        model.getPaginationModel().setCurrentPage(1);
    }

    /**
     * Leert die Tabelle.
     * <p>
     * Hilfreich bei z.B. leeren der Suchmaske, wobei die Suchmaske zwar geleert wird, eine Suche mit leeren
     * Suchparametern aber trotzdem Ergebnisse liefert.
     *
     * @param model das Modell
     */
    public void emptyTable(M model) {
        invalidateModel(model);
        model.getDataModel().setAllItems(Collections.emptyList());
        updateDisplayItems(model);
    }

    /**
     * Paginiert die Tabelleneinträge nach den angegebenen Parametern.
     *
     * @param items   Tabelleneinträge
     * @param request Anfrage an die DataTable
     * @return die Tabelleneinträge, die innerhalb der angefragten Seite liegen.
     */
    protected List<I> paginateItems(List<I> items, DataTableRequest request) {

        int itemTo = request.getItemsCount() + request.getItemFrom();
        if ((request.getItemsCount() == Integer.MAX_VALUE) || (items.size() < itemTo)) {
            itemTo = items.size();
        }
        return new ArrayList<>(items.subList(request.getItemFrom(), itemTo));
    }

    @Override
    protected DataTableResult<I> readItems(M model, DataTableRequest request) {

        DataTableResult<I> result = new DataTableResult<I>();

        // 0. check if we have read already the items from backend
        List<I> allItems = model.getDataModel().getAllItems();
        if (allItems == null) {
            allItems = getAllItems(model);
            if (allItems == null) {
                allItems = new ArrayList<>();
            }
            model.getDataModel().setAllItems(allItems);
        }
        if (model.getMode() == DatatableOperationMode.CLIENT) {
            // im CLIENT Mode macht JavaScript alles
            result.setItemCount(allItems.size());
            result.setItems(allItems);
            return result;
        }

        // 1. filter according to filter model
        List<I> items = filterDisplayItems(allItems, request.getFilter());
        // update count
        result.setItemCount(items.size());

        // 2. sort according to sort model
        sortDisplayItems(items, request.getSortProperty(), request.getSortDirection());

        // 3. filter according to pagination model
        items = paginateItems(items, request);

        result.setItems(items);
        return result;
    }

    /**
     * Gibt alle verfügbare Tabelleneinträge zurück, wird nur einmal pro Modell aufgerufen.
     *
     * @param model Das Tabellenmodel
     * @return alle verfügbare Tabelleneinträge
     */
    public List<I> getAllItems(M model) {

        return model.getAllitems();
    }

    /**
     * Sortiert die Tabelleneinträge nach der angegebenen Eigenschaft und Sortierrichtung.
     * <p>
     * Achtung: Die Einträge werden in-place sortiert, es wird keine neue Liste erzeugt.
     * <p>
     * Heißt <code>sortInvoked</code> aus Rückwärtskompatibilitätsgrunde
     *
     * @param items     zu sortierende Tabelleneinträge
     * @param property  Sortiereigenschaft
     * @param direction Sortierrichtung
     */
    protected void sortDisplayItems(List<I> items, String property, SortDirection direction) {
        if (property == null || property.isEmpty()) {
            return;
        }
        boolean isAscending = (SortDirection.ASCENDING == direction);
        Comparator<I> comparator = new NullSafeBeanComparator<>(property);
        items.sort(isAscending ? comparator : Collections.reverseOrder(comparator));
    }
}
