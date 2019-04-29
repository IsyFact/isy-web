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

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * {@link SimpleMappingExceptionResolver}, der Ausnahmen in AJAX-Requests behandelt und ins Log schreibt.
 */
public class SimpleMappingExceptionResolverWithAdvancedLogging extends SimpleMappingExceptionResolver {

    private static final IsyLogger LOG =
        IsyLoggerFactory.getLogger(SimpleMappingExceptionResolverWithAdvancedLogging.class);

    /** Mapper für Fehlerschlüssel von Ausnahmen. */
    private AusnahmeIdMapper ausnahmeIdMapper;

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
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

        return super.doResolveException(request, response, handler, ex);
    }

    @Required
    public void setAusnahmeIdMapper(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

}
