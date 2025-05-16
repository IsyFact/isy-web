package de.bund.bva.isyfact.common.web.tempwebresource.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;

/**
 * Controller-Klasse zur Darstellung von Bildern.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class BildController extends ResourceController implements GuiController {
    /**
     * Diese Methode stellt Bilder zur direkten Anzeige in der GUI bereit.
     *
     * @param request
     *            der aktuelle http request.
     * @param response
     *            die aktuelle http response.
     * @throws IOException
     *             Fehler beim Schreiben der Datei.
     * @return Rückgabe ist immer null.
     *
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        TempWebResourceRo webResource = ladeTempWebResource(request);
        if (webResource != null) {
            byte[] content = webResource.getInhalt();
            response.setBufferSize(content.length);
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        return null;
    }
}
