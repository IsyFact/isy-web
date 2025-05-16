package de.bund.bva.isyfact.common.web.tempwebresource.persistence;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;

/**
 * Implementierung der Entität TempWebResource. Eine TempWebResource ist eine Ressource, die temporär in der
 * Web-GUI benötigt wird. Es kann sich z.B. um ein Bild handeln oder um eine Datei, die dem Benutzer zum
 * Download bereit gestellt wird.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Entity
@Table(name = "TEMP_WEB_RESOURCE")
public class TempWebResource implements TempWebResourceRo {

    /** Künstlicher Primärschlüssel. */
    private Long id;

    /** Die Benutzerkennung des Benutzers, der auf die Ressource zugreifen darf. */
    private String benutzerkennung;

    /** Das Behördenkennzeichen des Benutzers. */
    private String bhknz;

    /** Ein anwendungsspezifisches Kennzeichen, das z.B. den Typ der Ressource angibt. Optional. */
    private String kennzeichen;

    /** Der MIME-Type der Ressource. Optional. */
    private String mimeType;

    /** Der Dateiname der Ressource. Optional. */
    private String dateiname;

    /** Der Inhalt der Ressource. */
    private byte[] inhalt;

    /** Der Zeitpunkt der Speicherung. */
    private Date zeitpunkt;

    /**
     * Setzt das Feld 'benutzerkennung'.
     * @param benutzerkennung
     *            Neuer Wert für benutzerkennung
     */
    public void setBenutzerkennung(String benutzerkennung) {
        this.benutzerkennung = benutzerkennung;
    }

    /**
     * {@inheritDoc}
     */
    public String getBenutzerkennung() {
        return this.benutzerkennung;
    }

    /**
     * Setzt das Feld 'bhknz'.
     * @param bhknz
     *            Neuer Wert für bhknz
     */
    public void setBhknz(String bhknz) {
        this.bhknz = bhknz;
    }

    /**
     * {@inheritDoc}
     */
    public String getBhknz() {
        return this.bhknz;
    }

    /**
     * Setzt das Feld 'kennzeichen'.
     * @param kennzeichen
     *            Neuer Wert für kennzeichen
     */
    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    /**
     * {@inheritDoc}
     */
    public String getKennzeichen() {
        return this.kennzeichen;
    }

    /**
     * Setzt das Feld 'mimeType'.
     * @param mimeType
     *            Neuer Wert für mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * {@inheritDoc}
     */
    public String getDateiname() {
        return this.dateiname;
    }

    /**
     * Setzt das Feld 'dateiname'.
     * @param dateiname
     *            Neuer Wert für dateiname
     */
    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    /**
     * Setzt das Feld 'inhalt'.
     * @param inhalt
     *            Neuer Wert für inhalt
     */
    public void setInhalt(byte[] inhalt) {
        this.inhalt = inhalt;
    }

    /**
     * {@inheritDoc}
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    public byte[] getInhalt() {
        return this.inhalt;
    }

    /**
     * Setzt das Feld 'zeitpunkt'.
     * @param zeitpunkt
     *            Neuer Wert für zeitpunkt
     */
    public void setZeitpunkt(Date zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    /**
     * {@inheritDoc}
     */
    public Date getZeitpunkt() {
        return this.zeitpunkt;
    }

    /**
     * Setzt das Feld 'id'.
     * @param id
     *            Neuer Wert für id
     */
    void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_TEMP_WEB_RESOURCE_ID")
    @Column(name = "TEMP_WEB_RESOURCE_ID")
    public Long getId() {
        return this.id;
    }
}
