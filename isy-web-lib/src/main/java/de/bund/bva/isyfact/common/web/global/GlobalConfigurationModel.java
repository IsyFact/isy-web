package de.bund.bva.isyfact.common.web.global;

import java.io.Serializable;

/**
 * Das globale Model für Einstellungen der Applikationen. Enthält Informationen über die Aktivierung von
 * JavaScript.
 *
 * Anzeigespezifische/Flowspezifische Einstellungen befinden sich nicht hier sondern im
 * {@link GlobalFlowModel}.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class GlobalConfigurationModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der Aktivierungsstatus von Javascript.
     */
    private boolean jsEnabled;

    public boolean isJsEnabled() {
        return this.jsEnabled;
    }

    public void setJsEnabled(boolean jsEnabled) {
        this.jsEnabled = jsEnabled;
    }
}
