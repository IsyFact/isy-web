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
package de.bund.bva.isyfact.common.web.konstanten;

/**
 * Enthaelt guibezogene Parameter-Schluessel.
 *
 * @author Capgemini
 * @version $Id: GuiParameterSchluessel.java 137477 2015-05-26 13:46:06Z sdm_ahoerning $
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
