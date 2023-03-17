package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;

/**
 * Eine Anwendung zum Einbinden in das Navigationsmenü.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
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
     * Der Konstruktor initialisiert direkt alle Variablen mit den übergebenen Werten.
     *
     * @param name
     *            Name der Anwendung.
     * @param link
     *            Linkziel der Anwendung.
     * @param reihenfolge
     *            Die Reihenfolge der Anwendung.
     */
    public Anwendung(String name, String link, int reihenfolge) {
        this.name = name;
        this.reihenfolge = reihenfolge;
        this.link = link;
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

    @Override
    public int compareTo(Anwendung andereAnwendung) {
        return Integer.compare(this.reihenfolge, andereAnwendung.getReihenfolge());
    }

}
