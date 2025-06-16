package de.bund.bva.isyfact.common.web.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;

/**
 * Ein Bean Comparator, welcher auf dem {@link BeanComparator} basiert, jedoch mit <code>null</code>-Werten
 * umgehen kann.
 * <p>
 * Der Unterschied zu der ursprüngliche Implementierung von Capgemini ist dass diese keine Generics
 * Warnings verursacht.
 *
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
     * @param sortProperty Die Property, nach welcher sortiert werden soll.
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
