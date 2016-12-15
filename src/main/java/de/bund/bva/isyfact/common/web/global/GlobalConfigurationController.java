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

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContextHolder;

import com.google.common.base.Strings;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;

/**
 * Der Controller zum Verwalten von globalen Einstellungen.
 *
 * @author Capgemini, Andreas Hörning.
 * @author Capgemini, Jonas Zitz
 * @version $Id: GlobalConfigurationController.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
public class GlobalConfigurationController implements GuiController {

    /** Zur Identifizierung eines Internet Explorers aus einem UserAgent-String. */
    private String internetExplorerAgentIdentifier = "MSIE ";

    /**
     * Initialsiert das GlobalConfigurationModel.
     * @return Das GlobalConfigurationModel.
     */
    public GlobalConfigurationModel initializeConfigurationModel() {
        GlobalConfigurationModel globalConfigurationModel = new GlobalConfigurationModel();

        // Registriere JavaScript: de-/aktiviert
        registriereJavascriptAktiviert(globalConfigurationModel);

        // Registriere Client-Browserversion
        registriereClientBrowserVersion(globalConfigurationModel);

        return globalConfigurationModel;
    }

    /**
     * Registiert die De-/Aktivierung von Javascript im {@link GlobalConfigurationModel}.
     *
     * @param globalConfigurationModel
     *            it das {@link GlobalConfigurationModel}
     */
    private void registriereJavascriptAktiviert(GlobalConfigurationModel globalConfigurationModel) {
        Map<String, Object> sessionMap =
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Object jsEnabledObject = sessionMap.get(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL);
        if (jsEnabledObject != null && jsEnabledObject instanceof Boolean) {
            Boolean jsEnabled = (Boolean) jsEnabledObject;
            globalConfigurationModel.setJsEnabled(jsEnabled.booleanValue());
        }
    }

    /**
     * Registriere die Client-{@link WebBrowserVersion}.
     *
     * @param globalConfigurationModel
     *            ist das {@link GlobalConfigurationModel}
     */
    private void registriereClientBrowserVersion(GlobalConfigurationModel globalConfigurationModel) {
        // Pruefe, ob es sich um einen IE <7, 7 oder 8 handelt
        WebBrowserVersion webBrowserVersion = ermittleWebBrowserVersion();

        // Registriere die HTML-Tag-Css-Klassen zur Webbrowser-Version
        globalConfigurationModel.setWebBrowserVersion(webBrowserVersion);
    }

    /**
     * Ermittelt die Browser-Version.
     *
     * @return Die Browser-Version.
     */
    private WebBrowserVersion ermittleWebBrowserVersion() {
        boolean isInternetExplorer = isClientBrowserInternetExplorer();
        if (!isInternetExplorer) {
            return WebBrowserVersion.SONSTIGER_WEBBROWSER;
        }

        // Extrahiere Internet-Explorer-Version
        int internetExplorerVersion = getInternetExplorerVersion();

        if (internetExplorerVersion == -1) {
            return WebBrowserVersion.SONSTIGER_WEBBROWSER;
        } else if (internetExplorerVersion < 7) {
            return WebBrowserVersion.INTERNET_EXPLORER_VERSION_KLEINER_7;
        } else if (internetExplorerVersion == 7) {
            return WebBrowserVersion.INTERNET_EXPLORER_VERSION_7;
        } else if (internetExplorerVersion == 8) {
            return WebBrowserVersion.INTERNET_EXPLORER_VERSION_8;
        }

        // Es handelt sich nicht um einen der o.a. speziellen Browser
        return WebBrowserVersion.SONSTIGER_WEBBROWSER;
    }

    /**
     * Extrahiert aus dem UserAgent-String, ob es sich beim Client um einen Internet Explorer handelt.
     * @return <code>true</code>, falls es sich um einen Internet Explorer handelt. <code>false</code>
     *         ansonsten.
     */
    private boolean isClientBrowserInternetExplorer() {
        String userAgent =
            FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("User-Agent");
        if (Strings.isNullOrEmpty(userAgent)) {
            return false;
        }

        if (userAgent.contains(this.internetExplorerAgentIdentifier)) {
            return true;
        }

        return false;
    }

    /**
     * Extrahiert aus dem UserAgent-String die Internet Explorer Version.
     * @return die InternetExplorer-Version oder -1, falls diese nicht extrahiert werden konnte
     */
    private int getInternetExplorerVersion() {
        String userAgent =
            FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("User-Agent");
        int internetExplorerVersionStartPosition = userAgent.indexOf(this.internetExplorerAgentIdentifier)
            + this.internetExplorerAgentIdentifier.length();
        int internetExplorerVersionEndPosition = userAgent.indexOf(".", internetExplorerVersionStartPosition);
        String internetExplorerVersionString =
            userAgent.substring(internetExplorerVersionStartPosition, internetExplorerVersionEndPosition);
        int internetExplorerVersion = -1;
        try {
            internetExplorerVersion = Integer.parseInt(internetExplorerVersionString);
        } catch (NumberFormatException e) {
            // Die IE-Version wird mit -1 belegt.
            internetExplorerVersion = -1;
        }

        return internetExplorerVersion;
    }

    /**
     * Extrahiert ein Model vom Typ {@link GlobalConfigurationModel} aus dem FlowScope.
     * @return das {@link GlobalConfigurationModel} oder <code>null</code>, falls dieses nicht gefunden wurde.
     */
    public GlobalConfigurationModel getModelZuController() {

        MutableAttributeMap<Object> mutableAttributeMap =
            RequestContextHolder.getRequestContext().getFlowScope();

        GlobalConfigurationModel globalConfigurationModel =
            (GlobalConfigurationModel) mutableAttributeMap.get("globalConfigurationModel");

        return globalConfigurationModel;
    }

}
