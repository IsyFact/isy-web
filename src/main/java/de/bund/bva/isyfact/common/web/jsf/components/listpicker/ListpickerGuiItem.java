package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.util.List;

/**
 * Eine Klasse, die eine Attribute-Liste zu einem Item speichert und diesem Item eine ID zuweist. Wird diese
 * Klasse in Zusammenhang mit einem Servlet benutzt, sollte darauf geachtet werden, dass die Attribute (attrs)
 * in der Reihenfolge in der Liste erscheinen, wie man sie im Header des Pickers definiert hat.
 */
public class ListpickerGuiItem {
    List<String> attrs;

    String id;

    ListpickerGuiItem() {

    }

    public ListpickerGuiItem(List<String> attrs, String id) {
        this.attrs = attrs;
        this.id = id;
    }

    public List<String> getAttrs() {
        return this.attrs;
    }

    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
