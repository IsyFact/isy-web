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
 * Diese Klasse enthält Ereignisschlüssel für das Logging.
 */
public class EreignisSchluessel {

    /** Keine Objekt-ID im Request-String {} gefunden. */
    public static final String E_OBJEKT_ID = "EPLWEB00001";

    /** Keine Ressource {} gefunden. */
    public static final String E_RESSOURCE = "EPLWEB00002";

    public static final String E_FEHLER_FACHDATEN = "EPLWEB00003";

    public static final String E_FEHLER = "EPLWEB00004";

    /** Client-Verbindungsfehler. */
    public static final String E_CLIENT_VERBINDUNG = "EPLWEB00005";

    /**
     * Es wurde ein negatives Löschintervall für die TEMP_WEB_RESOURCE-Tabelle konfiguriert. Verwende
     * Standardwert 1 Stunde.
     */
    public static final String E_KONFIGURATION = "EPLWEB00006";

    /** Bereinige Datenbank - Lösche Einträge aus Tabelle TEMP_WEB_RESOURCE, die älter als {} sind. */
    public static final String E_DATENBANK_BEREINIGUNG = "EPLWEB00007";

    /** Fehler beim Weiterleiten auf die anwendungsinterne Fehlerseite. */
    public static final String E_WEITERLEITUNG_FEHLERSEITE = "EPLWEB00008";

    /** Setzte Locale {} */
    public static final String E_LOKALISIERUNG = "EPLWEB00009";

    /** behalte Sitemap Daten für: {} */
    public static final String E_SITEMAP_DATEN_ERHALT = "EPLWEB00010";

    /**
     * Rollen für die Applikationsgruppe '{}' wurden aufgrund einer oder mehrerer Anwendungen innerhalb der
     * Gruppe ignoriert.
     */
    public static final String E_NAVMENU_ROLLEN_IGNORE = "EPLWEB00011";

    /**
     * Die Navigation konnte nicht geladen werden, weil der benötigte Schlüsselkatalog nicht abgerufen werden
     * konnte.
     */
    public static final String E_NAVMENU_SCHLUSSELKATALOG_NICHT_VORHANDEN = "EPLWEB00012";

    /**
     * Beim Laden der Navigation wurde versucht, die Eigenschaft '{}' auszulesen. Dabei konnte der Wert '{}'
     * nicht in eine Zahl umgewandelt werden. Es wurde auf einen Standardwert zurückgegriffen.
     */
    public static final String E_NAVMENU_SCHLUSSELKATALOG_WERT_NICHT_KORREKT_AUSGELESEN = "EPLWEB00013";
}
