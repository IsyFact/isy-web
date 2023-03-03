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
package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * Das Model für den Listpicker (rf:formListpicker).
 *
 * @param <T>
 *            Das spezifische ListpickerItem.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class ListpickerModel<T extends ListpickerItem> implements Serializable {

    /**
     * Die Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die Liste an auswählbaren Items.
     */
    private List<T> items = new ArrayList<>();

    /**
     * Die Gesamtanzahl an Items.
     */
    private int itemCount;

    /**
     * Der übermittelte Filter.
     */
    private String filter;

    /**
     * Die Nachricht, welche ausgegeben wird, falls weitere Elemente vorhanden sind.
     */
    private String messageMoreElements;

    /**
     * Gibt die Property-Keys für die anzugebenden Spalten im Listpicker zurück. Die Schlüssel entsprechen den
     * Property-Namen im spezifischen Listpicker-Item.
     *
     * @return Die geordnete Liste an Property-Keys.
     */
    public abstract List<String> getColumnPropertyKeys();

    /**
     * Gibt die Items als Select-Item Liste zur Nutzung in Widgets aus. Die Methode kann in einem spezifischen
     * ListpickerModel überschrieben werden.
     * @return Die Select-Item Liste
     */
    public List<SelectItem> getItemsAsSelectItems() {

        List<SelectItem> selectItems = new ArrayList<>();

        for (T item : this.items) {
            selectItems.add(new SelectItem(item.getValueForItem(), item.getReadableValueForItem()));
        }

        return selectItems;
    }

    /**
     * Gibt die Items als Select-Item Liste zur Nutzung in Widgets aus wenn JavaScript deaktivert ist. Die
     * Methode kann in einem spezifischen ListpickerModel überschrieben werden.
     * @return Die Select-Item Liste
     */
    public List<SelectItem> getItemsAsSelectItemsNonJs() {

        List<SelectItem> selectItems = new ArrayList<>();

        // Leeres SelectItem hinzufügen
        SelectItem nullSelect = new SelectItem(null, "");
        nullSelect.setNoSelectionOption(true);
        selectItems.add(nullSelect);

        for (T item : this.items) {
            selectItems.add(new SelectItem(item.getListpickerValueForItem(),
                item.getValueForItem() + " - " + item.getReadableValueForItem()));
        }

        return selectItems;
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getMessageMoreElements() {
        return this.messageMoreElements;
    }

    public void setMessageMoreElements(String messageMoreElements) {
        this.messageMoreElements = messageMoreElements;
    }

    /**
     * Gebt den Wert für bestimmten Schlüssel zurück.
     *
     * @param schluessel
     *            der Schlüssel
     *
     * @return gebt den Wert für bestimmten Schlüssel zurück.
     */
    public String getWert(String schluessel) {

        String wert = "";
        for (T item : getItems()) {
            if (item.getValueForItem().equals(schluessel)) {
                wert = item.getReadableValueForItem();
                break;
            }
        }

        return wert;
    }
}
