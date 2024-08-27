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
package de.bund.bva.isyfact.common.web.exception;

import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Technische Fehleroberklasse für Laufzeit-Fehler.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class IsyFactTechnicalRuntimeException extends TechnicalRuntimeException {

    /**
     * Id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Kontruktor.
     * @param ausnahmeId
     *            die Ausnahme-Id
     * @param throwable
     *            der Grund
     * @param parameter
     *            die Parameter
     */
    public IsyFactTechnicalRuntimeException(String ausnahmeId, Throwable throwable, String... parameter) {
        super(ausnahmeId, throwable, new IsyFactFehlertextProvider(), parameter);
    }

    /**
     * Konstruktor.
     * @param ausnahmeId
     *            die Ausnahme-Id
     * @param parameter
     *            die Parameter
     */
    public IsyFactTechnicalRuntimeException(String ausnahmeId, String... parameter) {
        super(ausnahmeId, new IsyFactFehlertextProvider(), parameter);
    }

}
