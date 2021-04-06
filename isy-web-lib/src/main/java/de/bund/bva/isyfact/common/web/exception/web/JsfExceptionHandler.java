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
package de.bund.bva.isyfact.common.web.exception.web;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.servlet.http.HttpServletRequest;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlerInformation;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * Exception Handler für JSF.
 *
 * @author Capgemini
 * @version $Id: JsfExceptionHandler.java 143397 2015-07-30 08:49:32Z sdm_apheino $
 */
public class JsfExceptionHandler extends ExceptionHandlerWrapper {

    /**
     * Logger instance.
     */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(JsfExceptionHandler.class);

    /**
     * Umschließender Exception Handler (Parent).
     */
    private ExceptionHandler wrapped;

    /**
     * Der Ausnahme-ID-Mapper.
     */
    private AusnahmeIdMapper ausnahmeIdMapper;

    /**
     * Erstellt eine neue Instanz des ExceptionHandlers.
     *
     * @param wrapped
     *            umschließender ExceptionHandler.
     * @param ausnahmeIdMapper
     *            Der Ausnahme-ID-Mapper.
     */
    public JsfExceptionHandler(ExceptionHandler wrapped, AusnahmeIdMapper ausnahmeIdMapper) {
        this.wrapped = wrapped;
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();
        if (it.hasNext()) {

            FehlerInformation fehlerInformation = null;

            // Logge alle Fehler
            while (it.hasNext()) {
                ExceptionQueuedEvent event = it.next();
                Throwable t = event.getContext().getException();

                try {
                    // Schreibe LOG Eintrag und ermittle Fehlerinformation.
                    fehlerInformation =
                        FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(t,
                            this.ausnahmeIdMapper, LOG);

                } finally {
                    it.remove();
                }
            }

            // Wenn mindestens ein Fehler existiert, redirecte auf Fehlerseite
            // Im Normalfall tritt immer nur ein Fehler auf.
            redirectZuFehlerseite(fehlerInformation);
        }

        getWrapped().handle();
    }

    /**
     * Redirected zur Fehlerseite.
     *
     * @param fehlerInformation
     *            Die Fehlerinformation.
     */
    private void redirectZuFehlerseite(FehlerInformation fehlerInformation) {

        try {
            direkterRedirectZuFehlerseite(fehlerInformation);
        } catch (IllegalStateException e) {
            // Info ergänzen
            // CHECKSTYLE:OFF
            throw new FacesException(
                "Fehler beim Redirect zu Error-Seite. Hinweis: Bei Fehlern während der Renderphase kann nur auf die Fehlerseite geleitet werden sofern die Antwort noch nicht übertragen wurde. Bei Bedarf kann die Buffer-Size über den Kontextparameter javax.faces.FACELETS_BUFFER_SIZE angepasst werden.",
                e);
            // CHECKSTYLE:ON
        } catch (Throwable t) {
            throw new FacesException("Fehler beim Redirect zu Error-Seite.", t);
        }

    }

    /**
     * Führt einen direkten Redirect zur Fehlerseite durch.
     *
     * @param fehlerInformation
     *            Die Fehlerinformation.
     */
    private void direkterRedirectZuFehlerseite(FehlerInformation fehlerInformation) {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest origRequest = (HttpServletRequest) externalContext.getRequest();

        String contextPath = origRequest.getContextPath();
        String servletPath = origRequest.getServletPath();

        String errorViewUrl =
            contextPath + servletPath + "/errorFlow?fehlerId=" + fehlerInformation.getFehlerId()
                + "&uniqueId=" + fehlerInformation.getUuid();

        // Redirect auf Fehlerseite
        try {
            externalContext.redirect(errorViewUrl);
        } catch (IOException e) {
            LOG.error(EreignisSchluessel.E_WEITERLEITUNG_FEHLERSEITE,
                "Fehler beim Weiterleiten auf die anwendungsinterne Fehlerseite.", e);
        }
    }

}
