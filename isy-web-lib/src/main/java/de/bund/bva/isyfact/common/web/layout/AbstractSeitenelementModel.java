package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;

/**
 * Abstrakte Oberklasse für IsyFact-Seitenelemente.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class AbstractSeitenelementModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Gibt an, ob das Seitenelement angezeigt werden soll.
     */
    protected boolean anzeigen;

    public boolean isAnzeigen() {
        return this.anzeigen;
    }

    public void setAnzeigen(boolean anzeigen) {
        this.anzeigen = anzeigen;
    }
}
