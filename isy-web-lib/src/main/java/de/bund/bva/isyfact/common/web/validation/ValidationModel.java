package de.bund.bva.isyfact.common.web.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * The validation model stores the validation messages for a mask.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ValidationModel implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Contains the validation errors for the current mask.
     */
    private List<ValidationMessage> validationMessages = new ArrayList<>();

    /**
     * Contains the headline of validation error messages.
     */
    private FacesMessage globalValidationFacesMessage;

    /**
     * Contains the validation errors, which can be displayed as Faces Messages.
     */
    private List<FacesMessage> validationFacesMessages = new ArrayList<>();

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
     * Reads the current validation messages for a given reference.
     * @param reference
     *            the reference.
     * @return The associated validation messages.
     */
    public List<ValidationMessage> getValidationMessagesForReference(String reference) {
        List<ValidationMessage> filteredValidationMessages = new ArrayList<>();

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
     * Reads the current validation messages without a reference.
     *
     * @return The validation messages without a reference.
     */
    public List<FacesMessage> getValidationMessagesWithoutReference() {
        List<FacesMessage> filteredValidationFacesMessages = new ArrayList<>();

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
     * Returns a concatenated validation message as a string from all existing validation messages
     * for the given reference.
     * @param reference
     *            the reference.
     * @return The validation message.
     */
    public String getCombinedValidationMessageForReference(String reference) {

        List<String> messages = new ArrayList<>();

        for (ValidationMessage validationMessage : getValidationMessagesForReference(reference)) {
            String message = validationMessage.getMessage();
            if (validationMessage.isShowReadableReferenceValidationMessageForReference()) {
                message = validationMessage.getReadableReference() + " " + message;
            }

            messages.add(message);
        }

        return Joiner.on(", ").join(messages);

    }

}
