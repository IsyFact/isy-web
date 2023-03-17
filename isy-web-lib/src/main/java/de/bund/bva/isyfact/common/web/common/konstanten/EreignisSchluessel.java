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
package de.bund.bva.isyfact.common.web.common.konstanten;

/**
 * This class contains event keys for logging.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class EreignisSchluessel {

    /** No object id found in request String {}. */
    public static final String E_OBJEKT_ID = "EPLWEB00001";

    /** No resource {} found. */
    public static final String E_RESSOURCE = "EPLWEB00002";

    public static final String E_FEHLER_FACHDATEN = "EPLWEB00003";

    public static final String E_FEHLER = "EPLWEB00004";

    /** Client connection error. */
    public static final String E_CLIENT_VERBINDUNG = "EPLWEB00005";

    /**
     * A negative delete interval has been configured for the TEMP_WEB_RESOURCE table.
     * Use default value: 1 hour.
     */
    public static final String E_KONFIGURATION = "EPLWEB00006";

    /** Clean database - delete entries from table TEMP_WEB_RESOURCE that are older than {}. */
    public static final String E_DATENBANK_BEREINIGUNG = "EPLWEB00007";

    /** Error while redirecting to the application internal error page. */
    public static final String E_WEITERLEITUNG_FEHLERSEITE = "EPLWEB00008";

    /** Set locale {}. */
    public static final String E_LOKALISIERUNG = "EPLWEB00009";

    /** Keep sitemap data for {}. */
    public static final String E_SITEMAP_DATEN_ERHALT = "EPLWEB00010";

    /**
     * Roles for the application group '{}' will be ignored due to one or more applications inside the group.
     */
    public static final String E_NAVMENU_ROLLEN_IGNORE = "EPLWEB00011";

    /**
     * The navigation could not be loaded because the required Schlüsselkatatlog could not be retrieved.
     */
    public static final String E_NAVMENU_SCHLUSSELKATALOG_NICHT_VORHANDEN = "EPLWEB00012";

    /**
     * When loading the navigation, an attempt was made to read the '{}' property. The value '{}' could
     * not be converted into a number. A default value was used.
     */
    public static final String E_NAVMENU_SCHLUSSELKATALOG_WERT_NICHT_KORREKT_AUSGELESEN = "EPLWEB00013";

    /**
     * Resource file of characters used by the character picker is not specified correctly.
     */
    public static final String E_SONDERZEICHEN_EINLESEN_FEHLER = "EPLWEB00014";
}
