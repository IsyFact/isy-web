package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Eine Applikationsgruppe mit allen Infos um sie in einem Navigationsmenü darzustellen.
 */
public class Applikationsgruppe implements Serializable, Comparable<Applikationsgruppe> {
    /**
     * Die serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name der Applikationsgruppe.
     */
    private String name;

    /**
     * Flag um anzuzeigen, ob die Applikationsgruppe aktiv ist. D.h. der Benutzer befindet sich gerade
     * innerhalb dieser Applikationsgruppe bzw. in einer Anwendung der Applikationsgruppe. (Es kann immer nur
     * eine Applikationsgruppe aktiv sein).
     */
    private boolean aktiv;

    /**
     * Der Link, der geöffnet wird, wenn die Applikationsgruppe ausgewählt wird.
     */
    private String link;

    /**
     * Farbe der Applikationsgruppe für die Navigationsmenüleiste.
     */
    private String farbe;

    /**
     * Gibt die Reihenfolge der Applikationsgruppe an (links=klein nach rechts=groß).
     */
    private int reihenfolge;

    /**
     * Liste von {@link Anwendung}en, welche zu dieser Applikationsgruppe gehören. Diese kann auch leer sein.
     */
    private List<Anwendung> anwendungen = new ArrayList<>();

    /**
     * Der Konstruktor initialisiert direkt alle Variablen mit den übergebenen Werten.
     *
     * @param name
     *            Name der Applikationsgruppe.
     * @param link
     *            Der Link, der geöffnet wird, wenn die Applikationsgruppe ausgewählt wird.
     * @param farbe
     *            Farbe der Menüleiste.
     * @param reihenfolge
     *            Die Reihenfolge der Applikationsgruppe.
     * @param anwendungen
     *            Liste von {@link Anwendung}en welche zu dieser Applikationsgruppe gehören.
     */
    public Applikationsgruppe(String name, String link, String farbe, int reihenfolge,
        List<Anwendung> anwendungen) {
        this.name = name;
        this.link = link;
        this.farbe = farbe;
        this.reihenfolge = reihenfolge;
        this.anwendungen = anwendungen;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFarbe() {
        return this.farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public int getReihenfolge() {
        return this.reihenfolge;
    }

    public void setReihenfolge(int reihenfolge) {
        this.reihenfolge = reihenfolge;
    }

    public List<Anwendung> getAnwendungen() {
        return this.anwendungen;
    }

    public void setAnwendungen(List<Anwendung> anwendungen) {
        this.anwendungen = anwendungen;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAktiv() {
        return this.aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    @Override
    public int compareTo(Applikationsgruppe andereApplikationsgruppe) {
        return Integer.compare(this.reihenfolge, andereApplikationsgruppe.getReihenfolge());
    }

}
