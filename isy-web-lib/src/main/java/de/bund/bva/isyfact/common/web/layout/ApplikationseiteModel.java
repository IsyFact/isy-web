package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Applikationseite".
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ApplikationseiteModel extends AbstractMaskenModel {

    // TODO Erweitern um gruppe aktiver quicklinks, bei leer default alle anzeigen

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Das LinksnavigationModel zur Applikationseite.
     */
    private LinksnavigationModel linksnavigationModel;

    public LinksnavigationModel getLinksnavigationModel() {
        return this.linksnavigationModel;
    }

    public void setLinksnavigationModel(LinksnavigationModel linksnavigationModel) {
        this.linksnavigationModel = linksnavigationModel;
    }
}
