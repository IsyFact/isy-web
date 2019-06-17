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

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Smart comparator.
 *
 * @param <T> Typ des Objekts.
 * @deprecated Die Klasse SmartComparator steht ab der Version IsyFact 2.0 nicht mehr zur Verfügung.
 */
@Deprecated
public abstract class SmartComparator<T> implements Comparator<T> {

    /** Comparator. */
    protected CompareToBuilder comparator;

    /**
     * Erzeuge comparator.
     *
     * @param o1 erste Objekt
     * @param o2 zweite Objekt
     */
    protected abstract void buildComparator(T o1, T o2);

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(T o1, T o2) {
        this.comparator = new CompareToBuilder();
        buildComparator(o1, o2);
        return this.comparator.toComparison();
    }

    /**
     * Gibt true zurück, wenn beide Objekte nicht null sind.
     *
     * @param o1 erste Objekt
     * @param o2 zweite Objekt
     * @return true wenn beide Objekte nicht null sind.
     */
    protected boolean pruefeBeideNichtNull(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return false;
        } else if (o1 == null) {
            this.comparator.append(true, false);
            return false;
        } else if (o2 == null) {
            this.comparator.append(false, true);
            return false;
        } else {
            return true;
        }
    }
}
