package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Basis-Seite".
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class BasisModel extends AbstractMaskenModel {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Legt fest ob ein modaler Dialog angezeigt werden soll.
     */
    private boolean showModalDialog;

    /**
     * Legt fest ob die Druckansicht angezeigt werden soll.
     */
    private boolean showPrintView;

    /**
     * Enthält das Model für den Informationsbereich.
     */
    private InformationsbereichModel informationsbereichModel;

    /**
     * Enthält den Zugriff auf die Seitentoolbar.
     */
    private SeitentoolbarModel seitentoolbarModel;

    /**
     * Enthält den Zugriff auf die vertikale Toolbar.
     */
    private VertikalToolbarModel vertikalToolbarModel;

    /**
     * Enthält den Zugriff auf den Location-Breadcrumb.
     */
    private LocationBreadcrumbModel locationBreadcrumbModel;

    /**
     * Enthält Name von modaler Dialog, der angezeigt werden soll.
     */
    private String modalDialogName;

    public InformationsbereichModel getInformationsbereichModel() {
        return this.informationsbereichModel;
    }

    public void setInformationsbereichModel(InformationsbereichModel informationsbereichModel) {
        this.informationsbereichModel = informationsbereichModel;
    }

    public SeitentoolbarModel getSeitentoolbarModel() {
        return this.seitentoolbarModel;
    }

    public void setSeitentoolbarModel(SeitentoolbarModel seitentoolbarModel) {
        this.seitentoolbarModel = seitentoolbarModel;
    }

    public VertikalToolbarModel getVertikalToolbarModel() {
        return this.vertikalToolbarModel;
    }

    public void setVertikalToolbarModel(VertikalToolbarModel vertikalToolbarModel) {
        this.vertikalToolbarModel = vertikalToolbarModel;
    }

    public LocationBreadcrumbModel getLocationBreadcrumbModel() {
        return locationBreadcrumbModel;
    }

    public void setLocationBreadcrumbModel(LocationBreadcrumbModel locationBreadcrumbModel) {
        this.locationBreadcrumbModel = locationBreadcrumbModel;
    }

    public boolean isShowModalDialog() {
        return this.showModalDialog;
    }

    public void setShowModalDialog(boolean showModalDialog) {
        this.showModalDialog = showModalDialog;
    }

    public boolean isShowPrintView() {
        return this.showPrintView;
    }

    public void setShowPrintView(boolean showPrintView) {
        this.showPrintView = showPrintView;
    }

    public String getModalDialogName() {
        return this.modalDialogName;
    }

    public void setModalDialogName(String modalDialogName) {
        this.modalDialogName = modalDialogName;
    }
}
