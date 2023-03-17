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
package de.bund.bva.isyfact.common.web.jsf.components.listpicker;

/**
 * Item der beinhaltet Schlüssel und Wert.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class SchluesselWertItem implements ListpickerItem {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Attribut: Schlüssel.
     */
    private String schluessel;

    /**
     * Attribut: Wert.
     */
    private String wert;

    /**
     * Eine anwendungsspezifische CSS-Klasse.
     */
    private String cssClass;

    /**
     * Erzeugt einen neuen Schluessel.
     *
     * @param schluessel
     *            Der Schlüssel.
     * @param wert
     *            Der Schlüsselwert.
     */
    public SchluesselWertItem(String schluessel, String wert) {
        super();
        this.schluessel = schluessel;
        this.wert = wert;
    }

    public String getSchluessel() {
        return this.schluessel;
    }

    public String getWert() {
        return this.wert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReadableValueForItem() {
        return getWert();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssClass() {
        return this.cssClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueForItem() {
        return getSchluessel();
    }

}
