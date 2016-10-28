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
package de.bund.bva.isyfact.common.web.jsf.components.listpicker.behoerde;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerItem;

/**
 * Ein Behördenkennzeichen als ListpickerItem.
 */
public class BehoerdeListpickerItem implements ListpickerItem {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Das eigentliche Behördenkennzeichen.
     */
    private String kennzeichen;

    /**
     * Der lesbare Name.
     */
    private String name;

    /**
     * Eine anwendungsspezifische CSS-Klasse.
     */
    private String cssClass;

    /**
     * Konstruktor.
     *
     * @param kennzeichen
     *            Das Kennzeichen der Behörde.
     * @param name
     *            Der Name der Behörde.
     */
    public BehoerdeListpickerItem(String kennzeichen, String name) {
        super();
        this.kennzeichen = kennzeichen;
        this.name = name;
    }

    public String getKennzeichen() {
        return this.kennzeichen;
    }

    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReadableValueForItem() {
        return this.name;
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
        return this.kennzeichen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getListpickerValueForItem() {
        // TODO Auto-generated method stub
        return null;
    }

}
