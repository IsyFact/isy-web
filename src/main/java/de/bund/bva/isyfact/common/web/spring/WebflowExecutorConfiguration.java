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
package de.bund.bva.isyfact.common.web.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.config.FlowExecutorBuilder;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.executor.FlowExecutor;

import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Konfiguration des Flow-Executors als Spring Konfigurationsklasse.
 *
 * Dies ist eine Abweichung zu den Vorgaben, jedoch notwendig um den
 * Standard-Webflow-Konfigurationsmechanismus benutzen zu können und dabei Optionen in die betriebliche
 * Konfiguration auslagern zu können (Properties in der webflow-XML-Konfiguration werden nicht aufgelöst).
 */
@Configuration
public class WebflowExecutorConfiguration extends AbstractFlowConfiguration {

    /**
     * Zugriff auf die Konfigurationsbean.
     */
    @Autowired
    @Qualifier("konfiguration")
    private Konfiguration konfiguration;

    /**
     * Der Flow Executor.
     * @return Die Instanz.
     */
    @Bean
    public FlowExecutor flowExecutor() {
        FlowExecutorBuilder builder =
            getFlowExecutorBuilder((FlowDefinitionLocator) getApplicationContext().getBean("flowRegistry"))
                .addFlowExecutionListener(
                    (FlowExecutionListener) getApplicationContext().getBean("facesContextListener"))
                .addFlowExecutionListener(
                    (FlowExecutionListener) getApplicationContext().getBean("securityListener"));

        // Benutze spezifische Werte nur, falls diese konfiguriert wurden. Ansonsten wird automatisch der
        // Spring-Standard verwendet.
        if (this.konfiguration.getAsString("gui.webflow.max.executions", null) != null) {
            builder =
                builder.setMaxFlowExecutions(this.konfiguration.getAsInteger("gui.webflow.max.executions"));
        }

        if (this.konfiguration.getAsString("gui.webflow.max.snapshots", null) != null) {
            builder =
                builder.setMaxFlowExecutionSnapshots(this.konfiguration
                    .getAsInteger("gui.webflow.max.snapshots"));
        }

        return builder.build();

    }
}
