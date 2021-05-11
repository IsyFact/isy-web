package de.bund.bva.isyfact.common.web.jsf.components.upload;

import java.io.IOException;

import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

public class UploadHelper {

    private static final String QUOTATION_MARK_REPLACEMENT = "";

    private static final String QUOTATION_MARK_TO_REPLACE = "\"";

    private static final char HEADERPART_VALUE_SEPARATOR = '=';

    private static final String HEADERPART_FILENAME = "filename";

    private static final String HEADERPART_SEPARATOR = ";";

    private UploadHelper() {

        // prevent instantiation
    }

    /**
     * Gibt den ursprünglichen Dateinamen zurück.
     *
     * @param part
     *            die hochgeladene Datei
     * @return den ursprünglichen Dateinamen
     */
    public static String getFileName(Part part) {

        if (part == null) {
            return null;
        }
        String header = part.getSubmittedFileName();
        if (header == null) {
            return null;
        }
        for (String headerPart : header.split(HEADERPART_SEPARATOR)) {
            if (headerPart.trim().startsWith(HEADERPART_FILENAME)) {
                return headerPart.substring(headerPart.indexOf(HEADERPART_VALUE_SEPARATOR) + 1).trim()
                    .replace(QUOTATION_MARK_TO_REPLACE, QUOTATION_MARK_REPLACEMENT);
            }
        }
        return null;
    }

    /**
     * Liest den Inhalt der hochgeladenen Datei.
     *
     * @param part
     *            die hochgeladene Datei
     * @return den Inhalt der hochgeladenen Datei
     * @throws IOException
     *             bei I/O-Fehlern
     */
    public static byte[] readData(Part part) throws IOException {

        return IOUtils.toByteArray(part.getInputStream());
    }

    /**
     * Liefert einen String der als Filter für die formUpload-Komponente verwendet werden kann und alle
     * bekannten Dateitypen erlaubt.
     *
     * @return Filter für Dateitypen
     */
    public static String getDateitypFilterAlle() {
        StringBuilder sb = new StringBuilder();
        for (DateitypEnum dateityp : DateitypEnum.values()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(dateityp.getMimeType());
        }
        return sb.toString();
    }
}