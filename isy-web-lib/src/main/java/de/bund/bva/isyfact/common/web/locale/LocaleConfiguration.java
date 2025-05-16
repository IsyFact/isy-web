package de.bund.bva.isyfact.common.web.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Kapselt den Zugriff auf Konfiguration bezüglich Lokalisierung.
 *
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
