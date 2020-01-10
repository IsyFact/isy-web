package de.bund.bva.isyfact.common.web.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.bund.bva.isyfact.common.web.servlet.filter.ResourceCacheHeaderFilter;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

@Configuration
public class FilterConfiguration {

    @Bean
    @ConditionalOnBean(Konfiguration.class)
    public ResourceCacheHeaderFilter resourceCacheHeaderFilter(Konfiguration konfiguration) {
        ResourceCacheHeaderFilter resourceCacheHeaderFilter = new ResourceCacheHeaderFilter();
        resourceCacheHeaderFilter.setKonfiguration(konfiguration);
        return resourceCacheHeaderFilter;
    }

}
