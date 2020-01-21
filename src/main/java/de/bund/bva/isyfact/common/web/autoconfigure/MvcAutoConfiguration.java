package de.bund.bva.isyfact.common.web.autoconfigure;


import de.bund.bva.isyfact.common.web.exception.IsyFactFlowHandlerMapping;
import de.bund.bva.isyfact.common.web.exception.OptimisticLockHandler;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.global.GlobalFlowController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.mvc.JsfView;
import org.springframework.faces.webflow.JsfFlowHandlerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.executor.FlowExecutor;

@EnableWebMvc
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class MvcAutoConfiguration implements WebMvcConfigurer {

    // <mvc:resources>
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/").setCachePeriod(86400);
    }

    @Bean
    public IsyFactFlowHandlerMapping isyFactFlowHandlerMapping(FlowDefinitionRegistry flowRegistry,
        AusnahmeIdMapper ausnahmeIdMapper) {
        IsyFactFlowHandlerMapping isyFactFlowHandlerMapping =
            new IsyFactFlowHandlerMapping(ausnahmeIdMapper, "snapshotNotFoundFlow", "accessDeniedFlow");
        isyFactFlowHandlerMapping.setFlowRegistry(flowRegistry);
        isyFactFlowHandlerMapping.setDefaultHandler(new UrlFilenameViewController());
        return isyFactFlowHandlerMapping;
    }

    // Loest die XHTML Views auf
    @Bean
    public UrlBasedViewResolver jsfViewResolver() {
        UrlBasedViewResolver jsfViewResolver = new UrlBasedViewResolver();
        jsfViewResolver.setViewClass(JsfView.class);
        jsfViewResolver.setPrefix("/WEB-INF/gui/");
        jsfViewResolver.setSuffix(".xhtml");
        return jsfViewResolver;
    }

    @Bean
    public JsfFlowHandlerAdapter jsfFlowHandlerAdapter(FlowExecutor flowExecutor) {
        JsfFlowHandlerAdapter jsfFlowHandlerAdapter = new JsfFlowHandlerAdapter();
        jsfFlowHandlerAdapter.setFlowExecutor(flowExecutor);
        return jsfFlowHandlerAdapter;
    }

    @Bean
    public OptimisticLockHandler optimisticLockHandler(GlobalFlowController globalFlowController) {
        return new OptimisticLockHandler(globalFlowController);
    }

}
