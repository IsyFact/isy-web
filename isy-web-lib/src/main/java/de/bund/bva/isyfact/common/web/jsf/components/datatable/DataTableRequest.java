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

import java.util.Map;

/**
 * Die Parameter einer Datentabelleanfrage an das Backend.
 *
 * Enthält alle benötigte Informationen, wie Filter-, Sortier- und Pagination-Parametern.
 *
 * @author msg
 *
 * @see DataTableResult
 * @see DataTableController#readItems(DataTableModel, DataTableRequest)
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableRequest {

    /**
     * Die Filtern, der Schlüssel representiert die Eigenschaft zu filtern und der Wert der Filter zu dieser
     * Eigenschaft.
     */
    private Map<String, String> filter;

    /** Die Nummer des erstes Eintrages zu ermitteln, der aller erste Eintrag hat die Nummer 1. */
    private int itemFrom;

    /** Die Anzahl der Einträge zu ermitteln. */
    private int itemsCount = Integer.MAX_VALUE;

    /** Die Sortierrichtung: ASC oder DESC. */
    private SortDirection sortDirection;

    /** Die Sortiereigenschaft, kann <code>null</code> sein. */
    private String sortProperty;

    @SuppressWarnings("javadoc")
    public Map<String, String> getFilter() {

        return this.filter;
    }

    @SuppressWarnings("javadoc")
    public int getItemFrom() {

        return this.itemFrom;
    }

    @SuppressWarnings("javadoc")
    public int getItemsCount() {

        return this.itemsCount;
    }

    @SuppressWarnings("javadoc")
    public SortDirection getSortDirection() {

        return this.sortDirection;
    }

    @SuppressWarnings("javadoc")
    public String getSortProperty() {

        return this.sortProperty;
    }

    @SuppressWarnings("javadoc")
    public void setFilter(Map<String, String> filter) {

        this.filter = filter;
    }

    @SuppressWarnings("javadoc")
    public void setItemFrom(int itemFrom) {

        this.itemFrom = itemFrom;
    }

    @SuppressWarnings("javadoc")
    public void setItemsCount(int itemCount) {

        this.itemsCount = itemCount;
    }

    @SuppressWarnings("javadoc")
    public void setSortDirection(SortDirection sortDirection) {

        this.sortDirection = sortDirection;
    }

    @SuppressWarnings("javadoc")
    public void setSortProperty(String sortProperty) {

        this.sortProperty = sortProperty;
    }

}
