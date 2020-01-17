package de.bund.bva.isyfact.common.web.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "de.bund.bva.isyfact.common.web",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "de\\.bund\\.bva\\.isyfact\\.common\\.web\\.spring\\..*"))
public class ControllerConfiguration {

}
