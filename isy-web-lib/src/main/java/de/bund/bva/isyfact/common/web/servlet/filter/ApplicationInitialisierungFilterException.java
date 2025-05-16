package de.bund.bva.isyfact.common.web.servlet.filter;

import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Wird fuer Laufzeitfehler bei der Application-Initialisierung genutzt.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
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
