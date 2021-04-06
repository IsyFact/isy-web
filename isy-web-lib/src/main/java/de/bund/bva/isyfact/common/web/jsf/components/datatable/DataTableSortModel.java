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

/**
 * Das Sortiermodell einer Tabelle.
 * <p>
 * Das Sortieren nach mehreren Attributen wird nicht unterstützt.
 *
 * @author msg
 */
public class DataTableSortModel implements Serializable {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    /** Die aktuelle Sortierrichtung. */
    private SortDirection direction = SortDirection.ASCENDING;

    /** Die Sortiereigenschaft vom Tabelleneintrag. */
    private String property;

    @SuppressWarnings("javadoc")
    public SortDirection getDirection() {

        return this.direction;
    }

    @SuppressWarnings("javadoc")
    public String getProperty() {

        return this.property;
    }

    @SuppressWarnings("javadoc")
    public void setDirection(SortDirection sortDirection) {

        this.direction = sortDirection;
    }

    @SuppressWarnings("javadoc")
    public void setProperty(String sortProperty) {

        this.property = sortProperty;
    }

}
