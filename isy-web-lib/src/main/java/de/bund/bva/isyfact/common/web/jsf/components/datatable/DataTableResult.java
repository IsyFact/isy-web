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
