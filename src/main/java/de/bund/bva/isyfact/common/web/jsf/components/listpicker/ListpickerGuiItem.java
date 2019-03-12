package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.io.Serializable;
import java.util.List;

import de.bund.bva.isyfact.common.web.servlet.requesthandler.AbstractListpickerProviderRequestHandler;

/**
 * Die Klasse wird von {@link AbstractListpickerProviderRequestHandler} bzw. dessen Implementierungen
 * verwendet, um die einzelnen Zeilen eines Listpicker zu repräsentieren. Die Items selbst werden in JSON
 * umgewandelt und dann mittels JavaScript verarbeitet. Eine direkt Bindung an die View (im xhtml) findet
 * nicht statt, daher muss {@link Serializable} nicht implementiert werden! Eine Klasse, die eine
 * Attribute-Liste zu einem Item speichert und diesem Item eine ID zuweist. Es muss darauf geachtet werden,
 * dass die Attribute (attrs) in der Reihenfolge in der Liste erscheinen, wie man sie im Header des
 * Listpickers definiert hat.
 */
public class ListpickerGuiItem {

    /**
     * Die Attribute die im Listpicker gerendert werden sollen, also typischerweise Schlüssel und Wert, aber
     * letztlich ist der Aufbau beliebig. Wenn der Listpicker beispielsweise "Schlüssel, Wert1, Wert2"
     * anzeigen soll, dann sind die entsprechenden Werte hier in der korrekten Reihenfolge zu hinterlegen. Es
     * ist bewusst eine sehr primitive Datenstruktur gewählt werden, damit die Verarbeitung in JavaScript
     * möglichst einfach ist.
     */
    private List<String> attrs;

    /**
     * Die ID des Items. Oft entspricht diese einfach dem Schlüssel des Eintrags.
     */
    private String id;

    ListpickerGuiItem() {
        // Leerer Konstruktor.
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
