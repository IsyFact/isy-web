package de.bund.bva.isyfact.common.web.webflow.titles;

import java.io.Serializable;

/**
 * Enthält die Informationen für einen Eintrag im Breadcrumb.
 * 
 */
@Deprecated
public class BreadcrumbEintrag implements Serializable {

    /** UUID. */
    private static final long serialVersionUID = 0L;

    /** der Titel enthält den Schlüssel des Titels, wie er in breadcrumb_de.properties angegeben ist. */
    private final String titel;

    /**
     * Die Gruppe des Breadcrumb-Eintrags. Die Liste der Breadcrumb-Einträge kann nur einen Eintrag von jeder
     * Gruppe enthalten.
     */
    private final String gruppe;

    /**
     * der Link enthält einen Link auf den Zustand, der auf diesen Eintrag geleitet hat, oder
     * <code>null</code> sollte es sich um den letzten Eintrag handeln.
     */
    private String link;

    /**
     * Erzeugt ein {@link BreadcrumbEintrag}, dessen {@link #titel} gesetzt ist.
     * @param titel
     *            der Wert, auf den {@link #titel} gesetzt wird.
     * @param gruppe
     *            die Gruppe; oder <code>null</code> wenn nicht vorhanden
     */
    public BreadcrumbEintrag(String titel, String gruppe) {
        this.titel = titel;
        this.gruppe = gruppe;
    }

    /**
     * Erzeugt ein {@link BreadcrumbEintrag} ohne Gruppe, dessen {@link #titel} gesetzt ist.
     * @param titel
     *            der Wert, auf den {@link #titel} gesetzt wird.
     */
    public BreadcrumbEintrag(String titel) {
        this(titel, null);
    }

    /**
     * Liefert das Feld 'link' zurück.
     * @return Wert von link
     */
    public String getLink() {
        return link;
    }

    /**
     * Liefert das Feld 'titel' zurück.
     * @return Wert von titel
     */
    public String getTitel() {
        return titel;
    }

    /**
     * Setzt das Feld 'link'.
     * @param link
     *            Neuer Wert für link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Liefert das Feld 'gruppe' zurück.
     * @return Wert von gruppe
     */
    public String getGruppe() {
        return gruppe;
    }

    @Override
    public String toString() {
        return titel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BreadcrumbEintrag) || titel == null || obj == null) {
            return false;
        }
        BreadcrumbEintrag eintrag = (BreadcrumbEintrag) obj;
        return titel.equals(eintrag.getTitel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (titel != null) {
            return titel.hashCode();
        }
        return super.hashCode();
    }

}
