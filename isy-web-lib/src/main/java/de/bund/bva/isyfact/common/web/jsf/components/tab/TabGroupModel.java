package de.bund.bva.isyfact.common.web.jsf.components.tab;

import java.io.Serializable;

/**
 * Das TAB Model für die Nutzung des rf:tabGroupt und rf:tab Tags. Jedes GUI-Model sollte pro Tab ein
 * entsprechendes Model bereitstellen. Es dient zur Speichern des Status und zur Konfiguration des
 * Initialzustands.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class TabGroupModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Welches Tab aktuell ausgewählt ist.
     */
    private String currentTab;

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

}
