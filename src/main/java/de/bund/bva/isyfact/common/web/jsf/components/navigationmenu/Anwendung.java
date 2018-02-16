package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;

/**
 * Eine Anwendung zum Einbinden in das Navigationsmenü.
 */
public class Anwendung implements Serializable, Comparable<Anwendung> {

    /**
     * Die SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name der Anwendung.
     */
    private String name;

    /**
     * Gibt die Reihenfolge der Anwendung an (links-oben=klein nach rechts-unten=groß).
     */
    private int reihenfolge;

    /**
     * Der Link, der geöffnet wird, wenn die Anwendung ausgewählt wird.
     */
    private String link;

    /**
     * Eine mit Komma getrennte Liste von Rollen, welche für diese Anwendung berechtigt sind. Ist die Liste
     * leer so ist jeder berechtigt.
     */
    private String rolle;

    /**
     * Der Konstruktor initialisiert direkt alle Variablen mit den übergebenen Werten.
     *
     * @param name
     *            Name der Anwendung.
     * @param link
     *            Linkziel der Anwendung.
     * @param rolle
     *            Zulässige Rollen für diese Anwendung.
     * @param reihenfolge
     *            Die Reihenfolge der Anwendung.
     */
    public Anwendung(String name, String link, String rolle, int reihenfolge) {
        this.name = name;
        this.reihenfolge = reihenfolge;
        this.link = link;
        this.rolle = rolle;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReihenfolge() {
        return this.reihenfolge;
    }

    public void setReihenfolge(int reihenfolge) {
        this.reihenfolge = reihenfolge;
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

    @Override
    public int compareTo(Anwendung andereAnwendung) {
        return Integer.compare(this.reihenfolge, andereAnwendung.getReihenfolge());
    }

}
