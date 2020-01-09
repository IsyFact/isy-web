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
package de.bund.bva.isyfact.common.web.global;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.exception.web.ErrorController;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.controller.NavigationMenuController;
import de.bund.bva.isyfact.common.web.validation.ValidationController;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;

/**
 * Ein globaler Controller der Zustände für einen konkreten Flow kontrolliert und Querschnittsfunktionalität
 * anbietet.
 *
 * @author Capgemini, Andreas Hörning.
 * @version $Id: GlobalFlowController.java 128983 2015-01-27 16:42:36Z sdm_tgroeger $
 */
public class GlobalFlowController extends AbstractGuiController<GlobalFlowModel> {

    /**
     * Der Zugriff auf den Nachrichten Controller.
     */
    private MessageController messageController;

    /**
     * Der Zugriff auf den Validation Controller.
     */
    private ValidationController validationController;

    /**
     * Der Zugriff auf den Error Controller.
     */
    private ErrorController errorController;

    /**
     * Zugriff auf den AufrufKontext.
     */
    private AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter;

    /**
     * Zugriff auf navigationMenuController
     */
    private NavigationMenuController navigationMenuController;

    @Override
    public void initialisiereModel(GlobalFlowModel model) {

        // Fehlertext bestimmen
        model.setAjaxErrorMessage(this.errorController.getAjaxErrorMessage());
        model.setAjaxErrorMessageTitle(this.errorController.getAjaxErrorMessageTitle());

        this.navigationMenuController.initialisiereNavigationMenue();
        // Resource-Bundles initialisieren
        initialisiereResourcesBundleCurrentFlow(model);

        // Benutzernamen merken
        model.setSachbearbeiterName(
            this.aufrufKontextVerwalter.getAufrufKontext().getDurchfuehrenderSachbearbeiterName());

        // Beim Laden soll ein bestimmtes Element des Inhaltsbereichs fokussiert werden
        model.setFocusOnloadActive(true);
    }

    /**
     * Initialisiert das ResourcesBundle zum aktuellen Flow.
     * @param model
     *            das GlobalFlowModel
     */
    private void initialisiereResourcesBundleCurrentFlow(GlobalFlowModel model) {

        FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();
        String flowName = flowDefinition.getId();

        // Laden der Maskentexte für den aktuellen Flow
        ResourceBundle resourcesBundleCurrentFlow = null;
        try {
            resourcesBundleCurrentFlow = ResourceBundle
                .getBundle("resources.nachrichten.maskentexte." + flowName, Locale.getDefault());
        } catch (MissingResourceException e) {
            // Wenn keine Ressource gefunden wurde ist das in Ordnung.
        }

        // Setzen des Pfads wenn die Maskentexte existieren
        if (resourcesBundleCurrentFlow != null) {
            model.setPathToResourcesBundleCurrentFlow("resources.nachrichten.maskentexte." + flowName);
        } else {
            model.setPathToResourcesBundleCurrentFlow(null);
        }
    }

    public MessageController getMessageController() {
        return this.messageController;
    }

    public ValidationController getValidationController() {
        return this.validationController;
    }

    @Required
    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    @Required
    public void setValidationController(ValidationController validationController) {
        this.validationController = validationController;
    }

    @Required
    public void setErrorController(ErrorController errorController) {
        this.errorController = errorController;
    }

    @Required
    public void setAufrufKontextVerwalter(AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter) {
        this.aufrufKontextVerwalter = aufrufKontextVerwalter;
    }

    @Required
    public void setNavigationMenuController(NavigationMenuController navigationMenuController) {
        this.navigationMenuController = navigationMenuController;
    }

    @Override
    protected Class<GlobalFlowModel> getMaskenModelKlasseZuController() {
        return GlobalFlowModel.class;
    }
}
