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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * Das Validation-Model hält die Validerungsnachrichten für eine Maske.
 *
 * @author Capgemini
 * @version $Id: ValidationModel.java 124952 2014-11-12 13:31:30Z sdm_ahoerning $
 */
public class ValidationModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Enthält die Validierungsfehler für die aktuelle Maske.
     */
    private List<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();

    /**
     * Enthält die Überschrift der Validierungsfehler-Nachrichten.
     */
    private FacesMessage globalValidationFacesMessage;

    /**
     * Enthält die Validierungsfehler, welche als Faces Messages dargestellt werden können.
     */
    private List<FacesMessage> validationFacesMessages = new ArrayList<FacesMessage>();

    public List<ValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }

    public void setValidationMessages(List<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public List<FacesMessage> getValidationFacesMessages() {
        return this.validationFacesMessages;
    }

    public void setValidationFacesMessages(List<FacesMessage> validationFacesMessages) {
        this.validationFacesMessages = validationFacesMessages;
    }

    public FacesMessage getGlobalValidationFacesMessage() {
        return this.globalValidationFacesMessage;
    }

    public void setGlobalValidationFacesMessage(FacesMessage globalValidationFacesMessage) {
        this.globalValidationFacesMessage = globalValidationFacesMessage;
    }

    /**
     * Liest die aktuellen Validierungsnachrichten zu einer gegebenen Referenz.
     * @param reference
     *            Die Referenz.
     * @return Die zugehörigen Validierungsnachrichten.
     */
    public List<ValidationMessage> getValidationMessagesForReference(String reference) {
        List<ValidationMessage> filteredValidationMessages = new ArrayList<ValidationMessage>();

        if (this.validationMessages == null) {
            return filteredValidationMessages;
        }

        for (ValidationMessage validationMessage : this.validationMessages) {

            if (Strings.isNullOrEmpty(validationMessage.getReference())) {
                continue;
            }

            if (validationMessage.getReference().equals(reference)) {
                filteredValidationMessages.add(validationMessage);
            }
        }

        return filteredValidationMessages;
    }

    /**
     * Liest die aktuellen Validierungsnachrichten ohne eine Referenz aus.
     *
     * @return Die Validierungsnachrichten ohne Referenz.
     */
    public List<FacesMessage> getValidationMessagesWithoutReference() {
        List<FacesMessage> filteredValidationFacesMessages = new ArrayList<FacesMessage>();

        if (this.validationMessages == null) {
            return filteredValidationFacesMessages;
        }

        for (FacesMessage validationFacesMessage : this.validationFacesMessages) {

            if (Strings.isNullOrEmpty(validationFacesMessage.getSummary())) {
                filteredValidationFacesMessages.add(validationFacesMessage);
            }
        }

        return filteredValidationFacesMessages;
    }

    /**
     * Gibt eine zusammengefügte Validierungsnachricht als String aus allen vorhanden Validierungsnachrichten
     * für die angegebene Referenz zurück.
     * @param reference
     *            Die Referenz.
     * @return Die Validierungsnachricht.
     */
    public String getCombinedValidationMessageForReference(String reference) {

        List<String> messages = new ArrayList<String>();

        for (ValidationMessage validationMessage : getValidationMessagesForReference(reference)) {
            String message = validationMessage.getMessage() + " (" + validationMessage.getCode() + ")";
            if (validationMessage.isShowReadableReferenceValidationMessageForReference()) {
                message = validationMessage.getReadableReference() + " " + message;
            }

            messages.add(message);
        }

        return Joiner.on(", ").join(messages);

    }

}
