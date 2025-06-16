package de.bund.bva.isyfact.common.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.Listpickerangabe;

/**
 * JSF Converter für eine Listpickerangabe.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@FacesConverter(forClass = Listpickerangabe.class)
public class ListpickerangabeJsfConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value != null && value.contains("-")) {
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
