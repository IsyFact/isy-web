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
package de.bund.bva.isyfact.common.web.jsf.components.panel;

import java.io.Serializable;

/**
 * Das Panel Model für die Nutzung des rf:panel Tags. Jedes GUI-Model sollte pro Panel ein entsprechendes
 * Model bereitstellen. Es dient zur Speichern des Status und zur Konfiguration des Panel Initialzustands.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class PanelModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ob das Panel ausgeklappt ist oder nicht.
     */
    private boolean ausgeklappt;

    /**
     * Default-Konstruktor.
     */
    public PanelModel() {
        super();
    }

    /**
     * Konstruktor.
     * @param ausgeklappt
     *            Ob das Panel ausgeklappt ist oder nicht.
     */
    public PanelModel(boolean ausgeklappt) {
        super();
        this.ausgeklappt = ausgeklappt;
    }

    public boolean isAusgeklappt() {
        return ausgeklappt;
    }

    public void setAusgeklappt(boolean ausgeklappt) {
        this.ausgeklappt = ausgeklappt;
    }

}
