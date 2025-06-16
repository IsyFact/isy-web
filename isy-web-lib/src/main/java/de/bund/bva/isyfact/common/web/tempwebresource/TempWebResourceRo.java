package de.bund.bva.isyfact.common.web.tempwebresource;

import java.util.Date;

/**
 * Read-Only-Interface auf die Entität TempWebResource. Eine TempWebResource repräsentiert eine Ressource, die
 * temporär für die Web-GUI benötigt wird, z.B. ein Bild oder eine PDF-Datei.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface TempWebResourceRo {

    /**
     * Liefert das Feld 'benutzerkennung' zurück.
     * @return Wert von benutzerkennung
     */
    public String getBenutzerkennung();

    /**
     * Liefert das Feld 'bhknz' zurück.
     * @return Wert von bhknz
     */
    public String getBhknz();

    /**
     * Liefert das Feld 'kennzeichen' zurück. Das Kennzeichen kann benutzt werden, um TempWebResources zu
     * klassifizieren (z.B. Bild oder PDF-Datei).
     * @return Wert von kennzeichen
     */
    public String getKennzeichen();

    /**
     * Liefert das Feld 'mimeType' zurück.
     * @return Wert von mimeType
     */
    public String getMimeType();

    /**
     * Liefert das Feld 'dateiname' zurück.
     * @return Wert von dateiname
     */
    public String getDateiname();

    /**
     * Liefert das Feld 'inhalt' zurück.
     * @return Wert von inhalt
     */
    public byte[] getInhalt();

    /**
     * Liefert das Feld 'zeitpunkt' zurück.
     * @return Wert von zeitpunkt
     */
    public Date getZeitpunkt();

    /**
     * Liefert das Feld 'id' zurück.
     * @return Wert von id
     */
    public Long getId();
}
