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
