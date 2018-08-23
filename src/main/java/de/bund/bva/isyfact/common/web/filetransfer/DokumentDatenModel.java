package de.bund.bva.isyfact.common.web.filetransfer;

import java.io.Serializable;

/**
 * @author Christopher Thomann, msg
 *
 */
public class DokumentDatenModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Inhalt des Dokuments.
     */
    private byte[] inhalt;

    /**
     * Dateityp.
     */
    private DateitypEnum dateityp;

    /**
     * Dateiname ohne Endung.
     */
    private String dateiname;

    @SuppressWarnings("javadoc")
    public byte[] getInhalt() {

        return inhalt;
    }

    @SuppressWarnings("javadoc")
    public void setInhalt(byte[] inhalt) {

        this.inhalt = inhalt;
    }

    @SuppressWarnings("javadoc")
    public DateitypEnum getDateityp() {

        return dateityp;
    }

    @SuppressWarnings("javadoc")
    public void setDateityp(DateitypEnum dateityp) {

        this.dateityp = dateityp;
    }

    @SuppressWarnings("javadoc")
    public String getDateiname() {

        return dateiname;
    }

    @SuppressWarnings("javadoc")
    public void setDateiname(String dateiname) {

        this.dateiname = dateiname;
    }

    /**
     * Liefert zu einem {@link DokumentDatenModel} ein zugehöriges {@link FileModel}, dessen Eigenschaften
     * sich direkt aus dem {@link DokumentDatenModel} ergeben.
     *
     * @return das {@link FileModel}
     */
    public FileModel getFileModel() {

        final FileModel fileModel = new FileModel();

        fileModel.setDateiname(this.getDateiname());
        fileModel.setInhalt(this.getInhalt());
        fileModel.setMimeType(this.getDateityp().getMimeType());

        return fileModel;
    }

    /**
     * Übernimmt in ein bestehendes {@link DokumentDatenModel} die Eigenschaften eines {@link FileModel}s;
     * falls der Dateityp des {@link FileModel}s im {@link DateitypEnum} nicht bekannt ist, setzt die Methode
     * diesen im {@link DokumentDatenModel} auf <code>null</code>.
     *
     * @param fileModel das {@link FileModel}, aus dem die Eigenschaften für das zu befüllende
     *        {@link DokumentDatenModel} entnommen werden sollen
     */
    public void setFromFileModel(FileModel fileModel) {

        this.setDateiname(fileModel.getDateiname());
        this.setInhalt(fileModel.getInhalt());

        // wenn der Dateityp aus dem FileModel nicht im DateitypEnum bekannt ist, wird er null gesetzt
        this.setDateityp(DateitypEnum.getDateitypFuerMimeType(fileModel.getMimeType()).orElse(null));
    }

}
