package de.bund.bva.isyfact.common.web.exception.common;

import java.util.UUID;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.exception.common.FehlerInformation.Fehlertyp;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.exception.BusinessException;
import de.bund.bva.isyfact.exception.BaseException;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

/**
 * Stellt gemeinsame Methoden für das Erstellen des GUI- und LOG-Fehlertextes für die unterschiedlichen
 * Exception Handler bereit.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class FehlertextUtil {

    /**
     * Schreibt einen Log-Eintrag über die Exception. Für Fehler, welche nicht in der Anwendung speziell
     * erzeugt werden, werden eine neue Unique-ID generiert bzw. Ausnahme-ID erstellt. Dann werden die
     * Log-Einträge genauso wie bei einer Isy-Exception geschreiben.
     * <p>
     * Extrahiert außerdem aus der übergebenen Exception den Fehlertext der ersten fachlichen oder technischen
     * Isy-Exception.
     *
     * @param exception
     *            die Exception, für die ein Log-Eintrag geschrieben wird.
     * @param ausnahmeIdMapper
     *            Der Ausnahme-ID-Mapper.
     * @param logger
     *            Der Logger.
     *
     * @return Die Fehlerinformation.
     *
     */
    public static FehlerInformation schreibeLogEintragUndErmittleFehlerinformation(Throwable exception,
        AusnahmeIdMapper ausnahmeIdMapper, IsyLogger logger) {
        Throwable aktuellerCause = exception;

        FehlerInformation fehlerInformation = ermittleFehlerinformation(aktuellerCause, ausnahmeIdMapper);

        // Daten waren entweder bereits in der Exception-Hierarchy vorhanden, oder wurden gerade erzeugt.
        schreibeLogEintragAbhaengigVomFehlertyp(exception, fehlerInformation, logger);

        return fehlerInformation;
    }

    /**
     * Ermittelt Fehlerinformation. Für Fehler, welche nicht in der Anwendung speziell erzeugt werden, werden
     * eine neue Unique-ID generiert bzw. Ausnahme-ID erstellt.
     * <p>
     * Extrahiert außerdem aus der übergebenen Exception den Fehlertext der ersten fachlichen oder technischen
     * Isy-Exception.
     *
     * @param exception
     *            die Exception.
     * @param ausnahmeIdMapper
     *            Der Ausnahme-ID-Mapper.
     *
     * @return Die Fehlerinformation.
     *
     */
    public static FehlerInformation ermittleFehlerinformation(Throwable exception,
        AusnahmeIdMapper ausnahmeIdMapper) {
        Throwable aktuellerCause = exception;

        // suche in der Exception Hierarchy nach einem bekannten Fehler, der geloggt werden kann:
        FehlerInformation fehlerInformation = null;
        while (aktuellerCause != null) {
            if (fehlerInformation != null || aktuellerCause == aktuellerCause.getCause()) {
                // wenn Fehlerinformationen gefunden wurden, oder wir am Ende angekommen sind, unterbrechen
                // wir die Suche
                break;
            }
            fehlerInformation = extrahiereFehlerInformation(aktuellerCause, ausnahmeIdMapper);
            aktuellerCause = aktuellerCause.getCause();
        }

        if (fehlerInformation == null) {
            // Es handelt sich um einen unbehandelten Fehler. Daten müssen erzeugt werden.
            String ausnahmeId = ausnahmeIdMapper.getFallbackAusnahmeId();
            fehlerInformation = erstelleTechnischeFehlerinformationMitNeuerUuid(ausnahmeId, ausnahmeIdMapper);
        }

        return fehlerInformation;
    }

    /**
     * Wenn Fehlerinformationen in dem übergebenen Fehler (nicht dessen case) ermittelbar sind (Durch den
     * AusnahmeIdMapper oder weil es sich um eine Isy-Exception handelt), werden diese ermittelt.
     *
     * @param fehler
     *            der zu untersuchende Fehler.
     * @param ausnahmeIdMapper
     *            Der Ausnahme-ID-Mapper.
     *
     * @return Fehlerinformationen oder <code>null</code> falls diese nicht aus dem übergebenen Fehler
     *         ermittelt werden konnten.
     */
    private static FehlerInformation extrahiereFehlerInformation(Throwable fehler,
        AusnahmeIdMapper ausnahmeIdMapper) {
        // checked Isy Exceptions
        if (fehler instanceof BaseException) {
            BaseException isyException = (BaseException) fehler;

            FehlerInformation fehlerInformation = new FehlerInformation();
            fehlerInformation.setFehlerId(isyException.getAusnahmeId());
            fehlerInformation.setFehlernachricht(isyException.getFehlertext());
            fehlerInformation.setUuid(isyException.getUniqueId());

            if (fehler instanceof BusinessException) {
                fehlerInformation.setTyp(Fehlertyp.FACHLICH);
            } else {
                fehlerInformation.setTyp(Fehlertyp.TECHNISCH);
            }
            return fehlerInformation;
        }

        // runtime Isy Exceptions
        if (fehler instanceof TechnicalRuntimeException) {
            TechnicalRuntimeException isyException = (TechnicalRuntimeException) fehler;

            FehlerInformation fehlerInformation = new FehlerInformation();
            fehlerInformation.setFehlerId(isyException.getAusnahmeId());
            fehlerInformation.setFehlernachricht(isyException.getFehlertext());
            fehlerInformation.setUuid(isyException.getUniqueId());
            fehlerInformation.setTyp(Fehlertyp.TECHNISCH);

            return fehlerInformation;
        }

        // versuche anwendungsspezifisches Mapping
        String ausnahmeId = ausnahmeIdMapper.getAusnahmeId(fehler);
        if (ausnahmeId == null) {
            // Kein anwendungsspezifisches Mapping vorhanden.
            return null;
        }

        // Anwendungsspezifisches Mapping vorhanden. Generiere UUID und lade Fehlertext von dem Provider.
        return erstelleTechnischeFehlerinformationMitNeuerUuid(ausnahmeId, ausnahmeIdMapper);
    }

    /**
     * Schreibt einen Log-Eintrag abhängig vom Fehlertyp. Fachliche Exceptions werden in das Debug-Log
     * geschrieben, technische bzw. unbekannte Exceptions werden in das Error-Log geschrieben.
     *
     * @param exception
     *            die zu loggende Exception
     * @param fehlerInformation
     *            beschreibt alle Informationen, die in einem Isy-Fehler vorhanden sind
     * @param logger
     *            Der Logger.
     */
    private static void schreibeLogEintragAbhaengigVomFehlertyp(Throwable exception,
        FehlerInformation fehlerInformation, IsyLogger logger) {

        String logNachricht = fehlerInformation.getErrorLogMessage();

        if (fehlerInformation.getTyp() == Fehlertyp.FACHLICH) {
            logger.errorFachdaten(EreignisSchluessel.E_FEHLER_FACHDATEN, logNachricht, exception);
        } else {
            logger.error(EreignisSchluessel.E_FEHLER, logNachricht, exception);
        }
    }

    /**
     * Erstellt eine technische {@link FehlerInformation}, wobei die UUID neu generiert wird. Die Ausnahme-ID
     * muss übergeben werden. Der Fehlertext wird anhand {@link AusnahmeIdMapper#getFehlertextProvider()}
     * ermittelt.
     *
     * @param ausnahmeId
     *            die Ausnahme-ID, die in die Fehlerinformation gesetzt wird. Der Fehlertext wird auch von
     *            dieser Ausnahme-ID geladen.
     * @param ausnahmeIdMapper
     *            der Ausnahme-ID-Mapper.
     *
     * @return die erstellte {@link FehlerInformation}
     */
    private static FehlerInformation erstelleTechnischeFehlerinformationMitNeuerUuid(String ausnahmeId,
        AusnahmeIdMapper ausnahmeIdMapper) {
        FehlerInformation fehlerInformation = new FehlerInformation();
        fehlerInformation.setFehlerId(ausnahmeId);
        fehlerInformation.setFehlernachricht(ausnahmeIdMapper.getFehlertextProvider().getMessage(ausnahmeId));
        fehlerInformation.setUuid(UUID.randomUUID().toString());
        fehlerInformation.setTyp(Fehlertyp.TECHNISCH);
        return fehlerInformation;
    }

}
