package de.bund.bva.isyfact.common.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * JSF Converter der keine Konvertierung durchführt und immer null zurückgibt. Dadurch können
 * Fehlkonfigurationen von Widgets abgefangen werden.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@FacesConverter(value = "nullJsfConverter")
public class EmptyJsfConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        return null;
    }
}
