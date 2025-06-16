package de.bund.bva.isyfact.common.web.exception.web;

import java.io.Serializable;

/**
 * Model für den Error-View.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ErrorModel implements Serializable {

    /**
     * Die Serial-Version-UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der Fehlertext.
     */
    private String fehlerText;

    /**
     * Die Fehlerüberschrift.
     */
    private String fehlerUeberschrift;

    public String getFehlerText() {
        return fehlerText;
    }

    public void setFehlerText(String fehlerText) {
        this.fehlerText = fehlerText;
    }

    public String getFehlerUeberschrift() {
        return fehlerUeberschrift;
    }

    public void setFehlerUeberschrift(String fehlerUeberschrift) {
        this.fehlerUeberschrift = fehlerUeberschrift;
    }

}
