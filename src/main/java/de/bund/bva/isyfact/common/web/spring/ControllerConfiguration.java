package de.bund.bva.isyfact.common.web.spring;

import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
import de.bund.bva.isyfact.common.web.jsf.components.tab.TabController;
import de.bund.bva.isyfact.common.web.layout.ApplikationseiteController;
import de.bund.bva.isyfact.common.web.layout.BasisController;
import de.bund.bva.isyfact.common.web.layout.DetailseiteController;
import de.bund.bva.isyfact.common.web.layout.HilfeController;
import de.bund.bva.isyfact.common.web.layout.LinksnavigationController;
import de.bund.bva.isyfact.common.web.layout.QuicklinksController;
import de.bund.bva.isyfact.common.web.locale.LocaleConfiguration;
import de.bund.bva.isyfact.common.web.validation.ValidationController;
import de.bund.bva.isyfact.common.web.webflow.titles.TitlesHelper;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

@Configuration
public class ControllerConfiguration {

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ErrorController createErrorController() {
        return new ErrorController();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ValidationController validationController() {
        return new ValidationController();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TabController tabController() {
        return new TabController();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnBean(AusnahmeIdMapper.class)
    public MessageController messageController(AusnahmeIdMapper ausnahmeIdMapper) {
        MessageController msgController = new MessageController();
        msgController.setAusnahmeIdMapper(ausnahmeIdMapper);
        return msgController;
    }

    @Bean
    public JsfHelper jsfHelper() {
        return new JsfHelper();
    }

    @Bean
    @ConditionalOnBean(Konfiguration.class)
    public HeaderHelper headerHelper(Konfiguration konfiguration) {
        return new HeaderHelper(konfiguration);
    }

    @Bean
    @ConditionalOnBean(Konfiguration.class)
    public LocaleConfiguration localeConfiguration(Konfiguration konfiguration) {
        LocaleConfiguration localeConfiguration = new LocaleConfiguration();
        localeConfiguration.setKonfiguration(konfiguration);
        return localeConfiguration;
    }

    @Bean
    @ConditionalOnBean(Konfiguration.class)
    public TitlesHelper titlesHelper(Konfiguration konfiguration) {
        TitlesHelper titlesHelper = new TitlesHelper();
        titlesHelper.setKonfiguration(konfiguration);
        return titlesHelper;
    }

    @Bean
    @ConditionalOnBean(Konfiguration.class)
    public IsyWebKonfigurationHelper isyWebKonfigurationHelper(Konfiguration konfiguration) {
        IsyWebKonfigurationHelper isyWebKonfigurationHelper = new IsyWebKonfigurationHelper();
        isyWebKonfigurationHelper.setKonfiguration(konfiguration);
        return isyWebKonfigurationHelper;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public GlobalConfigurationController globalConfigurationController() {
        return new GlobalConfigurationController();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnBean(AufrufKontextVerwalter.class)
    public GlobalFlowController globalFlowController(
            MessageController messageController,
            ValidationController validationController,
            ErrorController errorController,
            AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter,
            NavigationMenuController navigationMenuController) {
        GlobalFlowController globalFlowController = new GlobalFlowController();
        globalFlowController.setMessageController(messageController);
        globalFlowController.setValidationController(validationController);
        globalFlowController.setErrorController(errorController);
        globalFlowController.setAufrufKontextVerwalter(aufrufKontextVerwalter);
        globalFlowController.setNavigationMenuController(navigationMenuController);
        return globalFlowController;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BasisController basisController() {
        return new BasisController();
    }

    @Bean
    @ConditionalOnBean(NavigationMenuGenerierenStrategie.class)
    public NavigationMenuController navigationMenuController(
            NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie) {
        NavigationMenuController navigationMenuController = new NavigationMenuController();
        navigationMenuController.setNavigationMenuGenerierenStrategie(navigationMenuGenerierenStrategie);
        return navigationMenuController;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public DetailseiteController detailseiteController(BasisController basisController) {
        DetailseiteController detailseiteController = new DetailseiteController();
        detailseiteController.setBasisController(basisController);
        return detailseiteController;
    }

    @Bean
    public ApplikationseiteController applikationseiteController(
            LinksnavigationController linksnavigationController,
            QuicklinksController quicklinksController) {
        ApplikationseiteController applikationseiteController = new ApplikationseiteController();
        applikationseiteController.setLinksnavigationController(linksnavigationController);
        applikationseiteController.setQuicklinksController(quicklinksController);
        return applikationseiteController;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnBean({Konfiguration.class, AufrufKontextVerwalter.class})
    public LinksnavigationController linksnavigationController(
            Konfiguration konfiguration,
            AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter) {
        LinksnavigationController linksnavigationController = new LinksnavigationController();
        linksnavigationController.setKonfiguration(konfiguration);
        linksnavigationController.setAufrufKontextVerwalter(aufrufKontextVerwalter);
        return linksnavigationController;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnBean(Konfiguration.class)
    public QuicklinksController quicklinksController(Konfiguration konfiguration) {
        QuicklinksController quicklinksController = new QuicklinksController();
        quicklinksController.setKonfiguration(konfiguration);
        return quicklinksController;
    }

    @Bean
    public ZipHelper zipHelper() {
        return new ZipHelper();
    }

    @Bean
    public DownloadHelper downloadHelper() {
        return new DownloadHelper();
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnBean({Konfiguration.class, ServletContext.class})
    public HilfeController hilfeController(
            Konfiguration konfiguration,
            ServletContext servletContext) {
        HilfeController hilfeController = new HilfeController();
        hilfeController.setKonfiguration(konfiguration);
        hilfeController.setServletContext(servletContext);
        return hilfeController;
    }
}
