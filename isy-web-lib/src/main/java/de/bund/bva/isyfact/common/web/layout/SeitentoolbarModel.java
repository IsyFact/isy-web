package de.bund.bva.isyfact.common.web.layout;

/**
 * Ist das Model für die Toolbar.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SeitentoolbarModel extends AbstractSeitenelementModel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ob der Zurück-Button angezeigt werden soll oder nicht.
     */
    private boolean zeigeZurueckButton;
    
    /**
     * Bestimmt, ob die mittlere Buttongruppe in der Seitentoolbar angezeigt wird.
     * Wenn nicht, nimmt die rechte Buttongruppe den freigewordenen Platz ein.   
     */
    private boolean zeigeMittlereButtongruppe = true;

    public boolean isZeigeZurueckButton() {
        return this.zeigeZurueckButton;
    }

    public void setZeigeZurueckButton(boolean zeigeZurueckButton) {
        this.zeigeZurueckButton = zeigeZurueckButton;
    }
    
    public boolean isZeigeMittlereButtongruppe() {
        return this.zeigeMittlereButtongruppe;
    }
    
    public void setZeigeMittlereButtongruppe(boolean zeigeMittlereButtongruppe) {
        this.zeigeMittlereButtongruppe = zeigeMittlereButtongruppe;
    }

}
