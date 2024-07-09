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
