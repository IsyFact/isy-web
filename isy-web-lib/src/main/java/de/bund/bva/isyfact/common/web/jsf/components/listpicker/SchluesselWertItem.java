package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

/**
 * Item der beinhaltet Schlüssel und Wert.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class SchluesselWertItem implements ListpickerItem {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Attribut: Schlüssel.
     */
    private String schluessel;

    /**
     * Attribut: Wert.
     */
    private String wert;

    /**
     * Eine anwendungsspezifische CSS-Klasse.
     */
    private String cssClass;

    /**
     * Erzeugt einen neuen Schluessel.
     *
     * @param schluessel
     *            Der Schlüssel.
     * @param wert
     *            Der Schlüsselwert.
     */
    public SchluesselWertItem(String schluessel, String wert) {
        super();
        this.schluessel = schluessel;
        this.wert = wert;
    }

    public String getSchluessel() {
        return this.schluessel;
    }

    public String getWert() {
        return this.wert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReadableValueForItem() {
        return getWert();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssClass() {
        return this.cssClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueForItem() {
        return getSchluessel();
    }

}
