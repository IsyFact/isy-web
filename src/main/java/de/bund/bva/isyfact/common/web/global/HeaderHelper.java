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
package de.bund.bva.isyfact.common.web.global;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;
import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import de.bund.bva.isyfact.konfiguration.common.exception.KonfigurationException;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

/**
 * Helper-Klasse für den Header.
 */
public class HeaderHelper {

    /**
     * Die Konfiguration.
     */
    private final Konfiguration konfiguration;

    /**
     * Zuordnung von URL-Requests (Flows) zu Anwendungsgruppen-Ids.
     */
    private final Map<String, String> flowToAnwendungsgruppe = new HashMap<>();

    public HeaderHelper(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;

        initialisiereAnwendungsgruppenKonfiguration();
    }

    /**
     * Erstellt eine Zuordnung von URLs zu Anwendungsgruppen-IDs.
     */
    private void initialisiereAnwendungsgruppenKonfiguration() {
        List<String> anwendungsgruppenIds = ermittleAnwendungsgruppenIds();

        for (String id : anwendungsgruppenIds) {
            for (String url : ermittleAnwendungsgruppenUrls(id)) {
                flowToAnwendungsgruppe.put(url, id);
            }
        }
    }

    /**
     * Ermittelt den Farbwert der Anwendungsgruppe.
     * Der Wert wird dem {@link NavigationMenuModel} entnommen, das in der Session abgelegt ist.
     * Genauer wird der Wert der aktiven {@link Applikationsgruppe} genommen.
     * Sollte keine {@link Applikationsgruppe} aktiv sein, dann wird keine Farbe gesetzt.
     *
     * @return der Farbwert der Anwendungsgruppe
     */
    public String ermittleFarbwertAnwendungsgruppe() {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();
        synchronized (sessionMap.getMutex()) {
            NavigationMenuModel navigationMenuModel =
                (NavigationMenuModel) sessionMap.get(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU);
            if (navigationMenuModel != null) {
                List<Applikationsgruppe> applikationListe = navigationMenuModel.getApplikationsListe();
                for (Applikationsgruppe applikation : applikationListe) {
                    if (applikation.isAktiv()) {
                        return applikation.getFarbe();
                    }
                }
            }
        }

        return "";
    }

    /**
     * Gibt die XHTML Quelle für den Nutzerbereich zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Die Quelle.
     */
    public String ermittleXhtmlSrcNutzerbereich(HttpServletRequest request) {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_NUTZERBEREICH_XHTML_SRC);
    }

    /**
     * Gibt den Pfad zum rechten Header Logo zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Pfad, der aus der Konfiguration stammt, Fallback auf Defaultpfad.
     */
    public String ermittlePfadHeaderLogoRechts(HttpServletRequest request) {
        String flowname = getFlownameFromRequest(request);
        String konfigValue =
            getKonfigurationsWert(flowname, KonfigurationSchluessel.GUI_HEADER_LOGO_RECHTS_PFAD);

        if (konfigValue != null) {
            return konfigValue;
        } else {
            return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_RECHTS_PFAD, "");
        }
    }

    /**
     * Gibt den Pfad zum linken Header Logo zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Den Pfad, der aus der Konfiguration stammt, Fallback auf Defaultpfad.
     */
    public String ermittlePfadHeaderLogoLinks(HttpServletRequest request) {
        String flowname = getFlownameFromRequest(request);
        String konfigValue =
            getKonfigurationsWert(flowname, KonfigurationSchluessel.GUI_HEADER_LOGO_LINKS_PFAD);

        if (konfigValue != null) {
            return konfigValue;
        } else {
            return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_LINKS_PFAD, "");
        }
    }

    /**
     * Gibt den Text, der im span neben dem rechten Logo angezeigt werden soll, zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Den Text, der aus der Konfiguration stammt, Fallback auf Defaultpfad.
     */
    public String ermittleTextHeaderLogoRechts(HttpServletRequest request) {
        String flowname = getFlownameFromRequest(request);
        String konfigValue =
            getKonfigurationsWert(flowname, KonfigurationSchluessel.GUI_HEADER_TEXT_LOGO_RECHTS);

        if (konfigValue != null) {
            return konfigValue;
        } else {
            return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_TEXT_LOGO_RECHTS, "");
        }
    }

    /**
     * Ermittle die zulässigen AnwendungsgruppenIds und schreibe diese in konfigurierteGuiAnwendungsgruppenIds.
     *
     * @return true, wenn GUI-Anwendungsgruppen-Ids definiert sind.
     */
    private List<String> ermittleAnwendungsgruppenIds() {
        String anwendungsgruppenIds =
            getKonfigurationsWert(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_IDS);

        if (!Strings.isNullOrEmpty(anwendungsgruppenIds)) {
            return Lists
                .newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenIds));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Ermittle die zulässigen Anwendungsgruppen-URLs und schreibe diese in flowToAnwendungsgruppe (MAP).
     *
     * @return true, wenn GUI-Anwendungsgruppen-Urls definiert sind.
     */
    private List<String> ermittleAnwendungsgruppenUrls(String gruppenId) {
        String anwendungsgruppenUrls =
            getKonfigurationsWert(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_URLS + "." + gruppenId);

        if (!Strings.isNullOrEmpty(anwendungsgruppenUrls)) {
            return Lists
                .newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenUrls));
        } else {
            return Collections.emptyList();
        }
    }


    /**
     * Ermittelt den Flownamen aus dem RequestUri.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Flownamen.
     */
    private String getFlownameFromRequest(HttpServletRequest request) {
        return request.getRequestURI()
            .substring(request.getContextPath().length() + request.getServletPath().length() + 1);
    }

    /**
     * Hole den Wert für einen gegebenen Konfigurationsschluessel aus Konfiguration.
     *
     * @param flowname                 Name des aktuellen WebFlows
     * @param konfigurationsSchluessel der Konfigurationsschlüssel
     * @return Wert für den Konfigurationsschlüssel
     */
    private String getKonfigurationsWert(String flowname, String konfigurationsSchluessel) {

        if (!flowToAnwendungsgruppe.isEmpty()) {
            String gruppenId = flowToAnwendungsgruppe.get(flowname);
            String konfigParam = konfigurationsSchluessel + "." + gruppenId;

            return getKonfigurationsWert(konfigParam);
        }
        return null;
    }

    /**
     * Hole den Wert für einen gegebenen Konfigurationsschluessel aus Konfiguration.
     *
     * @param konfigParam Name des Konfigurationsparameters
     * @return Der Konfigurationswert für den übergebenen Konfigurationsschlüssel.
     */
    private String getKonfigurationsWert(String konfigParam) {
        try {
            return konfiguration.getAsString(konfigParam);
        } catch (KonfigurationException ex) {
            return null;
        }
    }
}