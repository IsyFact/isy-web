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
