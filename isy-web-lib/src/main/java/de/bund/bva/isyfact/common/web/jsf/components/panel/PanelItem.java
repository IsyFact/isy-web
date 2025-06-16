package de.bund.bva.isyfact.common.web.jsf.components.panel;

/**
 * Ein Interface für Datenklassen. Dieses kann benutzt werden um sicherzustellen, dass alle benötigten Panel
 * Models auch für dynamisch Bedingte Daten (z.B. ein Panel pro Personalie) zur Verfügung steht.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface PanelItem {

    /**
     * Setzt das Panel Model.
     * @param panelModel
     *            Das Panel Model.
     */
    public void setPanelModel(PanelModel panelModel);

    /**
     * Holt das Panel Model.
     * @return Das Panel Model.
     */
    public PanelModel getPanelModel();

}
