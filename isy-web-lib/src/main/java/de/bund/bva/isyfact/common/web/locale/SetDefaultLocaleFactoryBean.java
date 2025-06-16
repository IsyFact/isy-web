package de.bund.bva.isyfact.common.web.locale;

import java.util.Locale;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;

/**
 * Setzt das angegebene Default Locale.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SetDefaultLocaleFactoryBean implements BeanFactoryPostProcessor {

    /**
     * Die Locale.
     */
    private Locale locale;

    /**
     * Der Logger.
     */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(SetDefaultLocaleFactoryBean.class);

    public SetDefaultLocaleFactoryBean(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        LOG.info(LogKategorie.JOURNAL, EreignisSchluessel.E_LOKALISIERUNG, "Setzte Locale {}", this.locale);
        Locale.setDefault(this.locale);
    }

    /**
     * Locale to set.
     * @param locale
     *            Locale to set.
     */
    public void setDefaultLocale(Locale locale) {
        this.locale = locale;
    }

}
