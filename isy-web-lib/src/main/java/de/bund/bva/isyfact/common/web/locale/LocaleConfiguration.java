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
package de.bund.bva.isyfact.common.web.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Kapselt den Zugriff auf Konfiguration bezüglich Lokalisierung.
 *
 * @author Capgemini
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Component
public class LocaleConfiguration {

    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    @Autowired
    public LocaleConfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    /**
     * Ermittelt die erzwungene Sprache für Maskentexte, falls sie existiert.
     *
     * @return Die erzwungene Sprache oder null, falls keine gesetzt ist.
     */
    public String ermittleForcedLocale() {
        return this.konfiguration.getAsString(KonfigurationSchluessel.GUI_LANGUAGE_FORCED_LOCALE, null);
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
