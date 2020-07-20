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
package de.bund.bva.isyfact.common.web.validation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.global.NachrichtenProvider;
import de.bund.bva.isyfact.common.web.konstanten.MaskentexteSchluessel;

/**
 * Ein Controller, welcher sich um Validierungsnachrichten kümmert.
 * 
 * @author Capgemini, Andreas Hörning.
 * @version $Id: ValidationController.java 130854 2015-02-18 10:42:27Z sdm_mhartung $
 */
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ValidationController {

    /**
     * Die View-Scope Variable für das Validierungsmodel.
     */
    private static final String FLASH_SCOPE_VALIDATION_MODEL_VARIABLE = "validationModel";

    /**
     * Verarbeitet die übergebenen Validierungsnachrichten und schreibt diese in den Flash-Scope.
     * 
     * @param validationMessages
     *            Die Validierungsnachrichten
     */
    public void processValidationMessages(List<ValidationMessage> validationMessages) {

        ValidationModel validationModel = null;

        // Falls bereits ein Validation Model existiert, wird dieses aktualisiert, ansonsten wird ein neues
        // erzeugt.
        validationModel =
            (ValidationModel) RequestContextHolder.getRequestContext().getFlashScope()
                .get(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE);

        if (validationModel == null) {
            validationModel = new ValidationModel();
        }

        // Validierungsfehler hinzufügen
        if (validationModel.getValidationMessages() == null) {
            validationModel.setValidationMessages(new ArrayList<ValidationMessage>());
        }
        if (!validationMessages.isEmpty()) {
            validationModel.getValidationMessages().addAll(validationMessages);
        }

        // Übergeordnete Faces Message schreiben
        if (!validationMessages.isEmpty() && validationModel.getGlobalValidationFacesMessage() == null) {
            validationModel.setGlobalValidationFacesMessage(new FacesMessage(FacesMessage.SEVERITY_WARN,
                new NachrichtenProvider().getMessage(MaskentexteSchluessel.MEL_UNZULAESSIGE_DATEN_VORHANDEN),
                new NachrichtenProvider().getMessage(MaskentexteSchluessel.MEL_GEKENNZEICHNETE_FELDER_PRUEFEN)));
        }

        // Validierungsfehler als Faces Messages festhalten
        for (ValidationMessage validationMessage : validationMessages) {
            validationModel.getValidationFacesMessages().add(
                new FacesMessage(FacesMessage.SEVERITY_WARN, validationMessage.getReadableReference(),
                    validationMessage.getMessage() + " (" + validationMessage.getCode() + ")"));
        }

        if (!validationMessages.isEmpty()) {

            // In View Scope schreiben
            RequestContextHolder.getRequestContext().getFlashScope()
                .put(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE, validationModel);
        }

    }
}
