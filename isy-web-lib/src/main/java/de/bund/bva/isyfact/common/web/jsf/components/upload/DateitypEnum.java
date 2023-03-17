package de.bund.bva.isyfact.common.web.jsf.components.upload;

/**
 * Enum mit Mime-Types je Dateityp.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public enum DateitypEnum {

    /**
     * Bitmap files
     */
    BMP("image/bmp", ".bmp"),
    /**
     * Comma-separated files
     */
    CSV("text/csv", ".csv"),
    /**
     * JPEG image files
     */
    JPEG("image/jpeg", ".jpeg"),
    /**
     * JPG
     */
    JPG("image/jpg", ".jpg"),
    /**
     * Portable Network Graphic (PNG) files
     */
    PNG("image/png", ".png"),
    /**
     * MP3-Audio-files
     */
    MP3("audio/x-mpeg", ".mp3"),
    /**
     * MP4-Video-files
     */
    MP4("video/mp4", ".mp4"),
    /**
     * PDF-Documents
     */
    PDF("application/pdf", ".pdf"),
    /**
     * Plain-Text-Documents
     */
    TXT("text/plain", ".txt"),
    /**
     * Microsoft Word-Documents (binary, no xml)
     */
    DOC("application/msword", ".doc"),
    /**
     * Microsoft Word-Documents (xml)
     */
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    /**
     * Microsoft Excel-Documents (binary, no xml)
     */
    XLS("application/msexcel", ".xls"),
    /**
     * Microsoft Excel-Documents (xml)
     */
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    /**
     * Microsoft Powerpoint-Documents (binary, no xml)
     */
    PPT("application/mspowerpoint", ".ppt"),
    /**
     * Microsoft Powerpoint-Documents (xml)
     */
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"),
    /**
     * Zip files
     */
    ZIP("application/zip", ".zip");

    private String label;

    private String mimetype;

    private String dateiErweiterung;

    DateitypEnum(String mimetype, String dateiErweiterung) {

        this.label = name().toLowerCase();
        this.mimetype = mimetype;
        this.dateiErweiterung = dateiErweiterung;
    }

    /**
     * Gibt das Label des Dateityps zurück.
     *
     * @return Label des Dateityp
     */
    public String getLabel() {

        return this.label;
    }

    /**
     * Gibt den Mimetype der Datei zurück.
     *
     * @return Mimetype der Datei in Form von bspw. (für PDF): 'application/pdf'
     */
    public String getMimeType() {
        return this.mimetype;
    }

    /**
     * Gibt die Dateierweiterung der Datei zurück.
     *
     * @return Dateierweiterung in Form von bspw. (für PDF): '.pdf'
     */
    public String getDateiErweiterung() {

        return this.dateiErweiterung;
    }

    /**
     * Gibt den Dateityp für den übergebenen Mimetype zurück.
     *
     * @param mimetype
     *            Der Mimetype der Datei
     * @return der Dateityp, kann <code>null</code> sein.
     */
    public static DateitypEnum getDateitypFuerMimeType(String mimetype) {
        for (DateitypEnum typ : DateitypEnum.values()) {
            if (typ.getMimeType().equals(mimetype)) {
                return typ;
            }
        }
        return null;
    }

    @Override
    public String toString() {

        return this.label;
    }

    /**
     * Liefert für das übergebene Label den zugehörigen Dateityp.
     *
     * @param label
     *            das Label des Dateityps
     * @return den Dateityp, kann <code>null</code> sein.
     */
    public static DateitypEnum forLabel(String label) {
        for (DateitypEnum typ : DateitypEnum.values()) {
            if (typ.getLabel().equalsIgnoreCase(label)) {
                return typ;
            }
        }
        return null;
    }
}
