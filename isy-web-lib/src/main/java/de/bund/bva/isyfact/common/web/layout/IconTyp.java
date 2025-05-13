package de.bund.bva.isyfact.common.web.layout;

/**
 * Kapselt Typen von Icons.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public enum IconTyp {

    /** Info. */
    INFO("info");

    /** Der Suffix der CSS-Icon-Klasse. */
    private String cssSuffix;

    /**
     * Konstruktor.
     * 
     * @param cssSuffix
     *            Der Suffix der CSS-Icon-Klasse.
     */
    private IconTyp(String cssSuffix) {
        this.cssSuffix = cssSuffix;
    }

    public String getCssSuffix() {
        return cssSuffix;
    }

}
