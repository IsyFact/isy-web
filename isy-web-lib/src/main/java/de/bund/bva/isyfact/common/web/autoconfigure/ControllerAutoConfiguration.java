package de.bund.bva.isyfact.common.web.autoconfigure;

import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.web.ErrorController;
import de.bund.bva.isyfact.common.web.filetransfer.DownloadHelper;
import de.bund.bva.isyfact.common.web.filetransfer.ZipHelper;
import de.bund.bva.isyfact.common.web.global.GlobalConfigurationController;
import de.bund.bva.isyfact.common.web.global.GlobalFlowController;
import de.bund.bva.isyfact.common.web.global.HeaderHelper;
import de.bund.bva.isyfact.common.web.global.IsyWebKonfigurationHelper;
import de.bund.bva.isyfact.common.web.global.JsfHelper;
import de.bund.bva.isyfact.common.web.global.MessageController;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.controller.NavigationMenuController;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.NavigationMenuGenerierenStrategie;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.impl.NavigationMenuGenerierenAusKonfiguration;
import de.bund.bva.isyfact.common.web.jsf.components.tab.TabController;
import de.bund.bva.isyfact.common.web.layout.ApplikationseiteController;
import de.bund.bva.isyfact.common.web.layout.BasisController;
import de.bund.bva.isyfact.common.web.layout.DetailseiteController;
import de.bund.bva.isyfact.common.web.layout.HilfeController;
import de.bund.bva.isyfact.common.web.layout.LinksnavigationController;
import de.bund.bva.isyfact.common.web.layout.QuicklinksController;
import de.bund.bva.isyfact.common.web.locale.LocaleConfiguration;
import de.bund.bva.isyfact.common.web.servlet.filter.ResourceCacheHeaderFilter;
import de.bund.bva.isyfact.common.web.validation.ValidationController;
import de.bund.bva.isyfact.common.web.webflow.titles.TitlesHelper;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * ComponentScan für die Controller von Isy-Web.
 */
@Configuration
//@ComponentScan(basePackages = "de.bund.bva.isyfact.common.web",
//    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
//            pattern = "de\\.bund\\.bva\\.isyfact\\.common\\.web\\.autoconfigure\\..*"))
public class ControllerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TabController tabController() {
        return new TabController();
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public GlobalConfigurationController globalConfigurationController() {
        return new GlobalConfigurationController();
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public QuicklinksController quicklinksController(Konfiguration konfiguration) {
        return new QuicklinksController(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public GlobalFlowController globalFlowController(MessageController messageController,
                                                 ValidationController validationController,
                                                 ErrorController errorController,
                                                 AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter,
                                                 NavigationMenuController navigationMenuController) {
        return new GlobalFlowController(messageController,
                validationController,
                errorController,
                aufrufKontextVerwalter,
                navigationMenuController);
    }

    @Bean
    @ConditionalOnMissingBean
    public NavigationMenuController navigationMenuController(
            NavigationMenuGenerierenStrategie navigationMenuGenerierenAusKonfiguration) {
        return new NavigationMenuController(navigationMenuGenerierenAusKonfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public DetailseiteController detailseiteController(BasisController basisController) {
        return new DetailseiteController(basisController);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplikationseiteController applikationseiteController(
            LinksnavigationController linksnaviagtionController, QuicklinksController quicklinksController) {
        return new ApplikationseiteController(linksnaviagtionController, quicklinksController);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LinksnavigationController linksnavigationController(
            Konfiguration konfiguration, AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter) {
        return new LinksnavigationController(konfiguration, aufrufKontextVerwalter);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ErrorController errorController() {
        return new ErrorController();
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HilfeController hilfeController(ServletContext servletContext, Konfiguration konfiguration) {
        return new HilfeController(servletContext, konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MessageController messageController(AusnahmeIdMapper ausnahmeIdMapper) {
        return new MessageController(ausnahmeIdMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ValidationController validationController() {
        return new ValidationController();
    }


    @Bean
    @ConditionalOnMissingBean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BasisController basisController() {
        return new BasisController();
    }

    @Bean
    @ConditionalOnMissingBean
    public NavigationMenuGenerierenAusKonfiguration navigationMenuGenerierenAusKonfiguration(
            Konfiguration konfiguration, AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter) {
        return new NavigationMenuGenerierenAusKonfiguration(konfiguration, aufrufKontextVerwalter);
    }

    @Bean
    @ConditionalOnMissingBean
    public LocaleConfiguration localeConfiguration(Konfiguration konfiguration) {
        return new LocaleConfiguration(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceCacheHeaderFilter resourceCacheHeaderFilter(Konfiguration konfiguration) {
        return new ResourceCacheHeaderFilter(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadHelper downloadHelper() {
        return new DownloadHelper();
    }

    @Bean
    @ConditionalOnMissingBean
    public HeaderHelper headerHelper(Konfiguration konfiguration) {
        return new HeaderHelper(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public IsyWebKonfigurationHelper isyWebKonfigurationHelper(Konfiguration konfiguration) {
        return new IsyWebKonfigurationHelper(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsfHelper jsfHelper() {
        return new JsfHelper();
    }

    @Bean
    @ConditionalOnMissingBean
    public TitlesHelper titlesHelper(Konfiguration konfiguration) {
        return new TitlesHelper(konfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ZipHelper zipHelper() {
        return new ZipHelper();
    }
}
