package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Model zur Steuerung der Sichtbarkeit des Hilfe-Buttons.
 *
 * @author msg
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class HilfeModel extends AbstractMaskenModel {

    private static final long serialVersionUID = 1L;
    /**
     * Gibt an, ob der Hilfe-Button angezeigt wird.
     */
    private boolean hilfeButtonAvailable;

    @SuppressWarnings("javadoc")
    public boolean isHilfeButtonAvailable() {

        return hilfeButtonAvailable;
    }

    @SuppressWarnings("javadoc")
    public void setHilfeButtonAvailable(boolean hilfeButtonAvailable) {

        this.hilfeButtonAvailable = hilfeButtonAvailable;
    }
}