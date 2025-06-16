package de.bund.bva.isyfact.common.web.exception.common.impl;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.util.exception.MessageSourceFehlertextProvider;

/**
 * Default Implementierung von {@link AusnahmeIdMapper}. Optional kann der {@link FehlertextProvider}
 * verändern werden, um das Laden von Fehlertexten zu beeinflussen. Standmäßig wird die Ausnahme-ID anhand
 * {@link MessageSourceFehlertextProvider} geladen.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
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
