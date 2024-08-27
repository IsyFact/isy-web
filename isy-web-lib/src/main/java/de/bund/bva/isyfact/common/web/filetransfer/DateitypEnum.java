package de.bund.bva.isyfact.common.web.filetransfer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum mit den verwendeten Dateitypen. Enthält außerdem auch die jeweiligen Mime-Types je Dateityp.
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
     * Microsoft Word-Documents mit Makros (xml)
     */
    DOCM("application/vnd.ms-word.document.macroEnabled.12", ".docm"),
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

    /**
     * Maximale Länger der Schlüssel, die dieser Enum verwendet
     */
    public static final int MAXLENGTH = 4;

    /**
     * GUI-Darstellung
     */
    private String label;

    /**
     * Mime-Type
     */
    private String mimeType;

    /**
     * Dateierweiterung inkl. "."
     */
    private String dateiErweiterung;


    private DateitypEnum(String mimeType, String dateiErweiterung) {
        this.label = name().toLowerCase();
        this.mimeType = mimeType;
        this.dateiErweiterung = dateiErweiterung;
    }

    @SuppressWarnings("javadoc")
    public String getLabel() {

        return this.label;
    }

    @SuppressWarnings("javadoc")
    public String getMimeType() {

        return this.mimeType;
    }

    @SuppressWarnings("javadoc")
    public String getDateiErweiterung() {

        return this.dateiErweiterung;
    }

    /**
     * Gibt den Dateityp für den übergebenen Mimetype zurück.
     *
     * @param mimetype Der Mimetype der Datei
     * @return der Dateityp
     */
    public static Optional<DateitypEnum> getDateitypFuerMimeType(String mimetype) {
        return Stream.of(DateitypEnum.values()).filter(typ -> typ.getMimeType().equals(mimetype)).findFirst();
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * Liefert für das übergebene label die Enum-Ausprägung
     *
     * @param label Label
     * @return zugehöriger Enumwert
     */
    public static Optional<DateitypEnum> forLabel(String label) {
        return Stream.of(values()).filter(typ -> typ.label.equalsIgnoreCase(label)).findFirst();
    }

    /**
     * Lieft einen durch Komma getrennten String, der alle erlaubten Dateitypen enthält.
     * Dieser String kann z.B. im accept-Attribut verwendet werden.
     *
     * @param dateitypen erlaubte Dateitypen
     * @return durch Komma getrennter String mit Dateiendungen
     */
    public static String getErlaubteDateiendungen(List<DateitypEnum> dateitypen) {

        return dateitypen.stream().map(DateitypEnum::getDateiErweiterung).collect(Collectors.joining(","));
    }
}
