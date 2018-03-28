package de.bund.bva.isyfact.common.web.global;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Helfer-Klasse um den Zugriff auf die Konfiguration zu kapseln.
 */
public class IsyWebKonfigurationHelper {
    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    /**
     * Liefert den konfigurierten Grenzwert zum ergänzen von zweistelligen Jahreszahlen bei Datumsangaben. Ist
     * der Wert überhaupt nicht gesetzt, wird -1 zurückgegeben.
     * @return Der konfigurierte Wert oder -1 wenn dieser nicht gesetzt ist.
     */
    public int getGuiDatumsangabeJahreszahlenErgaenzenGrenze() {
        return this.konfiguration
            .getAsInteger(KonfigurationSchluessel.GUI_DATUMSANGABE_JAHRESZAHLEN_ERGAENZEN_GRENZE, -1);
    }

    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
