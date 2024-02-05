package de.bund.bva.isyfact.common.web.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import javax.servlet.FilterRegistration;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.web.filter.CharacterEncodingFilter;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

public class AutoConfigurationTest {

    // enable autoconfiguration conditions evaluation report
    private final ConditionEvaluationReportLoggingListener conditionEvalReport = new ConditionEvaluationReportLoggingListener(
            LogLevel.DEBUG);

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner(AnnotationConfigServletWebServerApplicationContext::new)
            .withInitializer(ctx -> LoggingSystem.get(ctx.getClassLoader()).beforeInitialize())
            .withInitializer(conditionEvalReport)
            .withInitializer(new ConfigDataApplicationContextInitializer())
            .withAllowCircularReferences(false)
            .withAllowBeanDefinitionOverriding(false)
            .withConfiguration(AutoConfigurations.of(
                    MvcAutoConfiguration.class,
                    WebFlowAutoConfiguration.class,
                    ControllerAutoConfiguration.class,
                    HttpEncodingAutoConfiguration.class
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



    @Test
    public void test_withAutoConfig_withAllowBeanOverridingFalse_doesNotThrow() {
        contextRunner.run(context -> assertThat(context).hasNotFailed());
    }

    @Test
    public void test_characterEncodingFilter_beanExists() {
        contextRunner.run(context -> assertThat(context)
                .hasNotFailed()
                .hasSingleBean(ControllerAutoConfiguration.class)
                .hasSingleBean(MvcAutoConfiguration.class)
                .hasSingleBean(WebFlowAutoConfiguration.class)
                .hasSingleBean(CharacterEncodingFilter.class));
    }

    @Test
    public void test_characterEncodingFilter_hasProperties() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            CharacterEncodingFilter characterEncodingFilter = context.getBean(CharacterEncodingFilter.class);
            assertThat(characterEncodingFilter.getEncoding()).isEqualTo("UTF-8");
            assertThat(characterEncodingFilter.isForceRequestEncoding()).isTrue();
            assertThat(characterEncodingFilter.isForceResponseEncoding()).isTrue();

        });
    }

    @Test
    public void test_characterEncodingFilter_isRegistered() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            Map<String, ? extends FilterRegistration> context1 = context.getServletContext().getFilterRegistrations();
            assertThat(context1.get("characterEncodingFilter")).isNotNull();
            assertThat(context1.get("requestContextFilter")).isNotNull();
        });
    }
}
