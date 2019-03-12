package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.util.List;

import de.bund.bva.isyfact.common.web.servlet.requesthandler.AbstractListpickerProviderRequestHandler;

/**
 * Die Klasse enthält eine Liste von {@link ListpickerGuiItem}s und enthält zusätzlich Attribute, die benutzt
 * werden, falls der Listpicker begrenzbar sein soll, was die maximale Anzahl angezeigter Elemente angeht. Die
 * Klasse wird von {@link AbstractListpickerProviderRequestHandler} verwendet und in JSON umgewandelt, so dass
 * die Informationen in JavaScript verfügbar sind.
 */

public class ListpickerGuiKonfiguration {

    /**
     * Die Nachricht die angezeigt wird, falls das Limit der anzuzeigenden Elemente erreicht ist. Siehe
     * {@link #isWeiterFiltern()}.
     */
    private String messageItem;

    /**
     * Legt fest, ob messageItem angezeigt werden soll oder nicht. Das Flag muss auf {@code true} gesetzt
     * werden, falls anhand der Filterkriterien eigentlich noch mehr {@link ListpickerGuiItem}s sichtbar
     * wären, die Anzahl der sichtbaren Items aber begrenzt wurde.
     */
    private boolean weiterFiltern;

    /**
     * Die {@link ListpickerGuiItem}s.
     */
    private List<ListpickerGuiItem> items;

    /**
     * Konstruktor für Listpicker, die keine Begrenzung bzgl. der Anzahl der angezeigten Elemente haben
     * sollen. weiterFiltern wird mit {@code false} initialisiert und messageItem mit dem leeren String.
     *
     * @param items
     *            Liste von GuiItems die angezeigt werden sollen.
     */
    ListpickerGuiKonfiguration(List<ListpickerGuiItem> items) {
        this.items = items;
        this.messageItem = "";
        // -1 bedeutet das es keine Obergrenze für die angezeigten Items gibt
        this.weiterFiltern = false;
    }

    /**
     * Konstruktor für Listpicker, die eine Begrenzung bzgl. der Anzahl der angezeigten Elemente haben sollen.
     * @param items
     *            Liste von GuiItems die angezeigt werden sollen.
     * @param messageItem
     *            Der Text der angezeigt werden soll, falls weiterFiltern {@code true} ist.
     * @param weiterFiltern
     *            Legt fest, ob messageItem angezeigt werden soll oder nicht.
     */
    public ListpickerGuiKonfiguration(List<ListpickerGuiItem> items, String messageItem,
        boolean weiterFiltern) {
        this.items = items;
        this.messageItem = messageItem;
        this.weiterFiltern = weiterFiltern;
    }

    public boolean isWeiterFiltern() {
        return this.weiterFiltern;
    }

    public void setWeiterFiltern(boolean weiterFiltern) {
        this.weiterFiltern = weiterFiltern;
    }

    public String getMessageItem() {
        return this.messageItem;
    }

    public void setMessageItem(String messageItem) {
        this.messageItem = messageItem;
    }

    public List<ListpickerGuiItem> getItems() {
        return this.items;
    }

    public void setItems(List<ListpickerGuiItem> items) {
        this.items = items;
    }
}
