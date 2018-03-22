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

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;
import de.bund.bva.pliscommon.konfiguration.common.exception.KonfigurationException;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper-Klasse für den Header.
 */
public class HeaderHelper {

    private Log log = LogFactory.getLog(HeaderHelper.class);

    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    /**
     * Aktueller Flowname.
     */
    private String flowname;

    /**
     * Flag, ob AnwendungsgruppenIds ueberhaupt konfiguriert sind.
     */
    private boolean hatAnwendungsgruppenIds = false;

    /**
     * Liste konfigurierter Anwendungsgruppen (ausgelesen aus Konfigurationsdatei)
     */
    private List<String> konfigurierteGuiAnwendungsgruppenIds = new ArrayList<>();

    /**
     * Zuordnung von URL-Request-(Flows) zu AnwendungsgruppenIds.
     */
    private Map<String, String> flowToAnwendungsgruppe = new Hashtable<String, String>();


    /**
     * Ermittelt den Farbwert der Anwendungsgruppe. Der Wert wird aus der Konfiguration gelesen. Wenn kein
     * Wert konfiguriert ist, dann wird "#337299" verwendet.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return der Farbwert der Anwendungsgruppe
     */
    public String ermittleFarbwertAnwendungsgruppe(HttpServletRequest request) {
        this.flowname = getFlownameFromRequest(request);
        String konfigValue =
            getKonfigurationsWert(flowname, KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPE_FARBWERT);

        if (konfigValue != null) {
            return konfigValue;
        } else {
            return this.konfiguration
                .getAsString(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPE_FARBWERT, "#337299");
        }
    }

    /**
     * Gibt die XHTML Quelle für den Nutzerbereich zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Die Quelle.
     */
    public String ermittleXhtmlSrcNutzerbereich(HttpServletRequest request) {
        this.flowname = getFlownameFromRequest(request);
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_NUTZERBEREICH_XHTML_SRC);
    }

    /**
     * Gibt den Pfad zum rechten Header Logo zurück.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Pfad, der aus der Konfiguration stammt, Fallback auf Defaultpfad.
     */
    public String ermittlePfadHeaderLogoRechts(HttpServletRequest request) {
        this.flowname = getFlownameFromRequest(request);
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
        this.flowname = getFlownameFromRequest(request);
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
        this.flowname = getFlownameFromRequest(request);
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
    private boolean ermittleGueltigeAnwendungsgruppenIds() {
        // Liste der Anwendungsgruppen Ids existiert schon
        if (hatAnwendungsgruppenIds) {
            return true;
        }else{
            setHatAnwendungsgruppenIds(true);
        }

        String anwendungsgruppenIds = getKonfigurationsWert(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_IDS);

        if (!Strings.isNullOrEmpty(anwendungsgruppenIds)) {
            this.konfigurierteGuiAnwendungsgruppenIds = Lists
                .newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenIds));
            return true;
        }
        return false;
    }

    /**
     * Ermittle die zulässigen AnwendungsgruppenUrls und schreibe diese in flowToAnwendungsgruppe (MAP).
     *
     * @return true, wenn GUI-Anwendungsgruppen-Urls definiert sind.
     */
    private List<String> ermittleKonfigurierteAnwendungsgruppnUrls(String gruppenId) {
        // Liste zulaessiger Anwendungsgruppen (ausgelesen aus Konfigurationsdatei)
        List<String> anwendungsgruppenUrlListe = new ArrayList<>();

        String anwendungsgruppenUrls = getKonfigurationsWert(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_URLS + "." + gruppenId);

        if (!Strings.isNullOrEmpty(anwendungsgruppenUrls)) {
            anwendungsgruppenUrlListe = Lists
                .newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenUrls));
        }

        return anwendungsgruppenUrlListe;
    }


    /**
     * Ermittelt den Flownamen aus dem RequestUri.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return Flownamen.
     */
    private String getFlownameFromRequest(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length() +
                                                 request.getServletPath().length() +
                                                 1);
    }

    /**
     * Existenz der Map von Flownamen zu AnwendungsgruppenIds prüfen.
     * (ggf. initiale Erstellung der Zuordnungsmap).
     *
     * @return true ().
     */
    private boolean hasFlowToAnwendungsgruppe() {
        // Liste der Anwendungsgruppen Ids existiert schon
        if (!ObjectUtils.isEmpty(flowToAnwendungsgruppe)) {
            return true;
        }

        if(ermittleGueltigeAnwendungsgruppenIds()){
            erzeugeFlowToAnwendungsgruppe();
        }
        return true;
    }

    /**
     * Ermittle die zulässigen AnwendungsgruppenUrls und schreibe diese in flowToAnwendungsgruppe (MAP).
     *
     * @param
     * @return true, wenn GUI-Anwendungsgruppen-Urls definiert sind.
     */
    private boolean erzeugeFlowToAnwendungsgruppe() {
        List<String> anwendungsgruppenUrlListe;

        for (String id : this.konfigurierteGuiAnwendungsgruppenIds) {
            anwendungsgruppenUrlListe = ermittleKonfigurierteAnwendungsgruppnUrls(id);
            for (String url : anwendungsgruppenUrlListe) {
                flowToAnwendungsgruppe.put(url, id);
            }
            anwendungsgruppenUrlListe.clear();
        }
        return true;
    }

    /**
     * Hole den Wert für einen gegebenen Konfigurationsschluessel aus Konfiguration.
     *
     * @param flowname  Name des aktuellen WebFlows
     * @param konfigurationsSchluessel  Key in der Klasse Konfiguration
     * @return true, wenn GUI-Anwendungsgruppen-Urls definiert sind.
     */
    private String getKonfigurationsWert(String flowname, String konfigurationsSchluessel) {

        if (hasFlowToAnwendungsgruppe()) {
            String gruppenId = flowToAnwendungsgruppe.get(flowname);
            String konfigParam = konfigurationsSchluessel + "." + gruppenId;
            try {
                return this.konfiguration.getAsString(konfigParam);
            } catch (KonfigurationException ex) {
                log.error(
                    "Konfigurationsparameter " + konfigParam + " ist in Properties-Datei nicht vorhanden.");
                // Fallbackfall: weiter im Code und auf die jeweiligen Standardwerte zugreifen
                return null;
            }
        }
        return null;
    }

    /**
     * Hole den Wert für einen gegebenen Konfigurationsschluessel aus Konfiguration.
     *
     * @param konfigParam  Name des Konfigurationsparameters
     * @return Der Konfigurationswert für den übergebenen Konfigurationsparameter.
     */
    private String getKonfigurationsWert(String konfigParam) {

            try {
                return this.konfiguration.getAsString(konfigParam);
            } catch (KonfigurationException ex) {
                log.error(
                    "Konfigurationsparameter " + konfigParam + " ist in Properties-Datei nicht vorhanden.");
                // Fallbackfall: weiter im Code und auf die jeweiligen Standardwerte zugreifen
                return null;
            }
    }

    public void setHatAnwendungsgruppenIds(boolean boolWert) {
        this.hatAnwendungsgruppenIds = boolWert;
    }

    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

}