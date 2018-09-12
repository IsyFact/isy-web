package de.bund.bva.isyfact.common.web.filetransfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

/**
 * Hilfklasse zur Handhabung von ZIP-Dateien.
 *
 * @author Michael Moossen, msg
 */
public class ZipHelper {

    /**
     * Generiert eine ZIP-Datei mit den angegebenen Inhalt.
     *
     * @param dateien die zukomprimierenden Dateien
     * @return die generierte komprimierte ZIP-Datei
     * @throws IOException wenn was schief geht. Leider sind diese Ereignisse in der darunter-liegenden
     *         ZIP-Api nicht besonders gut dokumentiert
     */
    public byte[] createZip(FileModel... dateien) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipfile = new ZipOutputStream(bos);
        for (FileModel datei : dateien) {
            final ZipEntry zipEntry = new ZipEntry(datei.getDateiname());
            zipfile.putNextEntry(zipEntry);
            zipfile.write(datei.getInhalt());
        }
        zipfile.close();
        return bos.toByteArray();
    }
}
