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
package de.bund.bva.isyfact.common.web.comparator;

import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Ein Bean Comparator, welcher auf dem {@link BeanComparator} basiert, jedoch mit <code>null</code>-Werten
 * umgehen kann.
 *
 * Der Unterschied zu der ursprüngliche Implementierung von Capgemini ist dass diese keine Generics
 * Warnings verursacht.
 *
 * @author msg
 * @author Capgemini
 * @param <T> Typparameter
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class NullSafeBeanComparator<T> implements Serializable, Comparator<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Die Property, nach welcher sortiert werden soll.
     */
    private String sortProperty;

    /**
     * Konstruktor.
     *
     * @param sortProperty
     *            Die Property, nach welcher sortiert werden soll.
     */
    public NullSafeBeanComparator(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(T o1, T o2) {

        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            try {
                Object o1Value = PropertyUtils.getProperty(o1, sortProperty);
                Object o2Value = PropertyUtils.getProperty(o2, sortProperty);

                if (o1Value == null && o2Value == null) {
                    return 0;
                } else if (o1Value == null) {
                    return -1;
                } else if (o2Value == null) {
                    return 1;
                } else {
                    return new BeanComparator(sortProperty).compare(o1, o2);
                }
            } catch (Throwable t) {
                throw new IsyFactTechnicalRuntimeException(FehlerSchluessel.FEHLERTEXT_GUI_FACHLICH, t,
                    "Das Vergleichen ist nicht möglich.");
            }
        }
    }
}
