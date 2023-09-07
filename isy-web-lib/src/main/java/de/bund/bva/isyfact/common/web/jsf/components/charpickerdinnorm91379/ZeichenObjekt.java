package de.bund.bva.isyfact.common.web.jsf.components.charpickerdinnorm91379;

import java.io.Serializable;

/**
 * An object containing a character and the attributes needed to be represented in the DIN NORM 91379
 * character picker.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ZeichenObjekt implements Serializable {


    private static final long serialVersionUID = -3213045454719019600L;

    /** A character that is part of the DIN SPEC 91379. */
    private String zeichen;

    /** The base character of the character. */
    private String grundzeichen;

    /** The group of the character. */
    private Schriftzeichengruppe schriftzeichengruppe;

    /** The written out name of the character. */
    private String name;

    /** Unicode codepoint of the character. */
    private String codepoint;

    public ZeichenObjekt(String zeichen, String grundzeichen,
                         Schriftzeichengruppe schriftzeichengruppe, String name, String codepoint) {
        this.zeichen = zeichen;
        this.grundzeichen = grundzeichen;
        this.schriftzeichengruppe = schriftzeichengruppe;
        this.name = name;
        this.codepoint = codepoint;
    }

    public String getZeichen() {
        return zeichen;
    }

    public void setZeichen(String zeichen) {
        this.zeichen = zeichen;
    }

    public String getGrundzeichen() {
        return grundzeichen;
    }

    public void setGrundzeichen(String grundzeichen) {
        this.grundzeichen = grundzeichen;
    }

    public Schriftzeichengruppe getSchriftzeichengruppe() {
        return schriftzeichengruppe;
    }

    public void setSchriftzeichengruppe(
            Schriftzeichengruppe schriftzeichengruppe) {
        this.schriftzeichengruppe = schriftzeichengruppe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public void setCodepoint(String codepoint) {
        this.codepoint = codepoint;
    }
}
