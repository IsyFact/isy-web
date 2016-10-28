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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * SimpleMappingExceptionResolver, der die Exception mittels FehlertextUtil loggt.
 * @author Capgemini, Tobias Groeger
 * @version $Id: SimpleMappingExceptionResolverWithAdvancedLogging.java 133934 2015-04-02 12:01:36Z
 *          sdm_tgroeger $
 */
public class SimpleMappingExceptionResolverWithAdvancedLogging extends SimpleMappingExceptionResolver {

    /**
     * Die View, die gezeigt wird, wenn der Snapshot nicht gefunden wurde.
     */
    private String snapshotNotFoundView;

    /**
     * Die View, die gezeigt wird, wenn der aktuelle Nutzer keine Berechtigung zur Ansicht der aktuellen Maske
     * hat.
     */
    private String accessDeniedView;

    /** Der Logger. */
    private static final IsyLogger LOG = IsyLoggerFactory
        .getLogger(SimpleMappingExceptionResolverWithAdvancedLogging.class);

    /**
     * Der AusnahmeIdMapper.
     */
    private AusnahmeIdMapper ausnahmeIdMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void logException(Exception ex, HttpServletRequest request) {

        // Schreibe LOG-Eintrag mittels FehlertextUtil
        FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(ex, this.ausnahmeIdMapper, LOG);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {
        // Falls dieser Request über AJAX aufgerufen wurde, führe ein Redirect aus.
        if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
            // It's a JSF ajax request
            response.setContentType("text/xml");
            // Gib eine View zurück, die so nicht gerendert werden kann
            // Dadurch erfolgt ein Redirect auf die allgemeine Fehlerseite
            return new ModelAndView("common/flow/error/ajaxErrorRedirect").addObject("exception", ex);
        }
        if (ex instanceof FlowExecutionRestorationFailureException) {
            return getModelAndView(this.snapshotNotFoundView, ex, request);
        } else if (ex instanceof FlowExecutionException && ex.getCause() != null
            && ex.getCause() instanceof AccessDeniedException) {
            return getModelAndView(this.accessDeniedView, ex, request);
        } else {
            return super.doResolveException(request, response, handler, ex);
        }
    }

    @Required
    public void setAusnahmeIdMapper(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

    @Required
    public void setSnapshotNotFoundView(String snapshotNotFoundView) {
        this.snapshotNotFoundView = snapshotNotFoundView;
    }

    @Required
    public void setAccessDeniedView(String accessDeniedView) {
        this.accessDeniedView = accessDeniedView;
    }

}
