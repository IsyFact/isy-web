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

/**
 * Eine ValidationMessage ist eine Nachricht, welche einen Validierungsfehler beinhaltet und beschreibt.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ValidationMessage implements Serializable {

    /**
     * Die Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der (Fehler)Code der Nachricht.
     */
    private String code;

    /**
     * Die Referenz auf den Fehler.
     */
    private String reference;

    /**
     * Die lesbare Darstellung der Referenz.
     */
    private String readableReference;

    /**
     * Die Fehlernachricht.
     */
    private String message;

    /**
     * Ob die lesbare Referenz als Präfix für die Nachrichten für die Darstellung an einem Feld gesetzt werden
     * soll. Dies ist vor allem bei sprechenden Fehlermeldungen notwendig.
     *
     * Beispiel: readableReference = Feld Nummer message = muss größer als 0 sein
     *
     * Mit showReadableReferenceValidationMessageForReference = true wird die Meldung 'Feld Nummer muss größer
     * als 0 sein' zurückgegeben. Mit showReadableReferenceValidationMessageForReference = false wird die
     * Meldung 'muss größer als 0 sein' zurückgegeben.
     */
    private boolean showReadableReferenceValidationMessageForReference;

    /**
     * Konstruktor für eine ValidationMessage.
     * @param code
     *            Der Fehlercode.
     * @param reference
     *            Die Fehlerreferenz.
     * @param readableReference
     *            Die Fehlerreferenz in lesbarer Form.
     * @param message
     *            Die Nachricht.
     */
    public ValidationMessage(String code, String reference, String readableReference, String message) {
        super();
        this.code = code;
        this.reference = reference;
        this.readableReference = readableReference;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getReference() {
        return this.reference;
    }

    public String getMessage() {
        return this.message;
    }

    public String getReadableReference() {
        return this.readableReference;
    }

    public void setReadableReference(String readableReference) {
        this.readableReference = readableReference;
    }

    public boolean isShowReadableReferenceValidationMessageForReference() {
        return this.showReadableReferenceValidationMessageForReference;
    }

    public void setShowReadableReferenceValidationMessageForReference(
        boolean showReadableReferenceValidationMessageForReference) {
        this.showReadableReferenceValidationMessageForReference =
            showReadableReferenceValidationMessageForReference;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
