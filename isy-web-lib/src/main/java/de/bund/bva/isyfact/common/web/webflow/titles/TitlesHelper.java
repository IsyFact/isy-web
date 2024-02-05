package de.bund.bva.isyfact.common.web.webflow.titles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Helper-Klasse für den Seitentitel.
 *
 * @author Capgemini
 * @version $Id:$
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Component
public class TitlesHelper {
    /**
     * Zugriff auf die Konfiguration, damit die benötigten Werte für die Anzeige der Versionsnummer im Titel
     * ausgelesen werden können.
     */
    private Konfiguration konfiguration;

    @Autowired
    public TitlesHelper(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    /**
     * Prüft ob die Versionsanzeige in der Anwendung aktiviert ist.
     *
     * @return {@code true}, falls die Versionanzeige aktiviert ist. Ansonsten {@code false}. Wenn der
     * entsprechende Eintrag in der Konfiguration gar nicht vorhanden ist, wird {@code false}
     * zurückgegeben und somit die Abwärtskompatibilität gewährleistet.
     */
    private boolean istVersionsanzeigeAktiviert() {
        boolean aktiv = this.konfiguration
                .getAsBoolean(KonfigurationSchluessel.GUI_VERSIONSANZEIGE_SEITENTITEL_AKTIV, false);
        return aktiv;
    }

    /**
     * Ermittelt den Text, der im Titel zusätzlich angezeigt werden soll, wenn die entsprechende
     * Konfigurations-Eigenschaft aktiviert ist. Der Text enthält - sofern gesetzt - den Anwendungsnamen und
     * die Versionsnummer der Anwendung.
     *
     * @return Der ermittelte Text.
     */
    public String ermittleVersionsnummer() {
        if (istVersionsanzeigeAktiviert()) {
            String anwendung = this.konfiguration.getAsString(KonfigurationSchluessel.SYSTEM_NAME, null);
            String version = this.konfiguration.getAsString(KonfigurationSchluessel.SYSTEM_VERSION, null);
            StringBuilder ergebnis = new StringBuilder("");

            // Es muss mindestens die Versionsnummer gesetzt sein. Der Anwendungsname ist optional. Wenn die
            // Versionsnummer nicht gesetzt ist, wird der leere String zurückgegeben.
            if (version != null) {
                ergebnis.append(" (");
                if (anwendung != null) {
                    ergebnis.append(anwendung + " ");
                }
                ergebnis.append("v" + version + ")");
            }

            return ergebnis.toString();
        } else {
            return null;
        }
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
