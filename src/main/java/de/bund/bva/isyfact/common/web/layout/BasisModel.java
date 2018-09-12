/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Basis-Seite".
 *
 * @author Capgemini, Jonas Zitz
 * @version $Id: BasisModel.java 144984 2015-08-18 13:53:33Z sdm_bpiatkowski $
 */
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
