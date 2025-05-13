package de.bund.bva.isyfact.common.web.exception;

import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Eine Exception, die geworfen wird, wenn ein Benutzer keine Berechtigung für einen Zugriff hat.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
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
