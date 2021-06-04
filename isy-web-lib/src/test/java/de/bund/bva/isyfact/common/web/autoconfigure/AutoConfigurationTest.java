package de.bund.bva.isyfact.common.web.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

public class AutoConfigurationTest {

    // enable autoconfiguration conditions evaluation report
    private final ConditionEvaluationReportLoggingListener initializer = new ConditionEvaluationReportLoggingListener(
			LogLevel.DEBUG);

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner(AnnotationConfigServletWebServerApplicationContext::new)
            .withInitializer(initializer)
            .withConfiguration(AutoConfigurations.of(
                    MvcAutoConfiguration.class,
                    WebFlowAutoConfiguration.class,
                    ControllerAutoConfiguration.class
            ))
            .withUserConfiguration(TestConfiguration.class);

    @Configuration
    static class TestConfiguration {

        @Bean
        TomcatServletWebServerFactory tomcatFactory() {
            return new TomcatServletWebServerFactory(0);
        }

        @Bean
        Konfiguration konfiguration() {
            return Mockito.mock(Konfiguration.class);
        }

        @Bean
        @SuppressWarnings("unchecked")
        AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter() {
            return Mockito.mock(AufrufKontextVerwalter.class);
        }

        @Bean
        AusnahmeIdMapper ausnahmeIdMapper() {
            return Mockito.mock(AusnahmeIdMapper.class);
        }

        @Bean
        AccessDecisionManager accessDecisionManager() {
            return Mockito.mock(AccessDecisionManager.class);
        }
    }

    @Test
    public void testContextSuccessful() {
        contextRunner.run(context -> assertThat(context).hasNotFailed());
    }
}
