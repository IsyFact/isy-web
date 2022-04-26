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
 * Constant class for configuration keys.
 */
public final class KonfigurationSchluessel {

    private KonfigurationSchluessel() {
    }

    /**
     * The forced language for mask texts, for example German only.
     */
    public static final String GUI_LANGUAGE_FORCED_LOCALE = "gui.language.forced.locale";

    /**
     * The system version.
     */
    public static final String SYSTEM_VERSION = "system.version";

    /**
     * The system name.
     */
    public static final String SYSTEM_NAME = "system.name";

    /**
     * Whether to display the version number in the page title.
     */
    public static final String GUI_VERSIONSANZEIGE_SEITENTITEL_AKTIV =
            "gui.versionsanzeige.seitentitel.aktiv";

    /**
     * The relative path to the logo to be displayed in the header on the right.
     * Example: /resources/img/logo-right.png
     */
    public static final String GUI_HEADER_LOGO_RECHTS_PFAD = "gui.header.logo.rechts.pfad";

    /**
     * The relative path to the logo to be displayed in the header on the left.
     * Example: /resources/img/logo-links.png
     */
    public static final String GUI_HEADER_LOGO_LINKS_PFAD = "gui.header.logo.links.pfad";

    /**
     * The text to be displayed in the span next to the right logo.
     */
    public static final String GUI_HEADER_TEXT_LOGO_RECHTS = "gui.header.text.logo.rechts";

    /**
     * Two-digit years are supplemented to 19XX or 20XX when a date is specified. The limit defines
     * defines up to which value 20XX is added or from which value 19XX is added. The limit value
     * and the current year are added, so that the resulting value runs along in the course of the years. Example for
     * configured limit 3 and year 2017: Inputs 0-20 are supplemented to 2000-2020 and inputs 21-99
     * are supplemented to 1921-1999.
     */
    public static final String GUI_DATUMSANGABE_JAHRESZAHLEN_ERGAENZEN_GRENZE =
            "gui.datumsangabe.jahreszahlen.ergaenzen.grenze";

    /**
     * Key value for extracting the application group ids from the configuration file.
     */
    public static final String GUI_ANWENDUNGSGRUPPEN_IDS = "gui.anwendungsgruppen.ids";

    /**
     * Key value for extracting the application group URLs from the configuration file.
     */
    public static final String GUI_ANWENDUNGSGRUPPEN_URLS = "gui.anwendungsgruppen.urls";

    /**
     * The logout url.
     */
    public static final String GUI_LOGOUT_URL = "gui.logout.url";
}
