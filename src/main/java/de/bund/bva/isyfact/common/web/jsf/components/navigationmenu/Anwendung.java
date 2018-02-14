package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;

/**
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */

/**
 * Eine Anwendung zum einbinden in das Navigationsmenü
 */
public class Anwendung implements Serializable {

    /**
     * Die SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name der Anwendung
     */
    String name;

    /**
     * Ein Integerwert der angibt an welcher Stelle im Menü dieser Eintrag steht (links-oben=klein nach
     * rechts-unten=groß)
     */
    int wert;

    /**
     * Das Linkziel einer Anwendung
     */
    String link;

    /**
     * Eine mit Komma getrennte Liste von Rollen, welche für diese Anwendung berechtigt sind. Ist die Liste
     * leer so ist jeder berechtigt.
     */
    String rolle;

    /**
     * Konstruktor
     *
     *
     * @param name
     *            Name der Anwendung
     * @param link
     *            Linkziel der Anwendung
     * @param rolle
     *            Zulässige Rollen für diese Anwendung
     * @param wert
     *            Wert zur Bestimmung der Auflistung im Menu (links-oben=klein nach rechts-unten=groß)
     */
    public Anwendung(String name, String link, String rolle, int wert) {
        this.name = name;
        this.wert = wert;
        this.link = link;
        this.rolle = rolle;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWert() {
        return this.wert;
    }

    public void setWert(Integer wert) {
        this.wert = wert;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRolle() {
        return this.rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

}
