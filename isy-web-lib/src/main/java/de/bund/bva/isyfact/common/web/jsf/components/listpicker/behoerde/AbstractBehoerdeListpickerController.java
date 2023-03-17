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
package de.bund.bva.isyfact.common.web.jsf.components.listpicker.behoerde;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerController;
import de.bund.bva.isyfact.util.spring.MessageSourceHolder;

/**
 * Der konkrete Listpicker-Controller für Behörden.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class AbstractBehoerdeListpickerController
    extends ListpickerController<BehoerdeListpickerModel> {

    /**
     * Konstante für Max Elemente. Wird aus der Konfiguration gelesen.
     */
    private int maxElemente;

    /**
     * Erzeugt die Liste an möglichen Behörden für den Listpicker.
     *
     * @return Die Liste.
     */
    public abstract List<BehoerdeListpickerItem> erzeugeBehoerdeListpickerItemListe();

    public AbstractBehoerdeListpickerController(int maxElemente) {
        this.maxElemente = maxElemente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(BehoerdeListpickerModel model) {

        // Filtere Items
        List<BehoerdeListpickerItem> behoerdeListpickerItems = erzeugeBehoerdeListpickerItemListe();

        List<BehoerdeListpickerItem> gefilterteBehoerdeListpickerItems = new ArrayList<>();

        for (int i = 0; i < behoerdeListpickerItems.size(); i++) {

            BehoerdeListpickerItem filterbaresItem = behoerdeListpickerItems.get(i);

            if (Strings.isNullOrEmpty(model.getFilter())) {
                gefilterteBehoerdeListpickerItems.add(filterbaresItem);
            } else if (filterbaresItem.getName().toLowerCase().contains(model.getFilter().toLowerCase())
                || filterbaresItem.getKennzeichen().startsWith(model.getFilter())) {
                gefilterteBehoerdeListpickerItems.add(filterbaresItem);
            }
        }

        model.setItems(gefilterteBehoerdeListpickerItems);
        model.setItemCount(gefilterteBehoerdeListpickerItems.size());

        begrenzeItems(model);

    }

    /**
     * Erzeugt ein Listpicker Model für alle Behörden.
     *
     * @return Das Listpicker Model.
     */
    public BehoerdeListpickerModel erzeugeListpickerModelFuerBehoerden() {

        // Erstelle Liste
        BehoerdeListpickerModel behoerdeListpickerModel = new BehoerdeListpickerModel();
        behoerdeListpickerModel.getItems().addAll(erzeugeBehoerdeListpickerItemListe());
        behoerdeListpickerModel.setItemCount(behoerdeListpickerModel.getItems().size());
        behoerdeListpickerModel.setMessageMoreElements(
            MessageSourceHolder.getMessage("MEL_Behoerdelistpicker_weitere_Elemente"));

        // Begrenze
        begrenzeItems(behoerdeListpickerModel);

        return behoerdeListpickerModel;

    }

    /**
     * Begrenzt die Items für den AJAX Listpicker.
     *
     * @param behoerdeListpickerModel
     *            Das Listpicker Model.
     */
    private void begrenzeItems(BehoerdeListpickerModel behoerdeListpickerModel) {
        // Schneide Liste bei Bedarf ab
        if (behoerdeListpickerModel.getItems().size() > this.maxElemente) {
            // Begrenze
            behoerdeListpickerModel.setItems(Lists
                .newArrayList(behoerdeListpickerModel.getItems().subList(0, this.maxElemente).iterator()));
        }
    }

    public void setMaxElemente(int maxElemente) {
        this.maxElemente = maxElemente;
    }

}
