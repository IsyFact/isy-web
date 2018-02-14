package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */

/**
 * Eine Applikation mit allen Infos um sie in einem Navigationsmenü darzustellen
 */
public class Applikation implements Serializable {
    /**
     * Die serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name der Applikation (Es kann immer nur eine Applikation aktiv sein)
     */
    private String name;

    /**
     * Flag um anzuzeigen ob die benutzte Anwendung sich in der Applikation befindet
     */
    private boolean aktiv;

    /**
     * Das Linkziel der Applikation
     */
    private String link;

    /**
     * Applikationsfarbe für die Navigationsmenüleiste
     */
    private String farbe;

    /**
     * Ein Integerwert der angibt an welcher Stelle im Menü dieser Eintrag steht (links=klein nach
     * rechts=groß)
     */
    private Integer wert;

    /**
     * Das CSS Tag des Icons der Applikationsgruppe, welches im Flyout angezeigt wird
     */
    private String imgTag;

    /**
     * Liste von Anwendungen, welche zu dieser Applikation gehören
     */
    private List<Anwendung> anwendungen;

    /**
     * Eine Beschreibung der Applikation
     */
    private String beschreibung;

    /**
     * Eine mit Komma getrennte Liste von Rollen, welche für diese Applikation berechtigt sind. Ist die Liste
     * leer so ist jeder berechtigt.
     */
    private String rolle;

    /**
     * Konstruktor
     *
     * @param name
     *            Name der Applikation
     * @param beschreibung
     *            Beschreibung der Applikation
     * @param link
     *            Linkziel der Applikation
     * @param imgTag
     *            CSS Tag des Icons der Applikation
     * @param farbe
     *            Farbe der Menüleiste
     * @param rolle
     *            Zulässige Rollen der Applikation
     * @param aktiv
     *            Flag die anzeigt, ob man sich in dieser Applikation befindet
     * @param wert
     *            Ein Wert für die Sortierung der Menüpunkte (links=klein nach rechts=groß)
     * @param anwendungen
     *            Liste von Anwendungen welche zu dieser Applikation gehören
     */
    public Applikation(String name, String beschreibung, String link, String imgTag, String farbe,
        String rolle, boolean aktiv, Integer wert, List<Anwendung> anwendungen) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.aktiv = aktiv;
        this.link = link;
        this.imgTag = imgTag;
        this.farbe = farbe;
        this.wert = wert;
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

    public Integer getWert() {
        return this.wert;
    }

    public void setWert(Integer wert) {
        this.wert = wert;
    }

    public String getImgTag() {
        return this.imgTag;
    }

    public void setImgTag(String imgPath) {
        this.imgTag = imgPath;
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

}
