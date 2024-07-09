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
package de.bund.bva.isyfact.common.web.jsf.components.outputformat;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.StateHelper;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * <p>
 * The <code>&lt;o:outputFormat&gt;</code> is a component that extends the standard
 * <code>&lt;h:outputFormat&gt;</code> with support for capturing the output and exposing it into the request
 * scope by the variable name as specified by the <code>var</code> attribute.
 * <p>
 * You can use it the same way as <code>&lt;h:outputFormat&gt;</code>, you only need to change <code>h:</code>
 * into <code>o:</code> to get the extra support for <code>var</code> attribute. Here's are some usage
 * examples:
 *
 * <pre>
 * &lt;o:outputFormat value="#{i18n['link.title']}" var="_link_title"&gt;
 *     &lt;f:param value="#{bean.foo}" /&gt;
 *     &lt;f:param value="#{bean.bar}" /&gt;
 * &lt;/o:outputFormat&gt;
 * &lt;h:commandLink value="#{i18n['link.value']}" title="#{_link_title}" /&gt;
 * </pre>
 *
 * <pre>
 * &lt;o:outputFormat value="#{bean.number}" var="_percentage"&gt;
 *     &lt;f:convertNumber type="percent" /&gt;
 * &lt;/o:outputFormat&gt;
 * &lt;div title="Percentage: #{_percentage}" /&gt;
 * </pre>
 * <p>
 * Make sure that the <code>var</code> attribute value doesn't conflict with any of existing variable names in
 * the current EL scope, such as managed bean names. It would be a good naming convention to start their names
 * with <code>_</code>.
 *
 * @since 1.2
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@FacesComponent(createTag = true, tagName = "outputFormat", namespace = "http://java.sun.com/jsf/isyfact2",
    value = OmniOutputFormat.COMPONENT_TYPE)
public class OmniOutputFormat extends HtmlOutputFormat {

    // Public constants
    // -----------------------------------------------------------------------------------------------

    /** The standard component type. */
    @SuppressWarnings("hiding")
    public static final String COMPONENT_TYPE = "org.omnifaces.component.output.OutputFormat";

    // Private constants
    // ----------------------------------------------------------------------------------------------

    private static final String ERROR_EXPRESSION_DISALLOWED =
        "A value expression is disallowed on 'var' attribute of OutputFormat.";

    private enum PropertyKeys {
        // Cannot be uppercased. They have to exactly match the attribute names.
        var;
    }

    // Variables
    // ------------------------------------------------------------------------------------------------------

    private final State state = new State(getStateHelper());

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * An override which checks if this isn't been invoked on <code>var</code> attribute. Finally it delegates
     * to the super method.
     *
     * @throws IllegalArgumentException
     *             When this value expression is been set on <code>var</code> attribute.
     */
    @Override
    public void setValueExpression(String name, ValueExpression binding) {

        if (PropertyKeys.var.toString().equals(name)) {
            throw new IllegalArgumentException(ERROR_EXPRESSION_DISALLOWED);
        }

        super.setValueExpression(name, binding);
    }

    /**
     * If the <code>var</code> attribute is set, start capturing the output.
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        if (getVar() != null) {
            ResponseWriter originalResponseWriter = context.getResponseWriter();
            StringWriter buffer = new StringWriter();
            context.setResponseWriter(originalResponseWriter.cloneWithWriter(buffer));
            context.getAttributes().put(this + "_writer", originalResponseWriter);
            context.getAttributes().put(this + "_buffer", buffer);
        }

        super.encodeBegin(context);
    }

    /**
     * If the <code>var</code> attribute is set, stop capturing the output and expose it in request scope by
     * the <code>var</code> attribute value as variable name.
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        super.encodeEnd(context);

        if (getVar() != null) {
            ResponseWriter originalResponseWriter =
                (ResponseWriter) context.getAttributes().remove(this + "_writer");
            StringWriter buffer = (StringWriter) context.getAttributes().remove(this + "_buffer");
            context.setResponseWriter(originalResponseWriter);
            context.getExternalContext().getRequestMap().put(getVar(), buffer.toString());
        }
    }

    // Attribute getters/setters
    // --------------------------------------------------------------------------------------

    /**
     * Returns the variable name which exposes the captured output into the request scope.
     *
     * @return The variable name which exposes the captured output into the request scope.
     */
    public String getVar() {

        return this.state.get(PropertyKeys.var);
    }

    /**
     * Sets the variable name which exposes the captured output into the request scope.
     *
     * @param var
     *            The variable name which exposes the captured output into the request scope.
     */
    public void setVar(String var) {

        this.state.put(PropertyKeys.var, var);
    }

    /**
     * Helper class for StateHelper that uses generic type-inference to make code that uses the StateHelper
     * slightly less verbose.
     *
     * @since 1.1
     */
    private static class State {

        private final StateHelper stateHelper;

        public State(StateHelper stateHelper) {
            this.stateHelper = stateHelper;
        }

        /**
         * Attempts to find a value associated with the specified key in the component's state.
         * <p>
         * See {@link StateHelper#eval(Serializable)}
         *
         * @param <T>
         *            The expected return type.
         * @param key
         *            the name of the value in component's state
         * @return The value associated with the specified key.
         * @throws ClassCastException
         *             When <code>T</code> is of wrong type.
         */
        @SuppressWarnings("unchecked")
        public <T> T get(Serializable key) {

            return (T) this.stateHelper.eval(key);
        }

        /**
         * Puts a value in the component's state and returns the previous value. Note that the previous value
         * has to be of the same type as the value being set. If this is unwanted, use the original
         * StateHelper.
         * <p>
         * See {@link StateHelper#put(Serializable, Object)}
         *
         * @param <T>
         *            The expected value and return type.
         * @param key
         *            The name of the value in component's state.
         * @param value
         *            The value to put in component's state.
         * @return The previous value, if any.
         * @throws ClassCastException
         *             When <code>T</code> is of wrong type.
         */
        @SuppressWarnings("unchecked")
        public <T> T put(Serializable key, T value) {

            return (T) this.stateHelper.put(key, value);
        }

    }
}
