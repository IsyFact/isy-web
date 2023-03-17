/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;
import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import de.bund.bva.isyfact.konfiguration.common.exception.KonfigurationException;

/**
 * Helper class for the header.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Component
public class HeaderHelper {

    /**
     * Configuration.
     */
    private final Konfiguration konfiguration;

    /**
     * Mapping URL requests (flows) to application group ids.
     */
    private final Map<String, String> flowToAnwendungsgruppe = new HashMap<>();

    @Autowired
    public HeaderHelper(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;

        initialisiereAnwendungsgruppenKonfiguration();
    }

    /**
     * Creates a mapping of URLs to application group IDs.
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
     * Determines the color value of the application group.
     * The value is taken from the {@link NavigationMenuModel} stored in the session.
     * More precisely, the value of the active {@link Applikationsgruppe} is taken.
     * If no {@link Applikationsgruppe} is active, then no color is set.
     *
     * @return the color value of the application group.
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
     * Returns the path to the right header logo.
     *
     * @param request is the {@link HttpServletRequest}
     * @return path that comes from the configuration, fallback to default path.
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
     * Returns the path to the left header logo.
     *
     * @param request is the {@link HttpServletRequest}
     * @return The path that comes from the configuration, fallback to default path.
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
     * Returns the text to be displayed in the span next to the right logo.
     *
     * @param request is the {@link HttpServletRequest}
     * @return The text that comes from the configuration, fallback to default path.
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
     * Determine the allowed application groupIds and write them to configuredGuiApplication groupIds.
     *
     * @return {@code true} if GUI application groupIds are defined.
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
     * Determine the allowed application group URLs and write them to flowToApplication Group (MAP).
     *
     * @return {@code true} if GUI application group urls are defined.
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
     * Determines the flow name from the requestUri.
     *
     * @param request is the {@link HttpServletRequest}
     * @return flow name.
     */
    private String getFlownameFromRequest(HttpServletRequest request) {
        return request.getRequestURI()
                .substring(request.getContextPath().length() + request.getServletPath().length() + 1);
    }

    /**
     * Get the value for a given configuration key from Configuration.
     *
     * @param flowname                 name of the current WebFlow.
     * @param konfigurationsSchluessel the configuration key
     * @return value for the configuration key
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
     * Get the value for a given configuration key from Configuration.
     *
     * @param konfigParam Name of the configuration parameter.
     * @return The configuration value for the given configuration key.
     */
    private String getKonfigurationsWert(String konfigParam) {
        try {
            return konfiguration.getAsString(konfigParam);
        } catch (KonfigurationException ex) {
            return null;
        }
    }

    /**
     * Gets the logout url for the header.
     *
     * @return Logout Url as {@link String}. Default Value: "/logout"
     */
    public String ermittleLogoutUrl() {
        return konfiguration.getAsString(KonfigurationSchluessel.GUI_LOGOUT_URL, "/logout");
    }
}