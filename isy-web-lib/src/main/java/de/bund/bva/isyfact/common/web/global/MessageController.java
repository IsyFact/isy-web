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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlerInformation;
import de.bund.bva.isyfact.common.web.exception.common.FehlerInformation.Fehlertyp;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * Eine Controller, welcher Funktionen zum Darstellen von Nachrichten bietet.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageController implements GuiController {

    /**
     * Der Logger.
     */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(MessageController.class);

    /**
     * Wird für die weitere Kategorisierung von Infomeldungen verwendet.
     */
    private static final String INFO_SUMMARY_TAG = "INFO";

    /**
     * Wird für die weitere Kategorisierung von Infomeldungen verwendet.
     */
    private static final String SUCCESS_SUMMARY_TAG = "SUCCESS";

    /**
     * Liefert eine Ausnahme-ID für Exceptions, welche nicht in der Anwendung speziell erzeugt werden (z.b.
     * DataAccessException, TransactionException). Dieser Mapper ist anwendungsspezifisch und <b>muss</b>
     * gesetzt werden.
     */
    private AusnahmeIdMapper ausnahmeIdMapper;

    @Autowired
    public MessageController(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

    /**
     * Schreibt eine Info-Nachricht.
     *
     * @param information Der Inhalt.
     */
    public void writeInfoMessage(String information) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, INFO_SUMMARY_TAG, information));
    }

    /**
     * Schreibt eine Erfolgs-Meldung.
     *
     * @param success Der Inhalt.
     */
    public void writeSuccessMessage(String success) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS_SUMMARY_TAG, success));
    }

    /**
     * Schreibt eine Warnmeldung.
     *
     * @param warning Der Inhalt der Warnmeldung.
     * @param summary Die Zusammenfassung/der Titel.
     */
    public void writeWarnMessage(String warning, String summary) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, summary, warning));
    }

    /**
     * Schreibt eine Fehlermeldung.
     *
     * @param error   Der Inhalt der Fehlermeldung.
     * @param summary Die Zusammenfassung/der Titel.
     */
    public void writeErrorMessage(String error, String summary) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, error));
    }

    /**
     * Gibt die aktuellen Infomeldungen aus dem FacesContext zurück.
     *
     * @return Die Meldungen
     */
    public List<FacesMessage> getCurrentInfoMessages() {
        return getCurrentMessages(FacesMessage.SEVERITY_INFO, INFO_SUMMARY_TAG);
    }

    /**
     * Gibt die aktuellen Erfolgsmeldungen aus dem FacesContext zurück.
     *
     * @return Die Meldungen
     */
    public List<FacesMessage> getCurrentSuccessMessages() {
        return getCurrentMessages(FacesMessage.SEVERITY_INFO, SUCCESS_SUMMARY_TAG);
    }

    /**
     * Gibt die aktuellen Warnmeldungen aus dem FacesContext zurück.
     *
     * @return Die Meldungen
     */
    public List<FacesMessage> getCurrentWarnMessages() {
        return getCurrentMessages(FacesMessage.SEVERITY_WARN, null);
    }

    /**
     * Gibt die aktuellen Fehlermeldungen aus dem FacesContext zurück.
     *
     * @return Die Meldungen
     */
    public List<FacesMessage> getCurrentErrorMessages() {
        return getCurrentMessages(FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Gibt die aktuellen Nachrichten mit den angegebenen Eigenschaften zurück.
     *
     * @param severity   Die Kategorie der Nachricht.
     * @param summaryTag Das zu beinhaltende Summary-Tag, falls Nachrichten noch weiter klassifiziert werden sollen.
     * @return Die Liste an Nachrichten.
     */
    private List<FacesMessage> getCurrentMessages(FacesMessage.Severity severity, String summaryTag) {

        List<FacesMessage> messages = new ArrayList<FacesMessage>();

        Iterator<FacesMessage> iterator = FacesContext.getCurrentInstance().getMessages(null);
        while (iterator.hasNext()) {
            FacesMessage message = iterator.next();

            if (message.getSeverity().equals(severity)) {
                if (summaryTag == null || summaryTag.equals(message.getSummary())) {
                    messages.add(message);
                }
            }
        }

        return messages;
    }

    /**
     * Schreibt und loggt eine Exception.
     *
     * @param t die Exception
     */
    public void writeAndLogException(Throwable t) {

        // Schreibe LOG-Eintrag
        FehlerInformation fehlerInformation =
                FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(t, this.ausnahmeIdMapper, LOG);

        writeErrorFacesMessage(fehlerInformation);
    }

    /**
     * Schreibt eine Exception.
     *
     * @param t die Exception
     */
    public void writeException(Throwable t) {

        // Ermittle Fehlerinformation
        FehlerInformation fehlerInformation =
                FehlertextUtil.ermittleFehlerinformation(t, this.ausnahmeIdMapper);

        writeErrorFacesMessage(fehlerInformation);
    }

    /**
     * Schreibt eine Fehlernachricht auf Basis der Fehlerinformationen.
     *
     * @param fehlerInformation Die Fehlerinformation
     */
    private void writeErrorFacesMessage(FehlerInformation fehlerInformation) {
        // Schreibe Faces-Message
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                        fehlerInformation.getTyp().equals(Fehlertyp.TECHNISCH) ? FacesMessage.SEVERITY_ERROR
                                : FacesMessage.SEVERITY_WARN,
                        fehlerInformation.getGuiErrorMessageTitle(), fehlerInformation.getGuiErrorMessage()));
    }

    /**
     * Loggt eine Exception.
     *
     * @param t die Exception
     */
    public void logException(Throwable t) {

        // Schreibe LOG-Eintrag
        FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(t, this.ausnahmeIdMapper, LOG);
    }

    public void setAusnahmeIdMapper(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

}
