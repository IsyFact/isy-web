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
 * Konstantenklasse für Konfigurationsschlüssel.
 *
 */
public final class KonfigurationSchluessel {

    private KonfigurationSchluessel () {}

    /**
     * Der zu inkludierende Nutzerbereich.
     */
    public static final String GUI_HEADER_NUTZERBEREICH_XHTML_SRC = "gui.header.nutzerbereich.xhtml.src";

    /**
     * Die erzwungene Sprache für Maskentexte, beispielsweise nur Deutsch.
     */
    public static final String GUI_LANGUAGE_FORCED_LOCALE = "gui.language.forced.locale";

    /**
     * The logout url as {@link String}
     */
    public static final String GUI_LOGOUT_URL = "gui.logout.url";

    /**
     * Die Systemversion.
     */
    public static final String SYSTEM_VERSION = "system.version";

    /**
     * Der Systemname.
     */
    public static final String SYSTEM_NAME = "system.name";

    /**
     * Ob die Versionsnummer im Seitentitel angezeigt werden soll.
     */
    public static final String GUI_VERSIONSANZEIGE_SEITENTITEL_AKTIV =
        "gui.versionsanzeige.seitentitel.aktiv";

    /**
     * Der relative Pfad zum Logo, das im Header rechts angezeigt werden soll. Bsp.:
     * /resources/img/logo-rechts.png
     */
    public static final String GUI_HEADER_LOGO_RECHTS_PFAD = "gui.header.logo.rechts.pfad";

    /**
     * Der relative Pfad zum Logo, das im Header links angezeigt werden soll. Bsp.:
     * /resources/img/logo-links.png
     */
    public static final String GUI_HEADER_LOGO_LINKS_PFAD = "gui.header.logo.links.pfad";

    /**
     * Der Text, der im span neben dem rechten Logo angezeigt werden soll.
     */
    public static final String GUI_HEADER_TEXT_LOGO_RECHTS = "gui.header.text.logo.rechts";

    /**
     * Zweistellige Jahreszahlen werden bei einer Datumsangabe ergänzt zu 19XX bzw. 20XX. Die Grenze legt
     * fest, bis welchen Wert 20XX ergänzt wird bzw. ab welchem Wert 19XX ergänzt wird. Dabei werden Grenzwert
     * und das aktuelle Jahr addiert, damit der resultierende Wert im Laufe der Jahre mitläuft. Beispiel für
     * konfigurierte Grenze 3 und Jahr 2017: Eingaben 0-20 werden auf 2000-2020 ergänzt und Eingaben 21-99
     * werden auf 1921-1999 ergänzt.
     */
    public static final String GUI_DATUMSANGABE_JAHRESZAHLEN_ERGAENZEN_GRENZE =
        "gui.datumsangabe.jahreszahlen.ergaenzen.grenze";

    /**
     * Schluesselwert fuer die Extraktion der Anwendungsgruppen-Ids aus der Konfigurationsdatei.
     */
    public static final String GUI_ANWENDUNGSGRUPPEN_IDS = "gui.anwendungsgruppen.ids";

    /**
     * Schluesselwert fuer die Extraktion der Anwendungsgruppen-URLs aus der Konfigurationsdatei.
     */
    public static final String GUI_ANWENDUNGSGRUPPEN_URLS = "gui.anwendungsgruppen.urls";
}
