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

import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Eine Exception, die geworfen wird, wenn ein Benutzer keine Berechtigung für einen Zugriff hat.
 * 
 * @author Capgemini
 * @version $Id: ZugriffBerechtigungException.java 130854 2015-02-18 10:42:27Z sdm_mhartung $
 */
public class ZugriffBerechtigungException extends TechnicalRuntimeException {

    /**
     * Generierte SVUID.
     */
    private static final long serialVersionUID = 7697886827764442124L;

    /** Fehlertextprovider zum Auslesen von Fehlertexten. */
    private static final FehlertextProvider FEHLERTEXT_PROVIDER = new FehlertextProviderImpl();

    /**
     * Konstruktor für eine {@link ZugriffBerechtigungException}.
     * 
     * @param ausnahmeId
     *            Die AusnahmeId, die mit Hilfe einer Instanz eines {@link FehlertextProvider} aufgelöst
     *            werden kann.
     * @param parameter
     *            Opionale Parameter, die der {@link FehlertextProvider} in die aufglöste Nachricht einbettet.
     */
    public ZugriffBerechtigungException(String ausnahmeId, String... parameter) {
        super(ausnahmeId, FEHLERTEXT_PROVIDER, parameter);
    }

}
