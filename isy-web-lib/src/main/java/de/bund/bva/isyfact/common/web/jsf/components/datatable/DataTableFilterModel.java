package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Das Filtermodell einer Tabelle mit Filter-Zeile.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableFilterModel implements Serializable {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    /** Menge der Filter. */
    private Map<String, String> filters = new HashMap<>();

    /**
     * Liefert die eingegebene Filter zurück.
     *
     * Im <code>key</code> ist die Property (eine Eigenschaft des Tabelleneintrags, z.B. <code>name</code>)
     * gespeichert, und im <code>value</code> das dazugehörige Filter.
     *
     * @return die eingegebene Filter
     */
    public Map<String, String> getFilters() {
        return filters;
    }

    /**
     * Prüft ob keine Filter gesetzt sind.
     * <p>
     * Es macht mehr als <code>filters.isEmpty()</code> da auch einen leere Filters als nicht vorhanden
     * interpretiert werden.
     *
     * @return <code>true</code> falls keine Filter gesetzt sind, sonst <code>false</code>
     */
    public boolean isEmpty() {
        return filters.values().stream().filter(Objects::nonNull).map(String::trim).allMatch(String::isEmpty);
    }

    @SuppressWarnings("javadoc")
    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }
}
