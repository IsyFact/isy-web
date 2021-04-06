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
package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

/**
 * Der Inhalt eines Sonderzeichen-Mappings.
 *
 */
public class SpecialCharPickerMapping {

    /** Der beschreibende Text zu einem Sonderzeichen . */
    private String titel;

    /** Das Basiszeichen eines Sonderzeichens. */
    private char basiszeichen;

    /**
     * Konstruktor.
     *
     * @param basiszeichen
     *            das Basiszeichen des Sonderzeichens
     * @param titel
     *            der Titel des Sonderzeichens.
     */
    public SpecialCharPickerMapping(char basiszeichen, String titel) {
        this.basiszeichen = basiszeichen;
        this.titel = titel;
    }

    /**
     * Gibt den Titel (erläuternder Text) des Sonderzeichens. zurück.
     *
     * @return den Titel, oder null.
     */
    public String getTitel() {
        return this.titel;
    }

    /**
     * Gibt das Basiszeichen des Sonderzeichens zurück.
     *
     * @return das Basiszeichen.
     */
    public char getBasiszeichen() {
        return this.basiszeichen;
    }
}
