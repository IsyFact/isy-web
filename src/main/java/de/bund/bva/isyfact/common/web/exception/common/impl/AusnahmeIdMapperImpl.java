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
package de.bund.bva.isyfact.common.web.exception.common.impl;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.pliscommon.exception.FehlertextProvider;
import de.bund.bva.pliscommon.util.exception.MessageSourceFehlertextProvider;

/**
 * Default Implementierung von {@link AusnahmeIdMapper}. Optional kann der {@link FehlertextProvider}
 * verändern werden, um das Laden von Fehlertexten zu beeinflussen. Standmäßig wird die Ausnahme-ID anhand
 * {@link MessageSourceFehlertextProvider} geladen.
 * 
 * @author Capgemini, Tobias Waller
 * @version $Id:$
 */
public class AusnahmeIdMapperImpl implements AusnahmeIdMapper {

    /** die AusnahmeId, die verwendet wird, wenn ein Fehler unbekannt ist. (Generischer Fehler). */
    private final String fallbackAusnahmeId;

    /**
     * Default {@link FehlertextProvider}.
     */
    private FehlertextProvider fehlertextProvider = new MessageSourceFehlertextProvider();

    /**
     * Erzeugt einen neuen AusnahmeIdMapperImpl mit gegebener {@link #fallbackAusnahmeId}.
     * @param fallbackAusnahmeId
     *            Die Fallback-Ausnahme-ID
     */
    public AusnahmeIdMapperImpl(String fallbackAusnahmeId) {
        this.fallbackAusnahmeId = fallbackAusnahmeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAusnahmeId(Throwable throwable) {
        // In der Default-Implementierung wird noch keine anwendungsspezifische Exception behandelt.
        // Subklassen können sich hier einklinken.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFallbackAusnahmeId() {
        return fallbackAusnahmeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FehlertextProvider getFehlertextProvider() {
        return fehlertextProvider;
    }

    /**
     * Setzt das Feld 'fehlertextProvider'.
     * @param fehlertextProvider
     *            Neuer Wert für fehlertextProvider
     */
    public void setFehlertextProvider(FehlertextProvider fehlertextProvider) {
        this.fehlertextProvider = fehlertextProvider;
    }

}
