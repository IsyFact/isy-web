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

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.FacesContext;

import org.springframework.web.jsf.FacesContextUtils;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;

/**
 * Die Factory zum Erzeugen eines JSF-Exception Handlers.
 *
 * @author Capgemini, Andreas Hörning
 * @version $Id: JsfExceptionHandlerFactory.java 128695 2015-01-22 15:49:46Z sdm_ahoerning $
 */
public class JsfExceptionHandlerFactory extends ExceptionHandlerFactory {

    /**
     * Die Parent-Factory.
     */
    private ExceptionHandlerFactory parent;

    /**
     * Konstruktor. Wird durch JSF aufgerufen.
     *
     * @param parent
     *            Die Parent-Factory
     */
    public JsfExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {

        AusnahmeIdMapper ausnahmeIdMapper =
            FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBean(
                AusnahmeIdMapper.class);

        // Erzeugt einen neuen Exception-Handler
        ExceptionHandler result =
            new JsfExceptionHandler(this.parent.getExceptionHandler(), ausnahmeIdMapper);
        return result;
    }
}
