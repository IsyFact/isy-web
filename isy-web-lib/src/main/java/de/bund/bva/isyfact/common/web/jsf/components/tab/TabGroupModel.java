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
package de.bund.bva.isyfact.common.web.jsf.components.tab;

import java.io.Serializable;

/**
 * Das TAB Model für die Nutzung des rf:tabGroupt und rf:tab Tags. Jedes GUI-Model sollte pro Tab ein
 * entsprechendes Model bereitstellen. Es dient zur Speichern des Status und zur Konfiguration des
 * Initialzustands.
 * 
 * @author Capgemini
 * @version $Id: TabGroupModel.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
public class TabGroupModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Welches Tab aktuell ausgewählt ist.
     */
    private String currentTab;

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

}
