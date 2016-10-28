/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.locale;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;

/**
 * Setzt das angegebene Default Locale.
 */
public class SetDefaultLocaleFactoryBean implements BeanFactoryPostProcessor {

    /**
     * Die Locale.
     */
    private Locale locale;

    /**
     * Der Logger.
     */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(SetDefaultLocaleFactoryBean.class);

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
    @Required
    public void setDefaultLocale(Locale locale) {
        this.locale = locale;
    }

}
