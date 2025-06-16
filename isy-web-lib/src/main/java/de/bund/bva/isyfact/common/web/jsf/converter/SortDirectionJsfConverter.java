package de.bund.bva.isyfact.common.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import de.bund.bva.isyfact.common.web.jsf.components.datatable.SortDirection;

/**
 * JSF Converter für ein Datum, das im Format "dd.MM.yyyy" dargestellt werden soll.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@FacesConverter(value = "sortDirectionJsfConverter")
public class SortDirectionJsfConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return SortDirection.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return "";
        } else {
            return ((SortDirection) value).toString();
        }

    }
}
