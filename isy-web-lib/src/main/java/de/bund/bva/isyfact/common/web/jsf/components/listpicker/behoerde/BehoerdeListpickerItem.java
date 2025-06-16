package de.bund.bva.isyfact.common.web.jsf.components.listpicker.behoerde;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerItem;

/**
 * Ein Behördenkennzeichen als ListpickerItem.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class BehoerdeListpickerItem implements ListpickerItem {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Das eigentliche Behördenkennzeichen.
     */
    private String kennzeichen;

    /**
     * Der lesbare Name.
     */
    private String name;

    /**
     * Eine anwendungsspezifische CSS-Klasse.
     */
    private String cssClass;

    /**
     * Konstruktor.
     *
     * @param kennzeichen
     *            Das Kennzeichen der Behörde.
     * @param name
     *            Der Name der Behörde.
     */
    public BehoerdeListpickerItem(String kennzeichen, String name) {
        super();
        this.kennzeichen = kennzeichen;
        this.name = name;
    }

    public String getKennzeichen() {
        return this.kennzeichen;
    }

    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReadableValueForItem() {
        return this.name;
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
        return this.kennzeichen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getListpickerValueForItem() {
        // TODO Auto-generated method stub
        return null;
    }

}
