package de.bund.bva.isyfact.common.web.tempwebresource.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;

/**
 * Controller-Klasse für den "Speichern unter"-Dialog. Es können Fingerabdrücke und XML-Dateien gespeichert
 * werden.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class FileDownloadController extends ResourceController implements GuiController {

    /** Header zum Starten des "Speichern Unter"-Dialogs. */
    private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    /**
     * Diese Methode öffnet den "Speichern unter"-Dialog und stellt somit dem aktuellen Benutzer die
     * gewünschte Datei bereit.
     *
     * @param request
     *            der aktuelle http request.
     * @param response
     *            die aktuelle http response.
     * @throws IOException
     *             Fehler beim Schreiben der Datei.
     * @return Rückgabe ist immer null.
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        TempWebResourceRo webResource = ladeTempWebResource(request);
        if (webResource != null) {
            String dateiname = webResource.getDateiname();
            if (dateiname == null) {
                dateiname = bestimmeDateiname(request.getRequestURI());
            }
            response.setHeader(HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + dateiname + "\"");

            byte[] content = webResource.getInhalt();

            if (webResource.getMimeType() != null) {
                response.setContentType(webResource.getMimeType());
            }
            response.setBufferSize(content.length);
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } else {
            dateiNichtGefunden(response);
        }
        return null;
    }

    /**
     * Hilfsmethode, die aus einer URI den Dateinamen extrahiert.
     * @param uri
     *            die zu untersuchende URI
     * @return der Dateiname ohne Pfadangabe
     */
    private String bestimmeDateiname(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }
}
