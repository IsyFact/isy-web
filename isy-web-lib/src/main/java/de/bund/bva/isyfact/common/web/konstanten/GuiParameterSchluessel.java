package de.bund.bva.isyfact.common.web.konstanten;

/**
 * Enthaelt guibezogene Parameter-Schluessel.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class GuiParameterSchluessel {

    /**
     * Schluessel zur Identifizierung der Javascript-Aktivierung in der HTTP-Session und als
     * HTTP-GET-Parameter.
     */
    public static final String JAVASCRIPT_AKTIVIERT_SCHLUESSEL = "javascriptAktiviert";

    /**
     * Ist die Url der urspruenglichen Anfrage, sofern noch keine Application-Initialisierung stattgefunden
     * hatte.
     */
    public static final String URSPRUENGLICHE_ANFRAGE_URL = "urspruenglicheAnfrageUrl";

    /**
     * Identifiziert den Schluessel des Initialisierungs-Parameters fuer auszulassende Url-Pfade, relativ zum
     * ApplicationContext-Root.
     */
    public static final String FILTER_PARAMETER_URLS_TO_SKIP = "urlsToSkip";

    /**
     * Identifiziert den Schluessel des Initialisierungs-Parameters fuer zu cachende Url-Pfade, relativ zum
     * ApplicationContext-Root.
     */
    public static final String FILTER_PARAMETER_URLS_TO_CACHE = "urlsToCache";

    /**
     * Identifiziert den Schluessel des Initialisierungs-Parameters zur Initialisierungsseite der Anwendung.
     */
    public static final String FILTER_PARAMETER_URL_APPLICATION_INITIALISIERUNG =
        "urlApplicationInitialisierung";

    /**
     * Ist die URL auf welche Initialisierunganfragen umgeleitet werden, sofern diese über AJAX ausgeführt
     * werden.
     */
    public static final String FILTER_PARAMETER_AJAX_REDIRECT_URL = "ajaxRedirectUrl";
}
