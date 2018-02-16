package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;
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
     * Die Bezeichnung des Icons der Applikationsgruppe, welches im Flyout angezeigt wird. Wenn kein Icon
     * konfiguriert ist, wird nichts angezeigt, auch kein Platzhalter. Das Icon muss namentlich in der
     * Icon-Library vorhanden sein, damit es aufgelöst werden kann.
     */
    private String icon;

    /**
     * Liste von {@link Anwendung}en, welche zu dieser Applikationsgruppe gehören. Diese kann auch leer sein.
     */
    private List<Anwendung> anwendungen;

    /**
     * Eine Beschreibung der Applikationsgruppe.
     */
    private String beschreibung;

    /**
     * Eine mit Komma getrennte Liste von Rollen, welche für diese Applikationsgruppe berechtigt sind. Ist die
     * Liste leer so ist jeder berechtigt.
     */
    private String rolle;

    /**
     * Der Konstruktor initialisiert direkt alle Variablen mit den übergebenen Werten.
     *
     * @param name
     *            Name der Applikationsgruppe.
     * @param beschreibung
     *            Beschreibung der Applikationsgruppe.
     * @param link
     *            Der Link, der geöffnet wird, wenn die Applikationsgruppe ausgewählt wird.
     * @param icon
     *            Die Bezeichnung des Icons der Applikationsgruppe.
     * @param farbe
     *            Farbe der Menüleiste.
     * @param rolle
     *            Zulässige Rollen der Applikationsgruppe.
     * @param reihenfolge
     *            Die Reihenfolge der Applikationsgruppe.
     * @param anwendungen
     *            Liste von {@link Anwendung}en welche zu dieser Applikationsgruppe gehören.
     */
    public Applikationsgruppe(String name, String beschreibung, String link, String icon, String farbe,
        String rolle, int reihenfolge, List<Anwendung> anwendungen) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.link = link;
        this.icon = icon;
        this.farbe = farbe;
        this.reihenfolge = reihenfolge;
        this.anwendungen = anwendungen;
        this.rolle = rolle;
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

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public boolean isAktiv() {
        return this.aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public String getRolle() {
        return this.rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    @Override
    public int compareTo(Applikationsgruppe andereApplikationsgruppe) {
        return Integer.compare(this.reihenfolge, andereApplikationsgruppe.getReihenfolge());
    }

}
