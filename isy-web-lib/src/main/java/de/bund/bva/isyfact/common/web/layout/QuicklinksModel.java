package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Model für Quicklinks.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class QuicklinksModel implements Serializable {

    /**
     * Die Serial-Version-UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die Quicklinks. Gruppe ID -> Gruppe Model.
     */
    private Map<String, QuicklinksGruppeModel> quicklinksGruppen = new TreeMap<>();

    public Map<String, QuicklinksGruppeModel> getQuicklinksGruppen() {
        return this.quicklinksGruppen;
    }

    public void setQuicklinksGruppen(Map<String, QuicklinksGruppeModel> quicklinksGruppen) {
        this.quicklinksGruppen = quicklinksGruppen;
    }

}
