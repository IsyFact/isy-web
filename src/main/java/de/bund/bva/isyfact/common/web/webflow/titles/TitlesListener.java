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
package de.bund.bva.isyfact.common.web.webflow.titles;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.webflow.core.Annotated;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * Stellt den Title und die Headline bereit.
 * <p>
 * Ein TitlesListener muss im org.springframework.webflow.config.FlowExecutionListenerLoaderFactoryBean in der
 * Property listeners bekannt gemacht werden.
 *
 * @author Capgemini, Artun Subasi
 * @author Capgemini, Tobias Waller
 * @version $Id: TitlesListener.java 143397 2015-07-30 08:49:32Z sdm_apheino $
 */
public class TitlesListener extends FlowExecutionListenerAdapter {

    /**
     * Enthält den Schlüssel für den Title, mit dem er in den Scope geschrieben wird.
     */
    private static final String TITLE_ATTRIBUT = "title";

    /**
     * Enthält den Namen zum Finden des Title in den Nachrichten.
     */
    private static final String TITLE_PROPERTY = "Title";

    /**
     * Enthält den Schlüssel, mit dem in der Flowdefinition der Titel überschrieben werden kann.
     */
    private static final String TITLE_ATTRIBUT_TRANSITION_NAME = "titleKey";

    /**
     * Enthält den Text, der als globaler Präfix für den Title verwendet werden soll.
     */
    private static final String TITLE_PREFIX_GLOBAL = "MAS_Global_Title_Prefix";

    /**
     * Enthält den Schlüssel für die Headline, mit der sie in den Scope geschrieben wird.
     */
    private static final String HEADLINE_ATTRIBUT = "headline";

    /**
     * Enthält den Namen zum Finden der Headline in den Nachrichten.
     */
    private static final String HEADLINE_PROPERTY = "Headline";

    /**
     * Enthält den Schlüssel, mit dem in der Flowdefinition die Headline überschrieben werden kann.
     */
    private static final String HEADLINE_ATTRIBUT_TRANSITION_NAME = "headlineKey";

    /**
     * Enthält das Präfix für Maskentexte.
     */
    private static final String PRAEFIX_TEXTS = "MAS";

    /**
     * Enthält den Schlüssel, der für Globale Angaben verwendet wird.
     */
    public static final String GLOBAL = "Global";

    /**
     * Die MessageSource.
     */
    private MessageSource messageSource;

    /**
     * Der Name eines ViewStates.
     */
    private String viewStateName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateEntering(RequestContext context, StateDefinition state) {
        super.stateEntering(context, state);
        this.viewStateName = state.getId();
        verarbeiteAktion(context, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionExecuting(RequestContext context, TransitionDefinition transition) {
        super.transitionExecuting(context, transition);
        this.viewStateName = transition.getId();
        verarbeiteAktion(context, transition);

    }

    /**
     * Gibt die Message aus den Message-Sources zurück.
     * @param key
     *            Der Schlüssel.
     * @return Die Message.
     */
    protected String getMessage(String key) {

        return this.messageSource.getMessage(key, null, null, Locale.getDefault());
    }

    /**
     * Verarbeitet eine Aktion.
     * @param context
     *            der Context
     * @param aktion
     *            die Aktion
     */
    private void verarbeiteAktion(RequestContext context, Annotated aktion) {
        pageTitleStateEntering(context, aktion);
        pageHeadlineStateEntering(context, aktion);

    }

    /**
     * Bestimmt den konkreten Titel.
     * @param context
     *            der Context
     * @param stateOrTransition
     *            der annotierte ViewState
     */
    private void pageTitleStateEntering(RequestContext context, Annotated stateOrTransition) {

        String pageTitle = bestimmePageTitleSchluessel(stateOrTransition);

        if (pageTitle == null) {
            // Flow-Name
            FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();
            String flowName = flowDefinition.getId();

            // Angabe mit ViewState-Name wird ausgelesen
            pageTitle =
                getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + this.viewStateName + "_" + TITLE_PROPERTY);

            if (pageTitle == null) {
                // Angabe ohne ViewState-Name wird ausgelesen
                pageTitle = getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + TITLE_PROPERTY);
                if (pageTitle == null) {
                    pageTitle = getMessage(PRAEFIX_TEXTS + "_" + GLOBAL + "_" + TITLE_PROPERTY);
                }
            }

            pageTitle = getMessage(TITLE_PREFIX_GLOBAL) + pageTitle;
        }

        fuegePageTitleInFlowScopeHinzu(context, pageTitle);

    }

