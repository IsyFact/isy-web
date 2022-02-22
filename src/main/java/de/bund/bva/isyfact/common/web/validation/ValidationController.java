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

import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.global.NachrichtenProvider;
import de.bund.bva.isyfact.common.web.konstanten.MaskentexteSchluessel;

/**
 * A controller that takes care of validation messages.
 *
 */
public class ValidationController {

    /**
     * The view scope variable for the validation model.
     */
    private static final String FLASH_SCOPE_VALIDATION_MODEL_VARIABLE = "validationModel";

    /**
     * Processes the passed validation messages and writes them to the Flash scope.
     * 
     * @param validationMessages
     *            the validation messages
     */
    public void processValidationMessages(List<ValidationMessage> validationMessages) {

        ValidationModel validationModel = null;

        // If a validation model already exists, it will be updated, otherwise a new one will be created.
        validationModel =
            (ValidationModel) RequestContextHolder.getRequestContext().getFlashScope()
                .get(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE);

        if (validationModel == null) {
            validationModel = new ValidationModel();
        }

        // Add validation errors
        if (validationModel.getValidationMessages() == null) {
            validationModel.setValidationMessages(new ArrayList<>());
        }
        if (!validationMessages.isEmpty()) {
            validationModel.getValidationMessages().addAll(validationMessages);
        }

        // Write parent Faces Message
        if (!validationMessages.isEmpty() && validationModel.getGlobalValidationFacesMessage() == null) {
            validationModel.setGlobalValidationFacesMessage(new FacesMessage(FacesMessage.SEVERITY_WARN,
                new NachrichtenProvider().getMessage(MaskentexteSchluessel.MEL_UNZULAESSIGE_DATEN_VORHANDEN),
                new NachrichtenProvider().getMessage(MaskentexteSchluessel.MEL_GEKENNZEICHNETE_FELDER_PRUEFEN)));
        }

        // Add validation errors as Faces messages
        for (ValidationMessage validationMessage : validationMessages) {
            validationModel.getValidationFacesMessages().add(
                new FacesMessage(FacesMessage.SEVERITY_WARN, validationMessage.getReadableReference(),
                    validationMessage.getMessage()));
        }

        if (!validationMessages.isEmpty()) {

            // Write to View Scope
            RequestContextHolder.getRequestContext().getFlashScope()
                .put(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE, validationModel);
        }

    }
}
