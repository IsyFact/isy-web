package de.bund.bva.isyfact.common.web.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import de.bund.bva.isyfact.common.web.konstanten.KonfigurationSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Helfer-Klasse um den Zugriff auf die Konfiguration zu kapseln.
 */
@Component
@ConditionalOnBean(Konfiguration.class)
public class IsyWebKonfigurationHelper {
    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    @Autowired
    public IsyWebKonfigurationHelper(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    /**
     * Liefert den konfigurierten Grenzwert zum ergänzen von zweistelligen Jahreszahlen bei Datumsangaben. Ist
     * der Wert überhaupt nicht gesetzt, wird -1 zurückgegeben.
     * @return Der konfigurierte Wert oder -1 wenn dieser nicht gesetzt ist.
     */
    public int getGuiDatumsangabeJahreszahlenErgaenzenGrenze() {
        return this.konfiguration
            .getAsInteger(KonfigurationSchluessel.GUI_DATUMSANGABE_JAHRESZAHLEN_ERGAENZEN_GRENZE, -1);
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
