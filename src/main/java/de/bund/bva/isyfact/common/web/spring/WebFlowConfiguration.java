package de.bund.bva.isyfact.common.web.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.config.AbstractFacesFlowConfiguration;
import org.springframework.faces.webflow.FlowFacesContextLifecycleListener;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.webflow.config.FlowExecutorBuilder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.security.SecurityFlowExecutionListener;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.web.SimpleMappingExceptionResolverWithAdvancedLogging;
import de.bund.bva.isyfact.common.web.webflow.titles.TitlesListener;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

@Configuration
@ComponentScan("de.bund.bva.isyfact.common.web.spring")
public class WebFlowConfiguration extends AbstractFacesFlowConfiguration {

    /**
     * Zugriff auf die Konfigurationsbean.
     */
    @Autowired
    @Qualifier("konfiguration")
    private Konfiguration konfiguration;

    @Bean
    public FlowDefinitionRegistry flowRegistry() {
        return getFlowDefinitionRegistryBuilder()
                .addFlowLocationPattern("/WEB-INF/gui/**/*Flow.xml")
                .setFlowBuilderServices(flowBuilderServices())
                .build();
    }

    @Bean
    public FlowBuilderServices flowBuilderServices() {
        return getFlowBuilderServicesBuilder()
                .build();
    }

    @Bean
    public FlowExecutor flowExecutor(TitlesListener titlesListener,
                                     SecurityFlowExecutionListener securityListener,
                                     FlowFacesContextLifecycleListener facesContextListener) {
        FlowExecutorBuilder builder = getFlowExecutorBuilder(flowRegistry())
                .addFlowExecutionListener(facesContextListener)
                .addFlowExecutionListener(securityListener)
                .addFlowExecutionListener(titlesListener);

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

    @Bean
    public FlowFacesContextLifecycleListener facesContextListener() {
        return new FlowFacesContextLifecycleListener();
    }

    @Bean
    public SimpleMappingExceptionResolverWithAdvancedLogging simpleMappingExceptionResolverWithAdvancedLogging(
            AusnahmeIdMapper ausnahmeIdMapper) {
        SimpleMappingExceptionResolverWithAdvancedLogging logging =
                new SimpleMappingExceptionResolverWithAdvancedLogging();
        logging.setDefaultErrorView("common/flow/error/errorViewState");
        logging.setAusnahmeIdMapper(ausnahmeIdMapper);
        return logging;
    }

    @Bean
    public TitlesListener flowExecutionTitlesListener(MessageSource messageSource) {
        TitlesListener titlesListener = new TitlesListener();
        titlesListener.setMessageSource(messageSource);
        return titlesListener;
    }

    @Bean
    @ConditionalOnBean(AccessDecisionManager.class)
    public SecurityFlowExecutionListener securityListener(AccessDecisionManager accessDecisionManager) {
        SecurityFlowExecutionListener securityFlowExecutionListener = new SecurityFlowExecutionListener();
        securityFlowExecutionListener.setAccessDecisionManager(accessDecisionManager);
        return securityFlowExecutionListener;
    }

}
