package de.bund.bva.isyfact.common.web.filetransfer;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Modell für einzelne Dateien.
 * <p>
 * Besonders wichtig ist dass eine dem Webserver bekannte Dateierweiterung im <code>dateiname</code> angegeben
 * wird, damit der Mime-Type richtig ermittelt und im Header gesetzt werden kann.
 *
 * @author Michael Moossen, msg
 **/
public class FileModel extends AbstractMaskenModel {

    private static final long serialVersionUID = 1L;

    /**
     * Hier ist besonders wichtig dass eine dem Webserver bekannte Dateierweiterung angegeben wird, damit der
     * Mime-Type richtig gesetzt werden kann.
     */
    private String dateiname;
    private byte[] inhalt;

    private String mimeType;

    @SuppressWarnings("javadoc")
    public String getDateiname() {

        return dateiname;
    }

    @SuppressWarnings("javadoc")
    public byte[] getInhalt() {

        return inhalt;
    }

    @SuppressWarnings("javadoc")
    public void setDateiname(String dateiname) {

        this.dateiname = dateiname;
    }

    @SuppressWarnings("javadoc")
    public void setInhalt(byte[] inhalt) {

        this.inhalt = inhalt;
    }

    //@SuppressWarnings("javadoc")
    public String getMimeType() {

        return mimeType;
    }

    //@SuppressWarnings("javadoc")
    public void setMimeType(String mimeType) {

        this.mimeType = mimeType;
    }
}
