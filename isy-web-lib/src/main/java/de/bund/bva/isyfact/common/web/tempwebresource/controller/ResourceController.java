/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.tempwebresource.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.common.base.Strings;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.exception.ZugriffBerechtigungException;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceZugriff;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.security.core.Berechtigungsmanager;

/**
 * Abstrakte Oberklasse für Controller zum Herunterladen von Bildern und Dateien.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class ResourceController implements Controller, GuiController {

    /** Logger. */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(ResourceController.class);

    /** Mime type für HTML. */
    private static final String MIME_TYPE_HTML = "text/html";

    /** Ein Request-Parameter, der einen Schlüssel für ein Objekt in der Session referenziert. */
    private static final String SESSION_ATTRIBUTE_KEY = "objectId";

    /** TempWebResourceZugriff-Instanz. */
    private TempWebResourceZugriff tempWebResourceZugriff;

    /** Zugriff auf den Berechtigungsmanager. */
    private Berechtigungsmanager berechtigungsmanager;

    /** Fehlertextprovider zum Auslesen von Fehlertexten. */
    private static final FehlertextProvider FEHLERTEXT_PROVIDER = new FehlertextProviderImpl();

    /**
     * Diese Methode stellt die Datei zum Herunterladen bereit.
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
    public abstract ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException;

    /**
     * Erzeugt Seite fÃ¼r den Fehlerfall.
     *
     * @param response
     *            die aktuelle http response.
     * @throws IOException
     *             Fehler beim Schreiben der Datei.
     */
    protected void dateiNichtGefunden(HttpServletResponse response) throws IOException {
        response.setContentType(MIME_TYPE_HTML);
        PrintWriter printwriter = response.getWriter();
        printwriter.println(FEHLERTEXT_PROVIDER.getMessage(FehlerSchluessel.DATEI_NICHT_GEFUNDEN_HTML));
        printwriter.flush();
        printwriter.close();
    }

    /**
     * Liest die bereitzustellende Ressourcen-Datei.
     *
     * @param request
     *            der aktuelle HTTP-Request
     * @return die Ressourcen-Datei
     */
    protected TempWebResourceRo ladeTempWebResource(HttpServletRequest request) {
        Long objectId = parseObjectId(request);
        if (objectId == null) {
            return null;
        }
        LOG.debug("Lade Ressource {}", objectId);

        TempWebResourceRo webResource = null;
        try {
            String benutzerkennung = (String) berechtigungsmanager.getTokenAttribute("preferred_username");
            String bhknz = (String) berechtigungsmanager.getTokenAttribute("bhknz");

            webResource = this.tempWebResourceZugriff.ladeTempWebResource(objectId, benutzerkennung, bhknz);
        } catch (ZugriffBerechtigungException e) {
            LOG.error("Berechtigungsfehler beim Laden von Ressource {}", e, objectId);
            return null;
        }

        if (webResource == null) {
            LOG.warn(EreignisSchluessel.E_RESSOURCE, "Keine Ressource {} gefunden", objectId);
            return null;
        }

        return webResource;
    }

    /**
     * Liest die Object-ID aus dem Request String.
     *
     * @param request
     *            der HTTP-Request
     * @return die Object-ID
     */
    private Long parseObjectId(HttpServletRequest request) {
        String objectIdString = request.getParameter(SESSION_ATTRIBUTE_KEY);
        if (Strings.nullToEmpty(objectIdString).trim().isEmpty()) {
            LOG.warn(EreignisSchluessel.E_OBJEKT_ID, "Keine Objekt-ID im Request-String {} gefunden.",
                request.getRequestURI());
            return null;
        }
        try {
            return Long.parseLong(objectIdString);
        } catch (NumberFormatException e) {
            LOG.warn(EreignisSchluessel.E_OBJEKT_ID, "Ungültige Objekt-ID im Request-String {} gefunden.",
                request.getRequestURI());
            return null;
        }
    }

    /**
     * Setter-Methode.
     *
     * @param tempWebResourceZugriff
     *            TempWebResourceZugriff-Instanz.
     */
    public void setTempWebResourceZugriff(TempWebResourceZugriff tempWebResourceZugriff) {
        this.tempWebResourceZugriff = tempWebResourceZugriff;
    }
}
