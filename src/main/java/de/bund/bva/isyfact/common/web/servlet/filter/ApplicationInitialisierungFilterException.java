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
package de.bund.bva.isyfact.common.web.servlet.filter;

import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Wird fuer Laufzeitfehler bei der Application-Initialisierung genutzt.
 * @author Capgemini
 * @version $Id: ApplicationInitialisierungFilterException.java 130854 2015-02-18 10:42:27Z sdm_mhartung $
 */
public class ApplicationInitialisierungFilterException extends TechnicalRuntimeException {

    /**
     * Id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Erstellt eine neue Exception.
     * 
     * @param ausnahmeId
     *            ist die AusnahmeId.
     * @param parameter
     *            sind die Parameter zur Ersetzung im Fehlertext
     */
    public ApplicationInitialisierungFilterException(String ausnahmeId, String... parameter) {
        super(ausnahmeId, new FehlertextProviderImpl(), parameter);
    }
}
