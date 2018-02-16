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

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;
import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Helper-Klasse für den Header.
 * @author Capgemini, Tobias Groeger
 * @version $Id: HeaderHelper.java 132237 2015-03-09 10:19:17Z sdm_tgroeger $
 */
public class HeaderHelper {

    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    /**
     * Der Farbwert, der standardmäßig gesetzt wird, wenn sich anhand des {@link NavigationMenuModel} kein
     * Farbwert ermitteln lässt. Siehe {@link #ermittleFarbwertAnwendungsgruppe(NavigationMenuModel)}.
     */
    private static final String DEFAULT_FARBWERT = "#337299";

    /**
     * Ermittelt den Farbwert der Anwendungsgruppe. Der Wert wird dem {@link NavigationMenuModel} entnommen,
     * das in der Session abgelegt ist. Genauer wird der Wert der aktiven {@link Applikationsgruppe} genommen. Sollte
     * (theoretisch) keine {@link Applikationsgruppe} aktiv sein, dann wird "#337299" verwendet.
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

        return DEFAULT_FARBWERT;
    }

    /**
     * Gibt die XHTML Quelle für den Nutzerbereich zurück.
     * @return Die Quelle.
     */
    public String ermittleXhtmlSrcNutzerbereich() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_NUTZERBEREICH_XHTML_SRC);
    }

    /**
     * Gibt den Pfad zum rechten Header Logo zurück.
     * @return Den Pfad, der aus der Konfiguration stammt.
     */
    public String ermittlePfadHeaderLogoRechts() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_RECHTS_PFAD, "");
    }

    /**
     * Gibt den Pfad zum linken Header Logo zurück.
     * @return Den Pfad, der aus der Konfiguration stammt.
     */
    public String ermittlePfadHeaderLogoLinks() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_LOGO_LINKS_PFAD, "");
    }

    /**
     * Gibt den Text, der im span neben dem rechten Logo angezeigt werden soll, zurück.
     * @return Den Text, der aus der Konfiguration stammt.
     */
    public String ermittleTextHeaderLogoRechts() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_HEADER_TEXT_LOGO_RECHTS, "");
    }

    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
