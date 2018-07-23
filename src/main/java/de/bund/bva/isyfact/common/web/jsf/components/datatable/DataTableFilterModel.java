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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Das Filtermodell einer Tabelle mit Filter-Zeile.
 *
 * @author Michael Moossen, msg
 */
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
