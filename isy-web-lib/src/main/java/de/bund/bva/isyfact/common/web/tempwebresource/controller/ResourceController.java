package de.bund.bva.isyfact.common.web.tempwebresource.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
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
            String benutzerkennung = getTokenValueOfCurrentUser("login");
            String bhknz = getTokenValueOfCurrentUser("bhknz");

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

    /**
     * Returns the value of a security attribute of the current user's token
     * or an empty Optional if the attribute is not set.
     * @param attribute
     *         the name of the attribute
     * @return the value of the attribute
     */
    public static String getTokenValueOfCurrentUser(String attribute) {
        String key = ResourceBundle
            .getBundle("config.isy-security-token")
            .getString("isy.security.oauth2.claim." + attribute);
        String login = (String) getTokenAttribute(key);
        return Optional.ofNullable(login).orElse("");
    }

    /**
     * Retrieves an attribute of the access token if the currently authenticated principal is an OAuth 2.0 token.
     *
     * @param key
     *         the key to retrieve the given attribute
     * @return the attribute in the access token for the given key, or {@code null}
     * @throws OAuth2AuthenticationException
     *         if the authenticated principal is not a {@link AbstractOAuth2TokenAuthenticationToken}
     */
    public static Object getTokenAttribute(String key) {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication instanceof AbstractOAuth2TokenAuthenticationToken) {
            return ((AbstractOAuth2TokenAuthenticationToken<?>) currentAuthentication).getTokenAttributes().get(key);
        } else {
            throw new OAuth2AuthenticationException(BearerTokenErrors.invalidToken("Authentication is not an OAuth2 token authentication."));
        }
    }

}
