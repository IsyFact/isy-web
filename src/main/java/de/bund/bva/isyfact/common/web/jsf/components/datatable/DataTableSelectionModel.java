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
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.Sets;

/**
 * Das Auswahlmodell einer Tabelle.
 *
 * @author Michael Moossen, msg
 */
public class DataTableSelectionModel implements Serializable {

    /**
     * Damit die Tabelle auch ohne JS funktioniert müssen wir JSF glauben lassen, dass wir einen Boolean pro
     * Checkbox verwalten.
     *
     * @author Michael Moossen, msg
     */
    protected final class SelectionMapper extends AbstractMap<Long, Boolean> {

        @Override
        public Set<Map.Entry<Long, Boolean>> entrySet() {

            HashSet<Map.Entry<Long, Boolean>> set = new HashSet<Map.Entry<Long, Boolean>>();
            for (Long id : getSelectedItems()) {
                set.add(new ImmutablePair<Long, Boolean>(id, Boolean.TRUE));
            }
            return set;
        }

        @Override
        public Boolean get(Object key) {

            if (key instanceof Long) {
                return getSelectedItems().contains(key);
            }
            return Boolean.FALSE;
        }

        @Override
        public Boolean put(Long key, Boolean value) {

            if (value == null || value == Boolean.FALSE) {
                return getSelectedItems().remove(key);
            }
            return getSelectedItems().add(key);
        }
    }

    private static final long serialVersionUID = 1L;

    /** Die ID vom Item für welches ein Doppelklick ausgeführt wurde. */
    private Long doubleClickSelectedItem;

    /** Der Wert des Select-All Buttons. */
    private boolean selectAll;

    private transient Map<Long, Boolean> selected;

    /** Die IDs der ausgewählten Items. */
    private Set<Long> selectedItems = new HashSet<Long>();

    @SuppressWarnings("javadoc")
    public Long getDoubleClickSelectedItem() {

        return this.doubleClickSelectedItem;
    }

    @SuppressWarnings("javadoc")
    public Map<Long, Boolean> getSelected() {

        if (this.selected == null) {
            this.selected = new SelectionMapper();
        }
        return this.selected;
    }

    @SuppressWarnings("javadoc")
    public Set<Long> getSelectedItems() {

        return this.selectedItems;
    }

    @SuppressWarnings("javadoc")
    public boolean isSelectAll() {

        return this.selectAll;
    }

    @SuppressWarnings("javadoc")
    public void setDoubleClickSelectedItem(Long doubleClickSelectedItem) {

        this.doubleClickSelectedItem = doubleClickSelectedItem;
    }

    @SuppressWarnings("javadoc")
    public void setSelectAll(boolean selectAll) {

        this.selectAll = selectAll;
    }

    @SuppressWarnings("javadoc")
    public void setSelectedItems(Set<Long> selectedItems) {

        this.selectedItems = selectedItems;
    }

    public void setSelectedItem(Long item) {
        this.selectedItems = Sets.newHashSetWithExpectedSize(1);
        if (item != null) {
            this.selectedItems.add(item);
        }
    }
}
