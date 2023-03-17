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
 * Das DatenModell einer Datentabelle bei der alle möglichen Tabelleeinträgen im Hauptspeicher gespeichert
 * werden.
 *
 * @param <I>
 *            Der konkrete Tabelleneintrag.
 *
 * @author msg
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
