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
package de.bund.bva.isyfact.common.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.Listpickerangabe;

/**
 * JSF Converter für eine Listpickerangabe.
 */
@FacesConverter(forClass = Listpickerangabe.class)
public class ListpickerangabeJsfConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (StringUtils.contains(value, "-")) {
            String[] inhalt = value.split(" ");
            if (inhalt[1].equals("-")) {
                // Die Angabe ist gültig. Erstelle ein gültiges Objekt.
                Listpickerangabe angabe = new Listpickerangabe();
                angabe.setText(inhalt[0]);
                return angabe;
            }
        } else {
            // nur ein Schlüssel ohne Wertangabe
            Listpickerangabe angabe = new Listpickerangabe();
            angabe.setText(value);
            return angabe;
        }

        // Die Angabe ist ungültig. Gib null zurück.
        // Die Validierung findet in einer späteren Phase statt.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        return ((Listpickerangabe) value).getText();
    }
}
