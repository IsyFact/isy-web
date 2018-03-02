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

import javax.servlet.http.HttpServletRequest;

/**
 * Helper-Klasse für den Header.
 *
 * @author Capgemini, Tobias Groeger
 * @version $Id: HeaderHelper.java 132237 2015-03-09 10:19:17Z sdm_tgroeger $
 */
public class HeaderHelper {

    private Log log = LogFactory.getLog(HeaderHelper.class);

    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    /**
     * Liste zulaessiger Anwendungsgruppen (ausgelesen aus Konfigurationsdatei)
     */
    private List<String> konfigurierteGuiAnwendungsgruppenIds = new ArrayList<>();



    /**
     * Zuordnung von URL-Request-(Flows)  zu AnwendungsgruppenIds.
     */
    private Map flowToAnwendungsgruppe = new Hashtable();

    ;

    /**
     * Ermittelt den Farbwert der Anwendungsgruppe. Der Wert wird aus der Konfiguration gelesen. Wenn kein
     * Wert konfiguriert ist, dann wird "#337299" verwendet.
     *
     * @return der Farbwert der Anwendungsgruppe
     */
    public String ermittleFarbwertAnwendungsgruppe() {
        return this.konfiguration
            .getAsString(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPE_FARBWERT, "#337299");
    }

    /**
     * Gibt die XHTML Quelle für den Nutzerbereich zurück.
     *
     * @return Die Quelle.
     */
    public String ermittleXhtmlSrcNutzerbereich() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_NUTZERBEREICH_XHTML_SRC);
    }

    /**
     * Gibt den Pfad zum rechten Header Logo zurück.
     *
     * @return Den Pfad, der aus der Konfiguration stammt.
     */
    public String ermittlePfadHeaderLogoRechts() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_RECHTS_PFAD, "");
    }

    /**
     * Gibt den Pfad zum linken Header Logo zurück.
     *
     * @return Den Pfad, der aus der Konfiguration stammt.
     */
    public String ermittlePfadHeaderLogoLinks(HttpServletRequest request) {
        System.out.print("--------aa---------" + request.getRequestURI().toString() + "----");

        boolean anwendungsgruppenSindVorhanden = ermittleGueltigeAnwendungsgruppenIds();
        System.out.print("-------bool--------" + anwendungsgruppenSindVorhanden + "----");

        boolean urlsSindVorhanden = ermittleKonfigurierteAnwendungsgruppnUrls();

        //String applicationContextPfad = request.getContextPath();
        System.out.println(konfigurierteGuiAnwendungsgruppenIds);
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_LINKS_PFAD, "");
    }


    /**
     * Gibt den Text, der im span neben dem rechten Logo angezeigt werden soll, zurück.
     *
     * @return Den Text, der aus der Konfiguration stammt.
     */
    public String ermittleTextHeaderLogoRechts() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_TEXT_LOGO_RECHTS, "");
    }


    /**
     * Ermittle die zulässigen AnwendungsgruppenIds.
     *
     * @param
     * @return true, wenn GUI-Anwendungsgruppen-Ids definiert sind.
     */
    private boolean ermittleGueltigeAnwendungsgruppenIds() {
        // Liste der Anwendungsgruppen Ids existiert schon
        if (!ObjectUtils.isEmpty(konfigurierteGuiAnwendungsgruppenIds)) {
            return true;
        }

        String anwendungsgruppenIds =
            this.konfiguration.getAsString(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_IDS);

        if (!Strings.isNullOrEmpty(anwendungsgruppenIds)) {
            this.konfigurierteGuiAnwendungsgruppenIds = Lists
                .newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenIds));
            return true;
        }
        return false;
    }

    /**
     * Ermittle die zulässigen AnwendungsgruppenUrls.
     *
     * @param
     * @return true, wenn GUI-Anwendungsgruppen-Urls definiert sind.
     */
    private boolean ermittleKonfigurierteAnwendungsgruppnUrls() {
        /**
         * Liste zulaessiger Anwendungsgruppen (ausgelesen aus Konfigurationsdatei)
         */
        List<String> anwendungsgruppenUrlListe = new ArrayList<>();

        for (String id : this.konfigurierteGuiAnwendungsgruppenIds) {
            String anwendungsgruppenUrls =
                this.konfiguration.getAsString(KonfigurationSchluessel.GUI_ANWENDUNGSGRUPPEN_URLS + "." + id);
            if (!Strings.isNullOrEmpty(anwendungsgruppenUrls)) {
                anwendungsgruppenUrlListe = Lists.newArrayList(
                    Splitter.on(",").omitEmptyStrings().trimResults().split(anwendungsgruppenUrls));
            }

            for (String url : anwendungsgruppenUrlListe) {
                flowToAnwendungsgruppe.put(url, id);
            }
            anwendungsgruppenUrlListe.clear();
        }

        return false;
    }


    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}