package de.bund.bva.isyfact.common.web.autoconfigure;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.config.AbstractFacesFlowConfiguration;
import org.springframework.faces.webflow.FlowFacesContextLifecycleListener;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.webflow.config.FlowExecutorBuilder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.security.SecurityFlowExecutionListener;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.web.SimpleMappingExceptionResolverWithAdvancedLogging;
import de.bund.bva.isyfact.common.web.webflow.titles.TitlesListener;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

@Configuration
public class WebFlowAutoConfiguration extends AbstractFacesFlowConfiguration {

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
    public FlowExecutor flowExecutor(
            Konfiguration konfiguration,
            ObjectProvider<FlowExecutionListener> flowExecutionListeners) {

        final FlowExecutorBuilder builder = getFlowExecutorBuilder(flowRegistry());

        flowExecutionListeners.forEach(builder::addFlowExecutionListener);

        // Benutze spezifische Werte nur, falls diese konfiguriert wurden. Ansonsten wird automatisch der
        // Spring-Standard verwendet.
        if (konfiguration.getSchluessel().contains("gui.webflow.max.executions")) {
            builder.setMaxFlowExecutions(konfiguration.getAsInteger("gui.webflow.max.executions"));
        }

        if (konfiguration.getSchluessel().contains("gui.webflow.max.snapshots")) {
            builder.setMaxFlowExecutionSnapshots(konfiguration.getAsInteger("gui.webflow.max.snapshots"));
        }

        return builder.build();
    }

    // Logging
    @Bean
    public SimpleMappingExceptionResolverWithAdvancedLogging simpleMappingExceptionResolverWithAdvancedLogging(
            AusnahmeIdMapper ausnahmeIdMapper) {
        SimpleMappingExceptionResolverWithAdvancedLogging logging =
                new SimpleMappingExceptionResolverWithAdvancedLogging(ausnahmeIdMapper);
        logging.setDefaultErrorView("common/flow/error/errorViewState");
        return logging;
    }

    // Listener
    @Bean
    public FlowFacesContextLifecycleListener facesContextListener() {
        return new FlowFacesContextLifecycleListener();
    }

    @Bean
    public TitlesListener flowExecutionTitlesListener(MessageSource messageSource) {
        return new TitlesListener(messageSource);
    }

    @Bean
    public SecurityFlowExecutionListener securityListener(AccessDecisionManager accessDecisionManager) {
        SecurityFlowExecutionListener securityFlowExecutionListener = new SecurityFlowExecutionListener();
        securityFlowExecutionListener.setAccessDecisionManager(accessDecisionManager);
        return securityFlowExecutionListener;
    }

    @Override
    @Bean
    @ConditionalOnMissingBean
    public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
        return super.httpRequestHandlerAdapter();
    }
}
