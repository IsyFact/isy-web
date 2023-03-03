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

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.global.AbstractGuiController;
import de.bund.bva.isyfact.common.web.global.GlobalConfigurationModel;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;

/**
 * Ist der Controller fuer das Template "Basis".
 *
 * @author Capgemini
 * @version $Id: BasisController.java 144984 2015-08-18 13:53:33Z sdm_bpiatkowski $
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BasisController extends AbstractGuiController<BasisModel> {

    private static final String DEFAULT_MODAL_DIALOG_NAME = "modalDialog";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialisiereModel(BasisModel model) {

        // Informationsbereich initialisieren
        initialisiereInformationsbereich(model);

        // Seitentoolbar initialisieren
        initialisiereSeitentoolbar(model);

        // Vertikale Toolbar initialisieren
        initialisiereVertikalToolbar(model);

        model.setLocationBreadcrumbModel(new LocationBreadcrumbModel());
    }

    /**
     * Invertiert den Zustand des Informationsbereich (Ein-/Ausklappen).
     *
     * @param model
     *            ist ein Model vom Typ {@link BasisModel}.
     */
    public void toggleInformationsbereich(BasisModel model) {

        boolean informationsbereichAnzeigen = model.getInformationsbereichModel().isAnzeigen();
        boolean informationsbereichAnzeigenNeu = !informationsbereichAnzeigen;

        model.getInformationsbereichModel().setAnzeigen(informationsbereichAnzeigenNeu);
    }

    /**
     * Aktiviert die Anzeige für den modalen Dialog im aktuellen Flow-Context.
     */
    public void showModalDialog() {
        getMaskenModelZuController().setShowModalDialog(true);
        getMaskenModelZuController().setModalDialogName(DEFAULT_MODAL_DIALOG_NAME);
    }

    /**
     * Aktiviert die Anzeige für den modalen Dialog im aktuellen Flow-Context.
     *
     * @param modalDialogName
     *            Name von modaler Dialog, der angezeigt werden soll.
     */
    public void showModalDialog(String modalDialogName) {
        getMaskenModelZuController().setShowModalDialog(true);
        getMaskenModelZuController().setModalDialogName(modalDialogName);
    }

    /**
     * Deaktiviert die Anzeige für den modalen Dialog im aktuellen Flow-Context.
     */
    public void hideModalDialog() {
        getMaskenModelZuController().setShowModalDialog(false);
    }

    /**
     * Aktiviert die Anzeige für die Druckansicht im aktuellen Flow. Dabei wird das
     * {@link GlobalConfigurationModel} überschrieben, indem es in den Flow-Scope geschrieben wird.
     *
     */
    public void showPrintView() {
        getMaskenModelZuController().setShowPrintView(true);

        MutableAttributeMap<Object> conversationScope =
            RequestContextHolder.getRequestContext().getConversationScope();

        MutableAttributeMap<Object> flowScope = RequestContextHolder.getRequestContext().getFlowScope();

        GlobalConfigurationModel globalConfigurationModel =
            (GlobalConfigurationModel) conversationScope.get("globalConfigurationModel");

        GlobalConfigurationModel flowScopedGlobalConfigurationModel = null;

        try {
            flowScopedGlobalConfigurationModel =
                (GlobalConfigurationModel) BeanUtils.cloneBean(globalConfigurationModel);
        } catch (Throwable t) {
            throw new IsyFactTechnicalRuntimeException(
                FehlerSchluessel.ALLGEMEINER_TECHNISCHER_FEHLER_MIT_PARAMETER, t,
                "Fehler beim Kopieren des GlobalConfigurationModel");
        }

        flowScope.put("globalConfigurationModel", flowScopedGlobalConfigurationModel);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<BasisModel> getMaskenModelKlasseZuController() {
        return BasisModel.class;
    }

    /**
     * Initialisiert die Seitentoolbar.
     *
     * @param model
     *            Das Model.
     */
    private void initialisiereSeitentoolbar(BasisModel model) {
        // Model initialisieren
        SeitentoolbarModel seitentoolbarModel = model.getSeitentoolbarModel();
        if (seitentoolbarModel == null) {
            seitentoolbarModel = new SeitentoolbarModel();
            model.setSeitentoolbarModel(seitentoolbarModel);
        }

        model.getSeitentoolbarModel().setAnzeigen(false);
    }

    /**
     * Initialisiert die vertikale Toolbar.
     *
     * @param model
     *            Das Model.
     */
    private void initialisiereVertikalToolbar(BasisModel model) {
        // Model initialisieren
        VertikalToolbarModel vertikalToolbarModel = model.getVertikalToolbarModel();
        if (vertikalToolbarModel == null) {
            vertikalToolbarModel = new VertikalToolbarModel();
            model.setVertikalToolbarModel(vertikalToolbarModel);
        }

        model.getVertikalToolbarModel().setAnzeigen(true);
    }

    /**
     * Initialisiert den Informationsbreich.
     *
     * @param model
     *            ist das Model
     */
    private void initialisiereInformationsbereich(BasisModel model) {
        // Model initialisieren
        InformationsbereichModel informationsbereichModel = model.getInformationsbereichModel();
        if (informationsbereichModel == null) {
            informationsbereichModel = new InformationsbereichModel();
            model.setInformationsbereichModel(informationsbereichModel);
        }

        // Definiere Standardwerte
        model.getInformationsbereichModel().setAnzeigen(false);
        model.getInformationsbereichModel().setIconTyp(IconTyp.INFO);
    }
}
