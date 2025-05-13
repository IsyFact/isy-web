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
