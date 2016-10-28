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
package de.bund.bva.isyfact.common.web.exception.common;

import java.util.UUID;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.exception.common.FehlerInformation.Fehlertyp;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.pliscommon.exception.PlisBusinessException;
import de.bund.bva.pliscommon.exception.PlisException;
import de.bund.bva.pliscommon.exception.PlisTechnicalRuntimeException;

/**
 * Stellt gemeinsame Methoden für das Erstellen des GUI- und LOG-Fehlertextes für die unterschiedlichen
 * Exception Handler bereit.
 *
 * @author Capgemini, Andreas Hörning.
 * @version $Id: FehlertextUtil.java 143397 2015-07-30 08:49:32Z sdm_apheino $
 */
public class FehlertextUtil {

    /**
     * Schreibt einen Log-Eintrag über die Exception. Für Fehler, welche nicht in der Anwendung speziell
     * erzeugt werden, werden eine neue Unique-ID generiert bzw. Ausnahme-ID erstellt. Dann werden die
     * Log-Einträge genauso wie bei einer PLIS-Exception geschreiben.
     * <p>
     * Extrahiert außerdem aus der übergebenen Exception den Fehlertext der ersten fachlichen oder technischen
     * Plis-Exception.
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
     * Plis-Exception.
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
     * AusnahmeIdMapper oder weil es sich um eine Plis-Exception handelt), werden diese ermittelt.
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
        // checked Plis Exceptions
        if (fehler instanceof PlisException) {
            PlisException plisException = (PlisException) fehler;

            FehlerInformation fehlerInformation = new FehlerInformation();
            fehlerInformation.setFehlerId(plisException.getAusnahmeId());
            fehlerInformation.setFehlernachricht(plisException.getFehlertext());
            fehlerInformation.setUuid(plisException.getUniqueId());

            if (fehler instanceof PlisBusinessException) {
                fehlerInformation.setTyp(Fehlertyp.FACHLICH);
            } else {
                fehlerInformation.setTyp(Fehlertyp.TECHNISCH);
            }
            return fehlerInformation;
        }

        // runtime Plis Exceptions
        if (fehler instanceof PlisTechnicalRuntimeException) {
            PlisTechnicalRuntimeException plisException = (PlisTechnicalRuntimeException) fehler;

            FehlerInformation fehlerInformation = new FehlerInformation();
            fehlerInformation.setFehlerId(plisException.getAusnahmeId());
            fehlerInformation.setFehlernachricht(plisException.getFehlertext());
            fehlerInformation.setUuid(plisException.getUniqueId());
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
     *            beschreibt alle Informationen, die in einem Plis-Fehler vorhanden sind
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
