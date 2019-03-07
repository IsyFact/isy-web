package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

import java.util.List;

/**
 * Die Klasse enthält eine Liste von {@link ListpickerGuiItem}s und enthält zusätzlich Attribute, die benutzt
 * werden, falls der Listpicker begrenzbar sein soll, was die maximale Anzahl angezeigter Elemente angeht. Die
 * Klasse wird für ein Listpickerservlet benötigt.
 */

public class ListpickerGuiKonfiguration {

    /**
     * Die Nachricht die angezeigt wird, falls das Limit der anzuzeigenden Elemente erreicht ist. Siehe
     * {@link #getMaxElemente()}.
     */
    String messageItem;

    /**
     * Die Anzahl von Elementen, die maximal angezeigt werden sollen. Der Wert -1 bedeutet keine Begrenzung.
     */
    int maxElemente;

    /**
     * Die {@link ListpickerGuiItem}s.
     */
    List<ListpickerGuiItem> items;

    /**
     * Konstruktor für Listpicker, die keine Begrenzung bzgl. der Anzahl der angezeigten Elemente haben
     * sollen. maxElemente wird mit -1 initialisiert und messageItem mit dem leeren String.
     *
     * @param items
     *            Liste von GuiItems die angezeigt werden sollen.
     */
    ListpickerGuiKonfiguration(List<ListpickerGuiItem> items) {
        this.items = items;
        this.messageItem = "";
        // -1 bedeutet das es keine Obergrenze für die angezeigten Items gibt
        this.maxElemente = -1;
    }

    /**
     * Konstruktor für Listpicker, die eine Begrenzung bzgl. der Anzahl der angezeigten Elemente haben sollen.
     * @param items
     *            Liste von GuiItems die angezeigt werden sollen.
     * @param messageItem
     *            Der Text der angezeigt werden soll, falls maxElemente erreicht ist.
     * @param maxElemente
     *            Die maximale Anzahl an Elementen, die in der gefilterten Liste erscheinen sollen. Falls die
     *            Anzahl erreicht wird, wird messageItem am Ende der gefilterten Liste angezeigt.
     */
    public ListpickerGuiKonfiguration(List<ListpickerGuiItem> items, String messageItem, int maxElemente) {
        this.items = items;
        this.messageItem = messageItem;
        this.maxElemente = maxElemente;
    }

    public String getMessageItem() {
        return this.messageItem;
    }

    public void setMessageItem(String messageItem) {
        this.messageItem = messageItem;
    }

    public int getMaxElemente() {
        return this.maxElemente;
    }

    public void setMaxElemente(int maxElemente) {
        this.maxElemente = maxElemente;
    }

    public List<ListpickerGuiItem> getItems() {
        return this.items;
    }

    public void setItems(List<ListpickerGuiItem> items) {
        this.items = items;
    }
}
