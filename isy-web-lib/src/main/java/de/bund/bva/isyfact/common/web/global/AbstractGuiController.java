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
package de.bund.bva.isyfact.common.web.global;

import java.util.Map;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * Der abstrakte GUI-Controller. Gibt Methoden und Konfigurationen vor, welcher jeder Masken-Controller oder
 * Teimasken-Controller bereitstellen muss.
 *
 * @param <T>
 *            Das spezifische AbstractMaskenModel.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class AbstractGuiController<T extends AbstractMaskenModel> implements RfGuiController<T> {

    /**
     * Extrahiert ein Model vom Typ {@link AbstractMaskenModel} aus dem FlowScope.
     *
     * @return das MaskenModel oder <code>null</code>, falls dieses nicht gefunden wurde.
     */
    public T getMaskenModelZuController() {
        Class<T> modelKlasse = getMaskenModelKlasseZuController();

        Map<String, Object> flowScopeAttributeMap = getFlowScopeAttributeMap();
        if (flowScopeAttributeMap == null) {
            return null;
        }
        for (Object flowScopeAttribute : flowScopeAttributeMap.values()) {
            if (flowScopeAttribute == null) {
                continue;
            }

            if (modelKlasse.isAssignableFrom(flowScopeAttribute.getClass())
                && flowScopeAttribute.getClass().isAssignableFrom(modelKlasse)) {
                T result = modelKlasse.cast(flowScopeAttribute);
                return result;
            }
        }

        return null;
    }

    /**
     * Gibt den Inhalt des Flow-Scopes zurück.
     * @return Der Inhalt des Flowscopes.
     */
    private Map<String, Object> getFlowScopeAttributeMap() {
        MutableAttributeMap<Object> mutualAttributeMap =
            RequestContextHolder.getRequestContext().getFlowScope();
        Map<String, Object> result = mutualAttributeMap.asMap();
        return result;
    }

    /**
     * Gibt die durch den Controller verwaltete {@link AbstractMaskenModel}-Klasse zurueck.
     *
     * @return das {@link AbstractMaskenModel}.
     */
    protected abstract Class<T> getMaskenModelKlasseZuController();

}
