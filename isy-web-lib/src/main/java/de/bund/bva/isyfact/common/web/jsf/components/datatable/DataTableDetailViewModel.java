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
import java.util.HashSet;
import java.util.Set;

/**
 * Verwaltet der Zustand der Detailansicht, bzw. für welche Tabelleneinträge diese aktiv ist.
 *
 * @author msg
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableDetailViewModel implements Serializable {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    /** Die IDs der Items für welches die Detailansicht aktiv ist. */
    private Set<Long> detailViewItems = new HashSet<Long>();

    @SuppressWarnings("javadoc")
    public Set<Long> getDetailViewItems() {

        return detailViewItems;
    }

    /**
     * Prüft ob die Detailansicht eines Tabelleneintrags angezeigt werden soll.
     *
     * @param itemId Die ID des zu überprüfenden Tabelleneintrags.
     * @return <code>true</code>, wenn die Detailansicht angezeigt werden soll, <code>false</code> ansonsten.
     */
    public boolean isDetailView(long itemId) {

        return (getDetailViewItems() != null) && getDetailViewItems().contains(itemId);
    }

    @SuppressWarnings("javadoc")
    public void setDetailViewItems(Set<Long> detailItems) {

        this.detailViewItems = detailItems;
    }
}
