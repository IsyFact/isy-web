package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

/**
 * Der Inhalt eines Sonderzeichen-Mappings.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SpecialCharPickerMapping {

    /** Der beschreibende Text zu einem Sonderzeichen . */
    private String titel;

    /** Das Basiszeichen eines Sonderzeichens. */
    private char basiszeichen;

    /**
     * Konstruktor.
     *
     * @param basiszeichen
     *            das Basiszeichen des Sonderzeichens
     * @param titel
     *            der Titel des Sonderzeichens.
     */
    public SpecialCharPickerMapping(char basiszeichen, String titel) {
        this.basiszeichen = basiszeichen;
        this.titel = titel;
    }

    /**
     * Gibt den Titel (erläuternder Text) des Sonderzeichens. zurück.
     *
     * @return den Titel, oder null.
     */
    public String getTitel() {
        return this.titel;
    }

    /**
     * Gibt das Basiszeichen des Sonderzeichens zurück.
     *
     * @return das Basiszeichen.
     */
    public char getBasiszeichen() {
        return this.basiszeichen;
    }
}