    /**
     * Bestimmt die konkrete Headline.
     * @param context
     *            der Context
     * @param stateOrTransition
     *            der annotierte ViewState
     */
    private void pageHeadlineStateEntering(RequestContext context, Annotated stateOrTransition) {

        String pageHeadline = bestimmePageHeadlineSchluessel(stateOrTransition);

        if (pageHeadline == null) {
            // Flow-Name
            FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();
            String flowName = flowDefinition.getId();

            // Angabe mit ViewState-Name wird ausgelesen
            pageHeadline =
                getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + this.viewStateName + "_"
                    + HEADLINE_PROPERTY);

            if (pageHeadline == null) {
                // Angabe ohne ViewState-Name wird ausgelesen
                pageHeadline = getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + HEADLINE_PROPERTY);
            }
        }

        fuegePageHeadlineInFlowScopeHinzu(context, pageHeadline);
    }

    /**
     * Fügt den Title in den FlowScope hinzu.
     * @param context
     *            Context
     * @param pageTitle
     *            der Title
     */
    private void fuegePageTitleInFlowScopeHinzu(RequestContext context, String pageTitle) {

        final MutableAttributeMap<Object> flowScope = context.getFlowScope();
        flowScope.put(TITLE_ATTRIBUT, pageTitle);
    }

    /**
     * Fügt die Headline in den FlowScope hinzu.
     * @param context
     *            Context
     * @param pageHeadline
     *            die Headline
     */
    private void fuegePageHeadlineInFlowScopeHinzu(RequestContext context, String pageHeadline) {
        final MutableAttributeMap<Object> flowScope = context.getFlowScope();
        flowScope.put(HEADLINE_ATTRIBUT, pageHeadline);
    }

    /**
     * Bestimmt den Title (Schlüssel). Falls der Attribut-Wert mit "#{xxx}" umgeben ist, wird der Wert "xxx"
     * aufgeloest. So ist bspw. eine Aufloesung von "bc" durch Angabe des Attribut-Werts "#{bc}" gegen
     * "flowScope.bc" moeglich.
     * @param annotatedStateOrTransition
     *            der betretene Zustand bzw die ausgelöste Transition.
     * @return der Title (Schlüssel), der an state annotiert wurde, oder <code>null</code> falls keine
     *         Annotation gefunden werden konnte bzw. wenn der Name leer ist
     */
    private String bestimmePageTitleSchluessel(Annotated annotatedStateOrTransition) {

        if (annotatedStateOrTransition == null) {
            return null;
        }
        String flow = null;
        if (annotatedStateOrTransition instanceof ViewState) {
            flow = ((ViewState) annotatedStateOrTransition).getFlow().getId();
        }

        MutableAttributeMap<Object> attributes = annotatedStateOrTransition.getAttributes();
        String transitionName = (String) attributes.get(TITLE_ATTRIBUT_TRANSITION_NAME);
        if (StringUtils.hasText(transitionName)) {
            // Falls eine Expression mit "#{xxx}" umgeben ist, wird diese Expression aufgeloest.
            if (transitionName.contains("#{")) {
                return resolveElExpression(transitionName, flow);
            }
        }
        return null;
    }

    /**
     * Bestimmt die Headline (Schlüssel). Falls der Attribut-Wert mit "#{xxx}" umgeben ist, wird der Wert
     * "xxx" aufgeloest. So ist bspw. eine Aufloesung von "bc" durch Angabe des Attribut-Werts "#{bc}" gegen
     * "flowScope.bc" moeglich.
     * @param annotatedStateOrTransition
     *            der betretene Zustand bzw die ausgelöste Transition.
     * @return die Headlien (Schlüssel), der an state annotiert wurde, oder <code>null</code> falls keine
     *         Annotation gefunden werden konnte bzw. wenn der Name leer ist
     */
    private String bestimmePageHeadlineSchluessel(Annotated annotatedStateOrTransition) {

        if (annotatedStateOrTransition == null) {
            return null;
        }
        String flow = null;
        if (annotatedStateOrTransition instanceof ViewState) {
            flow = ((ViewState) annotatedStateOrTransition).getFlow().getId();
        }

        MutableAttributeMap<Object> attributes = annotatedStateOrTransition.getAttributes();
        String transitionName = (String) attributes.get(HEADLINE_ATTRIBUT_TRANSITION_NAME);
        if (StringUtils.hasText(transitionName)) {
            // Falls eine Expression mit "#{xxx}" umgeben ist, wird diese Expression aufgeloest.
            if (transitionName.contains("#{")) {
                return resolveElExpression(transitionName, flow);
            }
        }
        return null;
    }

    /**
     * Löst die EL-Expression als String auf.
     * @param elExpression
     *            Die EL-Expression.
     * @param nextFlow
     *            Der Name des neuen Flows.
     * @return Der String
     */
    private String resolveElExpression(String elExpression, String nextFlow) {

        // EL-Context auflösen
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();

        ValueExpression vex = expressionFactory.createValueExpression(elContext, elExpression, String.class);
        return (String) vex.getValue(elContext);
    }

    @Required
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
