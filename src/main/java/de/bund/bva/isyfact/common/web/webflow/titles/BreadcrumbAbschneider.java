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
package de.bund.bva.isyfact.common.web.webflow.titles;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Mit Hilfe dieser Klasse kann die Anzahl der Breadcrumbs-Anzahl reduziert werden. Die Änderungen werden nicht
 * unabhängig von der Liste der Breadcrumbs vorgenommen, die im FlowScope gespeichert wird. Somit können die
 * Breacrumbs kürzer gerendert werden, ohne die echte echten Breadcrumbs zu beinflussen.
 * <p>
 * Die ersten Breadcrumbs werden mit "..." ohne Links ersetzt. Navigiert man anhand der Breadcrumbs zurück,
 * werden die Links wieder sichtbar.
 * 
 * @author Capgemini, Artun Subasi
 * @version $Id: BreadcrumbAbschneider.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 * @since 2.1.2
 */
@Deprecated
public class BreadcrumbAbschneider {

    /** Die Anzahl der maximal sichtbaren Breadcrumbs (inkl. Startseitenlink wenn vorhanden). */
    private int anzahlMaxBreadcrumbs = 5;

    /** Gibt an, ob der erste Link immer angezeigt wird. Wenn true, werden die "..." ab 2. Eintrag hinzugefügt. */
    private boolean sollErsterLinkImmerAngezeigtWerden;

    /**
     * Schneidet die übergebenen Breadcrumbs ab und reduziert die Anzahl so, dass maximal eine konfigurierte
     * Anzahl von Breadcrumbs dargestellt werden.
     * 
     * @param breadcrumbs
     *            alle Breadcrumbs (das übergebene Collection wird nicht modifiziert)
     * @return abgeschnitten Breadcrumbs
     */
    public Deque<BreadcrumbEintrag> schneideBreadcrumbsAb(Deque<BreadcrumbEintrag> breadcrumbs) {
        final Deque<BreadcrumbEintrag> neueBreadcrumbs = new LinkedList<BreadcrumbEintrag>(breadcrumbs);
        if (breadcrumbs.size() > anzahlMaxBreadcrumbs) {
            int anzahlDerAbzuschneidendenBreadcrumbs = breadcrumbs.size() - anzahlMaxBreadcrumbs;
            if (sollErsterLinkImmerAngezeigtWerden) {
                neueBreadcrumbs.removeFirst();
            }
            for (int i = 0; i < anzahlDerAbzuschneidendenBreadcrumbs; i++) {
                neueBreadcrumbs.removeFirst();
            }
            neueBreadcrumbs.addFirst(new BreadcrumbEintrag(null));
            if (sollErsterLinkImmerAngezeigtWerden) {
                neueBreadcrumbs.addFirst(breadcrumbs.getFirst());
            }
        }
        return neueBreadcrumbs;
    }

    /**
     * Liefert das Feld 'anzahlMaxBreadcrumbs' zurück.
     * @return Wert von anzahlMaxBreadcrumbs
     */
    public int getAnzahlMaxBreadcrumbs() {
        return anzahlMaxBreadcrumbs;
    }

    /**
     * Setzt das Feld 'anzahlMaxBreadcrumbs'.
     * @param anzahlMaxBreadcrumbs
     *            Neuer Wert für anzahlMaxBreadcrumbs
     */
    public void setAnzahlMaxBreadcrumbs(int anzahlMaxBreadcrumbs) {
        this.anzahlMaxBreadcrumbs = anzahlMaxBreadcrumbs;
    }

    /**
     * Liefert das Feld 'sollErsterLinkImmerAngezeigtWerden' zurück.
     * @return Wert von sollErsterLinkImmerAngezeigtWerden
     */
    public boolean isSollErsterLinkImmerAngezeigtWerden() {
        return sollErsterLinkImmerAngezeigtWerden;
    }

    /**
     * Setzt das Feld 'sollErsterLinkImmerAngezeigtWerden'.
     * @param sollErsterLinkImmerAngezeigtWerden
     *            Neuer Wert für sollErsterLinkImmerAngezeigtWerden
     */
    public void setSollErsterLinkImmerAngezeigtWerden(boolean sollErsterLinkImmerAngezeigtWerden) {
        this.sollErsterLinkImmerAngezeigtWerden = sollErsterLinkImmerAngezeigtWerden;
    }

}
